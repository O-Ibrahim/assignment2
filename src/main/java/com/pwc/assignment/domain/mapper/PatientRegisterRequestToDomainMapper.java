package com.pwc.assignment.domain.mapper;

import com.pwc.assignment.domain.dto.patient.RegisterPatientRequestDto;
import com.pwc.assignment.domain.model.Patient;
import com.pwc.assignment.util.DateTimeUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PatientRegisterRequestToDomainMapper {

    @Autowired
    DateTimeUtil dateTimeUtil;

    public Patient toDomain(RegisterPatientRequestDto requestDto) {
        return Patient.builder()
                .name(requestDto.getName())
                .nationalId(requestDto.getNID())
                .password(requestDto.getPassword())
                .dob(dateTimeUtil.parseDate(requestDto.getDOB()))
                .username(requestDto.getUsername())
                .build();
    }
}
