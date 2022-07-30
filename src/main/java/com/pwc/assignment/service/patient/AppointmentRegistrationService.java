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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AppointmentRegistrationService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DateTimeUtil dateTimeUtil;

    public AppointmentRegistrationService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, DateTimeUtil dateTimeUtil) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.dateTimeUtil = dateTimeUtil;
    }


    public void invoke(String patientId, String drId, String desiredDate, String desiredTime) throws AppointmentSlotNotFreeException, DoctorNotFoundException, PatientNotFoundException {
        LocalTime parsedTime = getParsedTime(desiredTime);
        LocalDate parsedDate = getParsedDate(desiredDate);
        UUID patientUUID = UUID.fromString(patientId);
        UUID drUUID = UUID.fromString(drId);
        Appointment appointmentInDb = appointmentRepository.findByDoctorIdAndAppointmentDateAndAppointmentTime(drUUID, parsedDate, parsedTime);
        if (Objects.nonNull(appointmentInDb)) {
            throw new AppointmentSlotNotFreeException();
        }
        Optional<Doctor> optionalDoctor = doctorRepository.findById(drUUID);
        if (optionalDoctor.isEmpty()) {
            throw new DoctorNotFoundException();
        }
        Optional<Patient> optionalPatient = patientRepository.findById(patientUUID);
        if (optionalPatient.isEmpty()) {
            throw new PatientNotFoundException();
        }

        Appointment appointment = getAppointment(parsedTime, parsedDate, optionalDoctor, optionalPatient);
        appointmentRepository.save(appointment);

    }

    private Appointment getAppointment(LocalTime parsedTime, LocalDate parsedDate, Optional<Doctor> optionalDoctor, Optional<Patient> optionalPatient) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(parsedTime);
        appointment.setDoctor(optionalDoctor.get());
        appointment.setPatient(optionalPatient.get());
        appointment.setAppointmentDate(parsedDate);
        return appointment;
    }

    private LocalDate getParsedDate(String desiredDate) {
        return dateTimeUtil.parseDate(desiredDate);
    }

    private LocalTime getParsedTime(String desiredTime) {
        return dateTimeUtil.parseTime(desiredTime);
    }
}
