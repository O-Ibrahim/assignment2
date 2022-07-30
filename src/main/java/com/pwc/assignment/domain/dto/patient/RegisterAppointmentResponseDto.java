package com.pwc.assignment.domain.dto.patient;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class RegisterAppointmentResponseDto {
    private String patientId;
    private String drId;
    private String desiredDate;
    private String desiredTime;
    private Boolean scheduled;
}
