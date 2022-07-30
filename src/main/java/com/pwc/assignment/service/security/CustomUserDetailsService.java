package com.pwc.assignment.service.security;

import com.pwc.assignment.domain.model.Doctor;
import com.pwc.assignment.domain.model.Patient;
import com.pwc.assignment.repository.DoctorRepository;
import com.pwc.assignment.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;
import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Doctor doctorByUsername = getDoctorByUsername(username);
        if (Objects.nonNull(doctorByUsername)) {
            return new User(username, doctorByUsername.getPassword(), doctorByUsername.getAuthoritiesEntities());
        }
        Patient patientByUsername = getPatientByUsername(username);
        if (Objects.nonNull(patientByUsername)) {
            return new User(username, patientByUsername.getPassword(), patientByUsername.getAuthoritiesEntities());
        }
        throw new UsernameNotFoundException("User " + username + " Not found.");
    }

    private Doctor getDoctorByUsername(String username) {
        Optional<Doctor> doctorByUsername = doctorRepository.findByUsername(username);
        return doctorByUsername.orElse(null);
    }

    private Patient getPatientByUsername(String username) {
        Optional<Patient> patientRepositorylByUsername = patientRepository.findByUsername(username);
        return patientRepositorylByUsername.orElse(null);
    }
}
