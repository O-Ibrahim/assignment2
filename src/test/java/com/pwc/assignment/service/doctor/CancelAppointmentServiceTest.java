package com.pwc.assignment.service.doctor;

import com.pwc.assignment.repository.AppointmentRepository;
import com.pwc.assignment.service.doctor.CancelAppointmentService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

class CancelAppointmentServiceTest {
    private final AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private final CancelAppointmentService cancelAppointmentService= new CancelAppointmentService(appointmentRepository);
    @Test
    void givenValidAppointmentIdAndDoctorId_whenDeleting_DeleteCalled() {
        UUID doctorId = UUID.randomUUID();
        UUID appointmentId = UUID.randomUUID();
        cancelAppointmentService.invoke(doctorId, appointmentId);
        verify(appointmentRepository).deleteAppointmentByIdAndDoctorId(appointmentId, doctorId);
    }

}