package com.pwc.assignment.configuration;

import com.pwc.assignment.repository.AppointmentRepository;
import com.pwc.assignment.repository.DoctorRepository;
import com.pwc.assignment.repository.PatientRepository;
import com.pwc.assignment.service.doctor.CancelAppointmentService;
import com.pwc.assignment.service.doctor.DoctorRegistrationService;
import com.pwc.assignment.service.doctor.PatientsListService;
import com.pwc.assignment.service.patient.AppointmentRegistrationService;
import com.pwc.assignment.service.patient.AvailableDoctorsService;
import com.pwc.assignment.service.patient.PatientRegistrationService;
import com.pwc.assignment.service.security.CustomUserDetailsService;
import com.pwc.assignment.util.DateTimeUtil;
import com.pwc.assignment.util.EnvValueUtil;
import com.pwc.assignment.util.JwtUtil;
import com.pwc.assignment.util.ResponseUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public ResponseUtil responseUtil(){
        return new ResponseUtil();
    }

    @Bean
    public EnvValueUtil envValueUtil() {
        return new EnvValueUtil();
    }

    @Bean
    public DateTimeUtil dateTimeUtil(EnvValueUtil envValueUtil) {
        return new DateTimeUtil(envValueUtil);
    }

    @Bean
    public DoctorRegistrationService doctorRegistrationService(
            DoctorRepository doctorRepository,
            PasswordEncoder passwordEncoder
    ) {
        return new DoctorRegistrationService(doctorRepository, passwordEncoder);
    }

    @Bean
    public PatientsListService patientsListService(AppointmentRepository appointmentRepository) {
        return new PatientsListService(appointmentRepository);
    }

    @Bean
    public PatientRegistrationService patientRegistrationService(
            PatientRepository patientRepository,
            PasswordEncoder passwordEncoder
    ) {
        return new PatientRegistrationService(patientRepository, passwordEncoder);
    }

    @Bean
    public CancelAppointmentService cancelAppointmentService(AppointmentRepository appointmentRepository) {
        return new CancelAppointmentService(appointmentRepository);
    }

    @Bean
    public AvailableDoctorsService availableDoctorsService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, DateTimeUtil dateTimeUtil, EnvValueUtil envValueUtil){
        return new AvailableDoctorsService(doctorRepository, appointmentRepository, dateTimeUtil, envValueUtil);
    }

    @Bean
    public AppointmentRegistrationService appointmentRegistrationService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, DateTimeUtil dateTimeUtil){
        return new AppointmentRegistrationService(appointmentRepository, doctorRepository, patientRepository, dateTimeUtil);
    }
}
