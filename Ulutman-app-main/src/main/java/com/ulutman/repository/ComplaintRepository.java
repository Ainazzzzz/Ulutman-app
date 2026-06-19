package com.ulutman.repository;

import com.ulutman.model.entities.Complaint;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.model.enums.ComplaintType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query("SELECT c FROM Complaint c WHERE c.user.id IN :userIds")
    List<Complaint> findByUserIdIn(@Param("userIds") List<Long> userIds);

    @Query("""
        SELECT complaint FROM Complaint complaint WHERE 
        (:complaintStatuses IS NULL OR complaint.complaintStatus IN :complaintStatuses)
    """)
    List<Complaint> findByComplaintStatus(@Param("complaintStatuses") List<ComplaintStatus> complaintStatuses);

    @Query("""
        SELECT complaint FROM Complaint complaint WHERE 
        (:createDates IS NULL OR complaint.createDate IN :createDates)
    """)
    List<Complaint> findByCreateDateIn(@Param("createDates") List<LocalDate> createDates);

  @Query("""
          SELECT c FROM Complaint c WHERE
          (:complaintTypes IS NULL OR c.complaintType IN :complaintTypes)
          """)
    List<Complaint> findByComplaintType(@Param("complaintTypes") List<ComplaintType> complaintTypes);
}

