package com.pwc.assignment.repository;

import com.pwc.assignment.domain.model.Doctor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends CrudRepository<Doctor, UUID> {
    Optional<Doctor> findByUsername(String username);

    Doctor save(Doctor doctor);

    List<Doctor> findAll();

    @Query(value = "select dt.id, dt.name from DOCTOR_TABLE dt", nativeQuery = true)
    List<Doctor> getIdsAndNames();
}

