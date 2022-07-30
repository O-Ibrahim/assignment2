package com.pwc.assignment.domain.dto.doctor;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListAppointmentsResponse {
    private String date;
    private List<ListAppointmentsResponsePatients> patientList;
}

