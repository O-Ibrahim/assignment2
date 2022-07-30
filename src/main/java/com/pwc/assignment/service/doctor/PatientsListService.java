package com.pwc.assignment.service.doctor;

import com.pwc.assignment.domain.model.Appointment;
import com.pwc.assignment.repository.AppointmentRepository;
import com.pwc.assignment.repository.DoctorRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PatientsListService {

    private final AppointmentRepository appointmentRepository;

    public PatientsListService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> invoke(String doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndAppointmentDate(UUID.fromString(doctorId), date);
    }

}
