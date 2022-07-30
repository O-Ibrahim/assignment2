package com.pwc.assignment.domain.mapper;

import com.pwc.assignment.domain.dto.doctor.RegisterDoctorRequestDto;
import com.pwc.assignment.domain.model.Doctor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class DoctorRegisterRequestToDomainMapper {
    public Doctor toDomain(RegisterDoctorRequestDto requestDto) {
        return Doctor.builder()
                .name(requestDto.getName())
                .nationalId(requestDto.getNationalId())
                .password(requestDto.getPassword())
                .speciality(requestDto.getSpeciality())
                .username(requestDto.getUsername())
                .build();
    }
}

