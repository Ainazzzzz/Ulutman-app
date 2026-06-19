package com.ulutman.mapper;

import com.ulutman.model.dto.MessageRequest;
import com.ulutman.model.dto.MessageResponse;
import com.ulutman.model.dto.ModeratorMessageResponse;
import com.ulutman.model.entities.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final AuthMapper authMapper;

    public Message mapToEntity(MessageRequest messageRequest) {
        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setUser(message.getUser());
        message.setModeratorStatus(message.getModeratorStatus());
        return message;
    }

    public MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .moderatorStatus(message.getModeratorStatus())
                .createDate(message.getCreateDate())
                .authResponse(message.getUser() != null ? authMapper.mapToResponse(message.getUser()) : null)
                .build();
    }

    public ModeratorMessageResponse mapToModeratorMessageResponse(Message message) {
        return ModeratorMessageResponse.builder()
                .messageId(message.getId())
                .content(message.getContent())
                .moderatorStatus(message.getModeratorStatus())
                .createDate(message.getCreateDate())
                .authResponse(message.getUser()!=null?authMapper.mapToResponse(message.getUser()):null)
                .build();
    }
}
