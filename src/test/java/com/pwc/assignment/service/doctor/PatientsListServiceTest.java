package com.pwc.assignment.service.doctor;

import com.pwc.assignment.domain.model.Appointment;
import com.pwc.assignment.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientsListServiceTest {

    private final AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private final PatientsListService patientsListService = new PatientsListService(appointmentRepository);

    @Test
    void getAppointmentList() {
        UUID doctorId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        List<Appointment> list = new ArrayList<>();
        when(appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date)).thenReturn(list);
        List<Appointment> actual = patientsListService.invoke(doctorId.toString(), date);
        assertEquals(actual, list);
        verify(appointmentRepository).findByDoctorIdAndAppointmentDate(doctorId, date);
    }
}