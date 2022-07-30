package com.pwc.assignment.service.patient;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class DoctorAppointments {
    private String doctorName;
    private List<String> availableHours;
}