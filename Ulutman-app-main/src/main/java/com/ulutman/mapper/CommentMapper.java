package com.ulutman.mapper;

import com.ulutman.model.dto.CommentRequest;
import com.ulutman.model.dto.CommentResponse;
import com.ulutman.model.dto.FilteredCommentResponse;
import com.ulutman.model.dto.ModeratorCommentResponse;
import com.ulutman.model.entities.Comment;
import com.ulutman.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public Comment mapToEntity(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        return comment;
    }

    public CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .moderatorStatus(comment.getModeratorStatus())
                .createDate(comment.getCreateDate())
                .build();
    }

    public ModeratorCommentResponse mapToModeratorCommentResponse(Comment comment) {
        return ModeratorCommentResponse.builder()
                .commentId(comment.getId())
                .commentId(comment.getId())
                .commentContent(comment.getContent())
                .moderatorStatus(comment.getModeratorStatus())
                .createDate(comment.getCreateDate())
                .build();
    }

    public FilteredCommentResponse mapToFilterResponse(Comment comment) {
        User user = comment.getUser();
        String userNameResult = user != null ? user.getName() : "Неизвестно";

        return FilteredCommentResponse.builder()
                .id(comment.getId())
                .userName(userNameResult)
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .moderatorStatus(comment.getModeratorStatus())
                .build();
    }
}
