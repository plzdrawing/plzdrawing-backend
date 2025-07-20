package org.example.plzdrawing.api.chatRoom.service;

import static org.example.plzdrawing.api.chatRoom.exception.ChatRoomErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.chatRoom.dto.converter.ChatRoomConverter;
import org.example.plzdrawing.api.chatRoom.dto.request.CreateChatRoomRequest;
import org.example.plzdrawing.api.chatRoom.dto.response.ResponseChatRoom;
import org.example.plzdrawing.api.chatRoom.exception.ChatRoomAlreadyExistsException;
import org.example.plzdrawing.api.chatRoom.repository.UnreadCountRepository;
import org.example.plzdrawing.api.member.service.MemberService;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.domain.chat.Chat;
import org.example.plzdrawing.domain.chatroom.ChatRoom;
import org.example.plzdrawing.domain.chatroom.ChatRoomRepository;
import org.example.plzdrawing.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final MemberService memberService;
    private final ChatRoomRepository chatRoomRepository;
    private final UnreadCountRepository unreadCountRepository;

    @Override
    @Transactional
    public String createChatRoom(Long memberId, CreateChatRoomRequest request) {
        Long sellerId = request.getSellerId();
        chatRoomRepository.findBySellerIdAndBuyerId(sellerId,memberId)
                .ifPresent(existingRoom -> {throw new ChatRoomAlreadyExistsException(existingRoom);});

        Map<Long, Member> members = memberListToMap(memberService.findMembersByIds(createMemberIdList(memberId, sellerId)));
        ChatRoom chatRoom = ChatRoom.create(members.get(sellerId), members.get(memberId));
        return chatRoomRepository.save(chatRoom).getChatRoomId();
    }

    @Override
    @Transactional
    public void updateLastMessage(Chat chat) {
        ChatRoom chatRoom = chatRoomRepository.findById(chat.getChatRoomId())
                .orElseThrow(() -> new RestApiException(CHATROOM_NOT_FOUND.getErrorCode()));
        chatRoom.updateLastMessage(chat.getDisplayContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseChatRoom> getChatRooms(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findBySellerIdOrBuyerId(memberId, memberId);
        return chatRooms.stream()
                .map(chatRoom -> {
                    String counterpartNickname = pickCounterpartNickname(memberId,chatRoom);
                    int unreadCount = unreadCountRepository.getUnreadCount(chatRoom.getChatRoomId(), memberId);
                    return ChatRoomConverter.fromEntity(chatRoom, counterpartNickname, unreadCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatRoom findById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RestApiException(CHATROOM_NOT_FOUND.getErrorCode()));
    }

    private String pickCounterpartNickname(Long memberId, ChatRoom chatRoom) {
        if(Objects.equals(memberId, chatRoom.getSellerId())) {
            return chatRoom.getBuyerNickname();
        }
        return chatRoom.getSellerNickname();
    }

    private List<Long> createMemberIdList(Long buyerId, Long sellerId) {
        List<Long> memberIds = new ArrayList<>();
        memberIds.add(buyerId);
        memberIds.add(sellerId);
        return memberIds;
    }

    private Map<Long, Member> memberListToMap(List<Member> members) {
        return members.stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));
    }
}
