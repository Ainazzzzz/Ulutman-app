package com.ulutman.repository;

import com.ulutman.model.entities.Mailing;
import com.ulutman.model.enums.MailingStatus;
import com.ulutman.model.enums.MailingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MailingRepository extends JpaRepository<Mailing, Long> {

    @Query("""
                SELECT mailing FROM Mailing mailing WHERE
                (:titles IS NULL OR LOWER(mailing.title) LIKE LOWER(CONCAT('%', :titles, '%')))
            """)
    List<Mailing> mailingFilterByTitle(@Param("titles") String titles);

    @Query("""
            SELECT mailing FROM Mailing mailing WHERE
            (:mailingTypes IS NULL OR mailing.mailingType IN :mailingTypes) AND
            (:mailingStatuses IS NULL OR mailing.mailingStatus IN :mailingStatuses) AND
            (:createDates IS NULL OR mailing.createDate IN :createDates)
            """)
    List<Mailing> filterMailing(
            @Param("mailingTypes") List<MailingType> mailingTypes,
            @Param("mailingStatuses") List<MailingStatus> mailingStatuses,
            @Param("createDates") List<LocalDate> createDates
    );
}
