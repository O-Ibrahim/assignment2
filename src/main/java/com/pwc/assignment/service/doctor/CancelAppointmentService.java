package com.pwc.assignment.service.doctor;

import com.pwc.assignment.repository.AppointmentRepository;

import java.util.UUID;

public class CancelAppointmentService {

    private final AppointmentRepository appointmentRepository;

    public CancelAppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void invoke(UUID doctorId, UUID appointmentId) {
        appointmentRepository.deleteAppointmentByIdAndDoctorId(appointmentId, doctorId);
    }

}
