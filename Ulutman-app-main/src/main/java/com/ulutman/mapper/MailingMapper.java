package com.ulutman.mapper;

import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.MailingRequest;
import com.ulutman.model.dto.MailingResponse;
import com.ulutman.model.dto.UserMailingResponse;
import com.ulutman.model.entities.Mailing;
import com.ulutman.model.entities.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MailingMapper {

    public Mailing mapToEntity(MailingRequest mailingRequest) {
        Mailing mailing = new Mailing();
        mailing.setTitle(mailingRequest.getTitle());
        mailing.setMessage(mailingRequest.getMessage());
        mailing.setMailingType(mailingRequest.getMailingType());
        mailing.setMailingStatus(mailingRequest.getMailingStatus());
        mailing.setPromotionStartDate(mailingRequest.getPromotionStartDate());
        mailing.setPromotionEndDate(mailingRequest.getPromotionEndDate());
        mailing.setCreateDate(LocalDate.now());
        return mailing;
    }

    public MailingResponse mapToResponse(Mailing mailing) {
        return MailingResponse.builder()
                .id(mailing.getId())
                .title(mailing.getTitle())
                .title(mailing.getTitle())
                .message(mailing.getMessage())
                .mailingType(mailing.getMailingType())
                .mailingStatus(mailing.getMailingStatus())
                .promotionStartDate(mailing.getPromotionStartDate())
                .promotionEndDate(mailing.getPromotionEndDate())
                .createDate(mailing.getCreateDate())
                .recipients(Optional.ofNullable(mailing.getRecipients())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(this::mapToUserMailingResponse)
                        .collect(Collectors.toList()))

                .build();
    }

    public UserMailingResponse mapToUserMailingResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserMailingResponse.builder()
                .userName(user.getName())
                .email(user.getEmail())
                .build();
    }


    private AuthResponse mapUserToAuthResponse(User user) {
        if (user == null) {
            return null;
        }
        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createDate(user.getCreateDate())
                .build();
    }
}
