package com.pwc.assignment.domain.dto.doctor;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ListAppointmentsRequest {
    @NotNull
    @NotEmpty
    private String doctorId;
    @NotNull
    @NotEmpty
    private String date;
}
