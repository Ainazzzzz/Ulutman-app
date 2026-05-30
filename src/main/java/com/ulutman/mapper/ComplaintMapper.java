package com.ulutman.mapper;

import com.ulutman.model.dto.ComplaintRequest;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.entities.Complaint;
import com.ulutman.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComplaintMapper {

    public Complaint mapToEntity(ComplaintRequest complaintRequest) {
        Complaint complaint = new Complaint();
        complaint.setComplaintType(complaintRequest.getComplaintType());
        complaint.setComplaintContent(complaintRequest.getComplaintContent());
        complaint.setUser(complaint.getUser());
        return complaint;
    }

    public ComplaintResponse mapToResponse(Complaint complaint) {

        User user = complaint.getUser();
        String userNameResult = user != null ? user.getName() : "Неизвестно";
        return ComplaintResponse.builder()
                .id(complaint.getId())
                .userName(complaint.getUser().getName())
                .complaintType(complaint.getComplaintType())
                .complaintContent(complaint.getComplaintContent())
                .complaintStatus(complaint.getComplaintStatus())
                .createDate(complaint.getCreateDate())
                .build();
    }
}
