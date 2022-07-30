package com.pwc.assignment.repository;

import com.pwc.assignment.domain.model.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends CrudRepository<Patient, UUID> {
    Optional<Patient> findByUsername(String email);
    Patient save(Patient doctor);
}
