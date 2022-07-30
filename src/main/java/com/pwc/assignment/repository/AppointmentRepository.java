package com.pwc.assignment.repository;

import com.pwc.assignment.domain.model.Appointment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends CrudRepository<Appointment, UUID> {

    List<Appointment> findByDoctorIdAndAppointmentDate(UUID doctorId, LocalDate appointmentDate);
    Appointment findByDoctorIdAndAppointmentDateAndAppointmentTime(UUID doctorId, LocalDate appointmentDate, LocalTime appointmentTime);

    @Modifying
    @Transactional
    @Query(value = "DELETE From APPOINTMENT_TABLE where id=:id and doctor_id=:doctorId", nativeQuery = true)
    void deleteAppointmentByIdAndDoctorId(UUID id, UUID doctorId);


    Optional<Appointment> findByPatientIdAndAppointmentDate(UUID id, LocalDate appointmentDate);

    List<Appointment> findByAppointmentDate(LocalDate desiredDate);
}


