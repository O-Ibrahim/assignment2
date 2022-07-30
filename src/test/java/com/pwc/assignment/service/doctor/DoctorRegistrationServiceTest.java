package com.pwc.assignment.service.doctor;

import com.pwc.assignment.domain.model.Doctor;
import com.pwc.assignment.exception.UsernameAlreadyUsedException;
import com.pwc.assignment.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorRegistrationServiceTest {

    private final DoctorRepository doctorRepository = mock(DoctorRepository.class);
    private final PasswordEncoder passwordEncoder= mock(PasswordEncoder.class);
    private final DoctorRegistrationService  doctorRegistrationService = new DoctorRegistrationService(doctorRepository, passwordEncoder);

    @Test
    void givenValidDoctor_whenRegistering_Pass() throws UsernameAlreadyUsedException {
        String encodedPassword = "encodedPassword";
        Doctor doctor = new Doctor();
        doctor.setUsername("username");
        doctor.setPassword("password");
        when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(doctor.getPassword())).thenReturn(encodedPassword);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        Doctor actual = doctorRegistrationService.invoke(doctor);
        verify(doctorRepository).findByUsername(doctor.getUsername());
        verify(doctorRepository).save(doctor);
        assertEquals(actual.getPassword(), encodedPassword);
        assertEquals(actual, doctor);
    }
    @Test
    void givenDoctorWithUsedUsername_whenRegistering_ThrowException() {
        Doctor doctor = new Doctor();
        when(doctorRepository.findByUsername(doctor.getUsername())).thenReturn(Optional.of(doctor));
        assertThrows(UsernameAlreadyUsedException.class, () -> {
            doctorRegistrationService.invoke(doctor);
        });
        verify(doctorRepository).findByUsername(doctor.getUsername());
    }
}