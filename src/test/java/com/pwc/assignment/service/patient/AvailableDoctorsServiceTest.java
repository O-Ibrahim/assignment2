package com.pwc.assignment.service.patient;

import com.pwc.assignment.domain.model.Appointment;
import com.pwc.assignment.domain.model.Doctor;
import com.pwc.assignment.repository.AppointmentRepository;
import com.pwc.assignment.repository.DoctorRepository;
import com.pwc.assignment.util.DateTimeUtil;
import com.pwc.assignment.util.EnvValueUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AvailableDoctorsServiceTest {
    private final DoctorRepository doctorRepository = mock(DoctorRepository.class);
    private final AppointmentRepository appointmentRepository=mock(AppointmentRepository.class);
    private final EnvValueUtil envValueUtil = mock(EnvValueUtil.class);
    private final DateTimeUtil dateTimeUtil= mock(DateTimeUtil.class);
    private final AvailableDoctorsService availableDoctorsService = new AvailableDoctorsService(doctorRepository,
            appointmentRepository, dateTimeUtil, envValueUtil);

    @Test
    void givenValidRequest_whenInvoking_returnList() {
        LocalDate desiredDate = LocalDate.now();
        UUID doctorId = UUID.randomUUID();
        List<String> availableHours = new ArrayList<>();
        List<Appointment> appointments = List.of(getAppointment(doctorId));
        availableHours.add("09:00");
        availableHours.add("10:00");
        when(envValueUtil.getDatePattern()).thenReturn("dd/MM/yyyy");
        when(envValueUtil.getTimePattern()).thenReturn("hh:mm a");
        when(envValueUtil.getAvailableHours()).thenReturn(availableHours);
        when(dateTimeUtil.parseDate(anyString())).thenReturn(LocalDate.now());
        when(doctorRepository.findAll()).thenReturn(List.of(getDoctor(doctorId)));
        when(appointmentRepository.findByAppointmentDate(any(LocalDate.class))).thenReturn(appointments);

        List<DoctorAppointments> list = availableDoctorsService.getList(desiredDate.toString());
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getAvailableHours().size(), 1);
        assertEquals(list.get(0).getAvailableHours().get(0), "10:00");
        verify(envValueUtil).getAvailableHours();
        verify(dateTimeUtil).parseDate(anyString());
        verify(doctorRepository).findAll();
        verify(appointmentRepository).findByAppointmentDate(any(LocalDate.class));
    }

    private Doctor getDoctor(UUID id){
        Doctor doctor = new Doctor();
        doctor.setUsername("test");
        doctor.setId(id);
        return doctor;
    }

    private Appointment getAppointment(UUID docId) {
        Appointment appointment = new Appointment();
        appointment.setDoctor(getDoctor(docId));
        appointment.setAppointmentTime(LocalTime.parse("09:00"));
        appointment.setAppointmentDate(LocalDate.now());
        return appointment;
    }
}