package com.pwc.assignment.domain.dto.doctor;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CancelAppointmentRequestDto {
    @NotNull
    @NotEmpty
    private String appointmentId;
    @NotNull
    @NotEmpty
    private String doctorId;
}
