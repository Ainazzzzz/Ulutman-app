package com.ulutman.repository;

import com.ulutman.model.entities.Message;
import com.ulutman.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByUserId(Long userId);

    @Query("SELECT m FROM Message m WHERE " +
           "(:users IS NULL OR m.user.id = :users) AND " +
           "(:content IS NULL OR LOWER( m.content) IN :content) AND " +
           "(:createDates IS NULL OR m.createDate IN :createDates) AND " +
           "(:moderatorStatuses IS NULL OR LOWER(m.moderatorStatus)  IN :moderatorStatuses)")
    List<Message> findMessagesByFilters(
            @Param("users") List<User> users,
            @Param("content") List<String> content,
            @Param("createDates") List<LocalDate> createDates,
            @Param("moderatorStatuses") List<String> moderatorStatuses);
}

