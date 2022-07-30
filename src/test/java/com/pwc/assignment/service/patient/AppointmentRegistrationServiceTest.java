package com.pwc.assignment.service.patient;

import com.pwc.assignment.domain.model.Appointment;
import com.pwc.assignment.domain.model.Doctor;
import com.pwc.assignment.domain.model.Patient;
import com.pwc.assignment.exception.AppointmentSlotNotFreeException;
import com.pwc.assignment.exception.DoctorNotFoundException;
import com.pwc.assignment.exception.PatientNotFoundException;
import com.pwc.assignment.repository.AppointmentRepository;
import com.pwc.assignment.repository.DoctorRepository;
import com.pwc.assignment.repository.PatientRepository;
import com.pwc.assignment.util.DateTimeUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppointmentRegistrationServiceTest {
    private AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private DoctorRepository doctorRepository = mock(DoctorRepository.class);
    private PatientRepository patientRepository = mock(PatientRepository.class);
    private DateTimeUtil dateTimeUtil = mock(DateTimeUtil.class);
    private AppointmentRegistrationService service = new AppointmentRegistrationService(appointmentRepository, doctorRepository, patientRepository, dateTimeUtil);

    @Test
    public void givenValidInputs_whenRegisteringAppointment_success() throws AppointmentSlotNotFreeException, DoctorNotFoundException, PatientNotFoundException {
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        String patientId = UUID.randomUUID().toString();
        String drId = UUID.randomUUID().toString();
        String desiredDate = date.toString();
        String desiredTime = time.toString();
        when(dateTimeUtil.parseDate(desiredDate)).thenReturn(date);
        when(dateTimeUtil.parseTime(desiredTime)).thenReturn(time);
        when(appointmentRepository.findByDoctorIdAndAppointmentDateAndAppointmentTime(any(UUID.class), any(LocalDate.class), any(LocalTime.class))).thenReturn(null);
        when(doctorRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Doctor()));
        when(patientRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Patient()));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(null);
        service.invoke(patientId, drId, desiredDate, desiredTime);
        verify(dateTimeUtil).parseDate(desiredDate);
        verify(dateTimeUtil).parseTime(desiredTime);
        verify(appointmentRepository).findByDoctorIdAndAppointmentDateAndAppointmentTime(any(UUID.class), any(LocalDate.class), any(LocalTime.class));
        verify(doctorRepository).findById(any(UUID.class));
        verify(patientRepository).findById(any(UUID.class));
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    public void givenDoctorDoesntExist_whenRegistering_throwException() {
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        String patientId = UUID.randomUUID().toString();
        String drId = UUID.randomUUID().toString();
        String desiredDate = date.toString();
        String desiredTime = time.toString();
        when(dateTimeUtil.parseDate(desiredDate)).thenReturn(date);
        when(dateTimeUtil.parseTime(desiredTime)).thenReturn(time);
        when(appointmentRepository.findByDoctorIdAndAppointmentDateAndAppointmentTime(any(UUID.class), any(LocalDate.class), any(LocalTime.class))).thenReturn(null);
        when(doctorRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(DoctorNotFoundException.class , () -> service.invoke(patientId, drId, desiredDate, desiredTime));
        verify(dateTimeUtil).parseDate(desiredDate);
        verify(dateTimeUtil).parseTime(desiredTime);
        verify(appointmentRepository).findByDoctorIdAndAppointmentDateAndAppointmentTime(any(UUID.class), any(LocalDate.class), any(LocalTime.class));
        verify(doctorRepository).findById(any(UUID.class));
    }

    @Test
    public void givenPatientDoesntExist_whenRegistering_throwException() {
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        String patientId = UUID.randomUUID().toString();
        String drId = UUID.randomUUID().toString();
        String desiredDate = date.toString();
        String desiredTime = time.toString();
        when(dateTimeUtil.parseDate(desiredDate)).thenReturn(date);
        when(dateTimeUtil.parseTime(desiredTime)).thenReturn(time);
        when(appointmentRepository.findByDoctorIdAndAppointmentDateAndAppointmentTime(any(UUID.class), any(LocalDate.class), any(LocalTime.class))).thenReturn(null);
        when(doctorRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Doctor()));
        when(patientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class , () -> service.invoke(patientId, drId, desiredDate, desiredTime));
        verify(dateTimeUtil).parseDate(desiredDate);
        verify(dateTimeUtil).parseTime(desiredTime);
        verify(appointmentRepository).findByDoctorIdAndAppointmentDateAndAppointmentTime(any(UUID.class), any(LocalDate.class), any(LocalTime.class));
        verify(doctorRepository).findById(any(UUID.class));
        verify(patientRepository).findById(any(UUID.class));
    }

    @Test
    public void givenExtriesToAnExistingAppointment_whenRegistering_throwException() {
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        String patientId = UUID.randomUUID().toString();
        String drId = UUID.randomUUID().toString();
        String desiredDate = date.toString();
        String desiredTime = time.toString();
        when(dateTimeUtil.parseDate(desiredDate)).thenReturn(date);
        when(dateTimeUtil.parseTime(desiredTime)).thenReturn(time);
        when(appointmentRepository.findByDoctorIdAndAppointmentDateAndAppointmentTime(any(UUID.class), any(LocalDate.class), any(LocalTime.class))).thenReturn(new Appointment());
        assertThrows(AppointmentSlotNotFreeException.class , () -> service.invoke(patientId, drId, desiredDate, desiredTime));
        verify(dateTimeUtil).parseDate(desiredDate);
        verify(dateTimeUtil).parseTime(desiredTime);
        verify(appointmentRepository).findByDoctorIdAndAppointmentDateAndAppointmentTime(any(UUID.class), any(LocalDate.class), any(LocalTime.class));
    }
}