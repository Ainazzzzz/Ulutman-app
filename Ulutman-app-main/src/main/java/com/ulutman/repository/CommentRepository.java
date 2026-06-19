package com.ulutman.repository;

import com.ulutman.model.entities.Comment;
import com.ulutman.model.enums.ModeratorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
    List<Comment> findByUserId(@Param("userId") Long userId);

    void deleteById(Long id);

    List<Comment> findByUserIdIn(List<Long> userIds);

    @Query("""
        SELECT comment FROM Comment comment WHERE 
        (:content IS NULL OR :content = '' OR LOWER(comment.content) LIKE LOWER(CONCAT('%', :content, '%')))
    """)
    List<Comment> commentsFilterByContents(@Param("content") String content);

    @Query("""
            SELECT comment FROM Comment comment WHERE
            (:moderatorStatuses IS NULL OR comment.moderatorStatus IN :moderatorStatuses)
            """)
    List<Comment> filterCommentByModeratorStatus(@Param("moderatorStatuses") List<ModeratorStatus> moderatorStatuses);

    @Query("""
            SELECT comment FROM Comment comment WHERE
            (:createDates IS NULL OR comment.createDate IN :createDates)
            """)
    List<Comment> findByModeratorByCreateDate(@Param("createDates") List<LocalDate> createDates);
}
