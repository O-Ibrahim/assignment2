package com.pwc.assignment.service.patient;

import com.pwc.assignment.domain.model.Appointment;
import com.pwc.assignment.domain.model.Doctor;
import com.pwc.assignment.repository.AppointmentRepository;
import com.pwc.assignment.repository.DoctorRepository;
import com.pwc.assignment.util.DateTimeUtil;
import com.pwc.assignment.util.EnvValueUtil;

import java.util.*;

public class AvailableDoctorsService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final DateTimeUtil dateTimeUtil;
    private final EnvValueUtil envValueUtil;



    public AvailableDoctorsService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, DateTimeUtil dateTimeUtil, EnvValueUtil envValueUtil) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.dateTimeUtil = dateTimeUtil;
        this.envValueUtil = envValueUtil;
    }

    public List<DoctorAppointments> getList(String desiredDate) {
        List<Doctor> doctorIds = doctorRepository.findAll();
        List<Appointment> appointments = appointmentRepository.findByAppointmentDate(dateTimeUtil.parseDate(desiredDate));
        List<DoctorAppointments> doctorAppointmentsList = new ArrayList<>();

        doctorIds.forEach(
                d -> {
                    List<String> availableHours = getAvailableHours(d.getId(), appointments);
                    doctorAppointmentsList.add(
                            DoctorAppointments.builder()
                                    .availableHours(availableHours)
                                    .doctorName(d.getName())
                                    .build()
                    );
                }
        );
        return doctorAppointmentsList;
    }


    private List<String> getAvailableHours(UUID doctorId, List<Appointment> appointments) {
        List<String> availableHours = new ArrayList<>(envValueUtil.getAvailableHours());
        List<String> drAppointments = new ArrayList<>();
        populateDrAppointments(doctorId, appointments, drAppointments);
        removeReservedHours(availableHours, drAppointments);
        return availableHours;
    }

    private void populateDrAppointments(UUID doctorId, List<Appointment> appointments, List<String> drAppointments) {
        appointments.stream().filter(appointment -> appointment.getDoctor().getId() == doctorId).forEach(
                appointment -> {
                    drAppointments.add(appointment.getAppointmentTime().toString());
                }
        );
    }

    private void removeReservedHours(List<String> availableHours, List<String> drAppointments) {
        for(String drAppointment: drAppointments){
            availableHours.removeIf(drAppointment::equalsIgnoreCase);
        }
    }
}
