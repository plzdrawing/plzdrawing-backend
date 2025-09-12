package org.example.plzdrawing.api.chat.service;

import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.chat.dto.request.ChatDto;
import org.example.plzdrawing.api.chat.dto.response.ResponseChat;
import org.example.plzdrawing.api.chat.dto.converter.ChatConverter;
import org.example.plzdrawing.api.chat.repository.ChatPresenceRepository;
import org.example.plzdrawing.api.chatRoom.repository.UnreadCountRepository;
import org.example.plzdrawing.api.chatRoom.service.ChatRoomService;
import org.example.plzdrawing.domain.chat.Chat;
import org.example.plzdrawing.domain.chat.ChatRepository;
import org.example.plzdrawing.domain.chatroom.ChatRoom;
import org.example.plzdrawing.util.websocket.MessagePublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;
    private final UnreadCountRepository unreadCountRepository;
    private final ChatPresenceRepository chatPresenceRepository;
    private final MessagePublisher messagePublisher;
    private static final int PAGE_SIZE = 20;

    @Override
    @Transactional
    public void saveMessage(ChatDto chatDto, Timestamp sendTime) {
        Chat chat = ChatConverter.toEntity(chatDto, sendTime);
        ChatRoom chatRoom = chatRoomService.findById(chat.getChatRoomId());
        String chatRoomId = chatRoom.getChatRoomId();
        Long recipientId = extractRecipientId(chat, chatRoom);

        if (!chatPresenceRepository.isActive(chatRoomId, recipientId)) {
            unreadCountRepository.incrementUnreadCount(chatRoomId, recipientId);
            chat.read(false);
            messagePublisher.publishNotExist(chatRoomId, chat);
        } else {
            messagePublisher.publishExist(chatRoomId, chat);
        }

        chatRepository.save(chat);
        chatRoomService.updateLastMessage(chat);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResponseChat> getChats(String chatRoomId, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("timestamp").descending());
        Page<Chat> chatPage = chatRepository.findByChatRoomId(chatRoomId, pageable);

        return chatPage.map(ChatConverter::fromEntity);
    }

    private Long extractRecipientId(Chat chat, ChatRoom chatRoom) {
        if (chat.getSenderId().equals(chatRoom.getSellerId())) {
            return chatRoom.getBuyerId();
        }
        return chatRoom.getSellerId();
    }
}
