package com.ulutman.service;

import com.ulutman.mapper.ComplaintMapper;
import com.ulutman.model.dto.ComplaintRequest;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.entities.Complaint;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.repository.ComplaintRepository;
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
public class ComplaintService {

    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;

    public ComplaintResponse createComplaint(ComplaintRequest complaintRequest) {
        Complaint complaint = new Complaint();
        complaint.setComplaintContent(complaintRequest.getComplaintContent());
        complaint.setComplaintType(complaintRequest.getComplaintType());
        complaint.setComplaintStatus(ComplaintStatus.ОЖИДАЕТ);
        complaint.setCreateDate(LocalDate.now());

        Optional<User> user = userRepository.findById(complaintRequest.getUserId());
        user.ifPresent(complaint::setUser);
        Complaint savedComplaint = complaintRepository.save(complaint);
        return complaintMapper.mapToResponse(savedComplaint);
    }
}
