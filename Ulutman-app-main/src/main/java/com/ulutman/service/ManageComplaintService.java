package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.ComplaintMapper;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.entities.Complaint;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.model.enums.ComplaintType;
import com.ulutman.repository.ComplaintRepository;
import com.ulutman.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ComplaintMapper complaintMapper;
    private final MailingService mailingService;

    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAll().stream().map(complaintMapper::mapToResponse).collect(Collectors.toList());
    }

    public ComplaintResponse updateComplaintStatus(Long id, ComplaintStatus newStatus) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жалоба по идентификатору " + id + " не найдена"));

        if (complaint.getComplaintStatus() != newStatus) {
            complaint.setComplaintStatus(newStatus);

            if (newStatus == ComplaintStatus.ОТКЛОНЕН) {
                String userEmail = complaint.getUser().getEmail();
                String complaintDetails = complaint.getComplaintContent();

                complaintRepository.delete(complaint);

                try {
                    mailingService.sendComplaintRejectionNotification(userEmail, complaintDetails);
                } catch (MessagingException e) {
                    log.error("Ошибка при отправке уведомления об отклонении жалобы с id {} на email: {}", id, e.getMessage());
                }
            } else {
                complaintRepository.save(complaint);
            }
        }

        return complaintMapper.mapToResponse(complaint);
    }

    public List<ComplaintResponse> filterComplaints(
            List<ComplaintStatus> complaintStatuses,
            List<ComplaintType> complaintTypes,
            List<LocalDate> createDates,
            String names) {

        List<Complaint> filteredComplaints = complaintRepository.findAll();

        if (complaintTypes != null && complaintTypes.stream().anyMatch(type -> type == null)) {
            throw new IllegalArgumentException("Типы жалоб не могут содержать нулевых значений.");
        } else if (complaintTypes != null && !complaintTypes.isEmpty()) {
            filteredComplaints = filteredComplaints.stream()
                    .filter(complaint -> complaintTypes.contains(complaint.getComplaintType()))
                    .collect(Collectors.toList());
        }

        if (complaintStatuses != null && complaintStatuses.stream().anyMatch(status -> status == null)) {
            throw new IllegalArgumentException("Статусы публикаций не могут содержать нулевых значений.");
        } else if (complaintStatuses != null && !complaintStatuses.isEmpty()) {
            filteredComplaints = filteredComplaints.stream()
                    .filter(complaint -> complaintStatuses.contains(complaint.getComplaintStatus()))
                    .collect(Collectors.toList());
        }

        if (createDates != null && createDates.stream().anyMatch(date -> date == null)) {
            throw new IllegalArgumentException("Даты создания не могут содержать нулевых значений.");
        } else if (createDates != null && !createDates.isEmpty()) {
            filteredComplaints = filteredComplaints.stream()
                    .filter(complaint -> createDates.contains(complaint.getCreateDate()))
                    .collect(Collectors.toList());
        }

        if (names != null && !names.trim().isEmpty()) {
            List<User> users = userRepository.findByUserName(names);
            if (users.isEmpty()) {
                return Collections.emptyList();
            }
            List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
            filteredComplaints = filteredComplaints.stream()
                    .filter(complaint -> userIds.contains(complaint.getUser().getId()))
                    .collect(Collectors.toList());
        }

        if (filteredComplaints.isEmpty()) {
            return Collections.emptyList();
        }

        return filteredComplaints.stream()
                .map(complaint -> {
                    User user = complaint.getUser();
                    String userNameResult = user != null ? user.getName() : "Неизвестно";

                    ComplaintResponse response = complaintMapper.mapToResponse(complaint);
                    response.setUserName(userNameResult);
                    return response;
                })
                .collect(Collectors.toList());
    }

    public void deleteComplaintsByIds(List<Long> ids) {
        List<Complaint> complaints = complaintRepository.findAllById(ids);

        if (complaints.isEmpty()) {
            throw new NotFoundException("Жалобы с такими ID не найдены");
        }

        complaintRepository.deleteAll(complaints);
    }
}
