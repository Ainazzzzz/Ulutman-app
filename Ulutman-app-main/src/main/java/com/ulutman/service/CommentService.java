package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.CommentMapper;
import com.ulutman.model.dto.CommentRequest;
import com.ulutman.model.dto.CommentResponse;
import com.ulutman.model.entities.Comment;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.repository.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentResponse createComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setModeratorStatus(ModeratorStatus.ОЖИДАЕТ);
        comment.setCreateDate(LocalDate.now());

        Optional<User> user = userRepository.findById(commentRequest.getUserId());
        user.ifPresent(comment::setUser);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.mapToResponse(savedComment);
    }

    public CommentResponse updateCommentStatus(Long commentId, CommentRequest commentRequest) {
        if (commentId == null || commentRequest == null) {
            throw new IllegalArgumentException("Идентификатор комментария и запрос сообщения не могут быть пустыми");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий  по идентификатору " + commentId + " не найден"));

        ModeratorStatus newStatus = commentRequest.getModeratorStatus();

        if (newStatus != null && !newStatus.equals(comment.getModeratorStatus())) {
            comment.setModeratorStatus(newStatus);
            commentRepository.save(comment);
        }

        return commentMapper.mapToResponse(comment);
    }
}
