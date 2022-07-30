package com.pwc.assignment.domain.dto.patient;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class RegisterAppointmentRequestDto {
    @NotNull
    @NotEmpty
    private String patientId;
    @NotNull
    @NotEmpty
    private String drId;
    @NotNull
    @NotEmpty
    private String desiredDate;
    @NotNull
    @NotEmpty
    private String desiredTime;
}
