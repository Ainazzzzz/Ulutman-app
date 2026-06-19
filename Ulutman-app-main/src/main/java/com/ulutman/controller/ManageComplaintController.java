package com.ulutman.controller;

import com.ulutman.exception.NotFoundException;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.model.enums.ComplaintType;
import com.ulutman.service.ManageComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/complaints")
@Tag(name = "Manage Complaint")
@SecurityRequirement(name = "Authorization")
public class ManageComplaintController {

    private final ManageComplaintService complaintService;
    private final ManageComplaintService manageComplaintService;

    @Operation(summary = "Get all complaints")
    @ApiResponse(responseCode = "201", description = "Return list of complaints")
    @GetMapping("/getAll")
    public List<ComplaintResponse> getAll() {
        return manageComplaintService.getAllComplaints();
    }

    @Operation(summary = "Update complaint status")
    @ApiResponse(responseCode = "201", description = "Updated complaint status successfully")
    @PutMapping("/update/status/{id}")
    public ResponseEntity<ComplaintResponse> updateComplaintStatus(
            @PathVariable Long id,
            @RequestParam ComplaintStatus newStatus) {

        ComplaintResponse response = manageComplaintService.updateComplaintStatus(id, newStatus);
        return ResponseEntity.ok(response); // Возвращаем ответ с обновленной жалобой
    }

    @Operation(summary = "Filter complaints by criteria")
    @ApiResponse(responseCode = "201", description = "Complaints successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<ComplaintResponse>> filterComplaints(
            @RequestParam(required = false) List<ComplaintStatus> complaintStatuses,
            @RequestParam(required = false) List<ComplaintType> complaintTypes,
            @RequestParam(required = false) List<LocalDate> createDates,
            @RequestParam(required = false) String names) {

        List<ComplaintResponse> filteredComplaint = manageComplaintService.filterComplaints(complaintStatuses, complaintTypes, createDates, names);
        return ResponseEntity.ok(filteredComplaint);
    }

    @Operation(summary = "Reset filters complaints")
    @ApiResponse(responseCode = "201", description = "Complaints filters successfully reset")
    @GetMapping("/resetFilter")
    public List<ComplaintResponse> resetFilter() {
        return manageComplaintService.getAllComplaints();
    }

    @Operation(summary = "Delete complaint by arrays by id")
    @ApiResponse(responseCode = "201", description = "Deleted, the array of complaints is successful")
    @DeleteMapping("/delete/batch")
    public ResponseEntity<String> deleteComplaintsByIds(@RequestBody List<Long> ids) {
        try {
            manageComplaintService.deleteComplaintsByIds(ids);
            return ResponseEntity.ok("Жалобы успешно удалены");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Некоторые жалобы  не найдены");
        }
    }
}

