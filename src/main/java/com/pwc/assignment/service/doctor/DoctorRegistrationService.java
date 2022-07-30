package com.pwc.assignment.service.doctor;

import com.pwc.assignment.domain.model.Doctor;
import com.pwc.assignment.exception.UsernameAlreadyUsedException;
import com.pwc.assignment.repository.DoctorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

public class DoctorRegistrationService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorRegistrationService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Doctor invoke(Doctor doctor) throws UsernameAlreadyUsedException {
        Optional<Doctor> byUsername = doctorRepository.findByUsername(doctor.getUsername());
        if (byUsername.isPresent()) {
            throw new UsernameAlreadyUsedException();
        }
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);

    }
}
