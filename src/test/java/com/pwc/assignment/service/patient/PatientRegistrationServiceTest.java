package com.pwc.assignment.service.patient;

import com.pwc.assignment.domain.model.Patient;
import com.pwc.assignment.exception.UsernameAlreadyUsedException;
import com.pwc.assignment.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PatientRegistrationServiceTest {
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final PatientRegistrationService patientRegistrationService = new PatientRegistrationService(patientRepository, passwordEncoder);

    @Test
    void givenValidPatient_whenRegistering_Pass() throws UsernameAlreadyUsedException {
        String encodedPassword = "encodedPassword";
        Patient patient = new Patient();
        patient.setUsername("username");
        patient.setPassword("password");
        when(patientRepository.findByUsername(patient.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(patient.getPassword())).thenReturn(encodedPassword);
        when(patientRepository.save(patient)).thenReturn(patient);
        Patient actual = patientRegistrationService.registerPatientService(patient);
        verify(patientRepository).findByUsername(patient.getUsername());
        verify(patientRepository).save(patient);
        assertEquals(actual.getPassword(), encodedPassword);
        assertEquals(actual, patient);
    }

    @Test
    void givenPatientWithUsedUsername_whenRegistering_ThrowException() {
        Patient patient = new Patient();
        when(patientRepository.findByUsername(patient.getUsername())).thenReturn(Optional.of(patient));
        assertThrows(UsernameAlreadyUsedException.class, () -> {
            patientRegistrationService.registerPatientService(patient);
        });
        verify(patientRepository).findByUsername(patient.getUsername());
    }
}