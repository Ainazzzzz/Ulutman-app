package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.MessageMapper;
import com.ulutman.model.dto.MessageRequest;
import com.ulutman.model.dto.MessageResponse;
import com.ulutman.model.entities.Message;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.repository.MessageRepository;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    public MessageResponse createMessage(MessageRequest messageRequest) {
        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setModeratorStatus(ModeratorStatus.ОЖИДАЕТ);
        message.setCreateDate(LocalDate.now());

        Optional<User> user = userRepository.findById(messageRequest.getUserId());
        user.ifPresent(message::setUser);
        Message savedMessage = messageRepository.save(message);
        return messageMapper.mapToResponse(savedMessage);
    }

    public MessageResponse updateMessageStatus(Long messageId, MessageRequest messageRequest) {

        if (messageId == null || messageRequest == null) {
            throw new IllegalArgumentException("Message ID and MessageRequest cannot be null");
        }

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Сообщение по идентификатору " + messageId + " не найдено"));

        ModeratorStatus newStatus = messageRequest.getModeratorStatus();

        if (newStatus != null && !newStatus.equals(message.getModeratorStatus())) {
            message.setModeratorStatus(newStatus);
            messageRepository.save(message);
        }

        return messageMapper.mapToResponse(message);
    }
}
