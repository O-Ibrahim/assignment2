package com.pwc.assignment.domain.dto.doctor;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ListAppointmentsResponsePatients {
    private String patientName;
    private String appointmentId;
}
