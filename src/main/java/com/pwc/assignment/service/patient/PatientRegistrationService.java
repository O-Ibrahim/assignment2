package com.pwc.assignment.service.patient;

import com.pwc.assignment.domain.model.Patient;
import com.pwc.assignment.exception.UsernameAlreadyUsedException;
import com.pwc.assignment.repository.PatientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

public class PatientRegistrationService {

    private final PatientRepository patientRepository;

    private final PasswordEncoder passwordEncoder;

    public PatientRegistrationService(PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Patient registerPatientService(Patient patient) throws UsernameAlreadyUsedException {
        Optional<Patient> byUsername = patientRepository.findByUsername(patient.getUsername());
        if (byUsername.isPresent()) {
            throw new UsernameAlreadyUsedException();
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        return patientRepository.save(patient);

    }
}
