package org.example.plzdrawing.api.chat.service;

import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.chat.dto.request.ChatDto;
import org.example.plzdrawing.api.chat.dto.response.ResponseChat;
import org.example.plzdrawing.api.chat.dto.converter.ChatConverter;
import org.example.plzdrawing.api.chatRoom.service.ChatRoomService;
import org.example.plzdrawing.domain.chat.Chat;
import org.example.plzdrawing.domain.chat.ChatRepository;
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
    private static final int PAGE_SIZE = 20;

    @Override
    @Transactional
    public void saveMessage(ChatDto chatDto, Timestamp sendTime) {
        Chat chat = ChatConverter.toEntity(chatDto, sendTime);
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
}
