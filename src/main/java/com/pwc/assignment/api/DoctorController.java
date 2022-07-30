package com.pwc.assignment.api;

import com.pwc.assignment.domain.dto.auth.AuthenticationRequest;
import com.pwc.assignment.domain.dto.auth.AuthenticationResponse;
import com.pwc.assignment.domain.dto.doctor.CancelAppointmentRequestDto;
import com.pwc.assignment.domain.dto.doctor.ListAppointmentsResponse;
import com.pwc.assignment.domain.dto.doctor.ListAppointmentsResponsePatients;
import com.pwc.assignment.domain.dto.doctor.RegisterDoctorRequestDto;
import com.pwc.assignment.domain.mapper.DoctorRegisterRequestToDomainMapper;
import com.pwc.assignment.domain.model.Appointment;
import com.pwc.assignment.enums.ErrorCodesEnum;
import com.pwc.assignment.enums.RolesEnum;
import com.pwc.assignment.exception.UsernameAlreadyUsedException;
import com.pwc.assignment.service.doctor.CancelAppointmentService;
import com.pwc.assignment.service.doctor.DoctorRegistrationService;
import com.pwc.assignment.service.doctor.PatientsListService;
import com.pwc.assignment.util.DateTimeUtil;
import com.pwc.assignment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api/v1/doctor/")
public class DoctorController {
    @Autowired
    DoctorRegistrationService doctorRegistrationService;
    @Autowired
    DoctorRegisterRequestToDomainMapper doctorRegisterRequestToDomainMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PatientsListService patientsListService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CancelAppointmentService cancelAppointmentService;
    @Autowired
    private DateTimeUtil dateTimeUtil;

    @PostMapping("register")
    public ResponseEntity<Response> register(@Valid @RequestBody RegisterDoctorRequestDto requestDto) {
        try {
            doctorRegistrationService.invoke(doctorRegisterRequestToDomainMapper.toDomain(requestDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(Response.builder().build());
        } catch (UsernameAlreadyUsedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder()
                    .errorCode(ErrorCodesEnum.USERNAME_ALREADY_EXISTS.getCode())
                    .errorMessage(ErrorCodesEnum.USERNAME_ALREADY_EXISTS.getMsg())
                    .build());
        }
    }

    @PostMapping("login")
    public ResponseEntity<Response> login(@Valid @RequestBody AuthenticationRequest request) {
        try {
            UserDetails userDetails = getUserDetails(request);
            if (userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equalsIgnoreCase(RolesEnum.DOCTOR.name())).collect(Collectors.toList()).size() == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        Response.builder().errorCode(ErrorCodesEnum.INVALID_ROLE.getCode())
                                .errorMessage(ErrorCodesEnum.INVALID_ROLE.getMsg()).build()
                );
            }
            String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(Response.builder()
                    .payload(new AuthenticationResponse(jwt))
                    .build());

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Response.builder().errorCode(ErrorCodesEnum.INVALID_CREDENTIALS.getCode())
                            .errorMessage(ErrorCodesEnum.INVALID_CREDENTIALS.getMsg()).build()
            );
        }
    }

    @RolesAllowed("DOCTOR")
    @GetMapping("patient-list")
    public ResponseEntity<Object> listAppointments(@RequestParam String date, @RequestParam String doctorId) {
        LocalDate localDate = dateTimeUtil.parseDate(date);
        List<Appointment> appointmentList = patientsListService.invoke(doctorId, localDate);
        if(appointmentList.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ErrorResponse.builder()
                            .errorCode(ErrorCodesEnum.NO_APPOINTMENTS_FOR_TODAY.getCode())
                            .errorMessage(ErrorCodesEnum.NO_APPOINTMENTS_FOR_TODAY.getMsg() + " "+ date)
                            .build()
            );
        }
        return ResponseEntity.ok(buildListAppointment(localDate, appointmentList));

    }

    @RolesAllowed("DOCTOR")
    @DeleteMapping("cancel-appointment")
    public ResponseEntity<Response> cancelAppointment(@Valid @RequestBody CancelAppointmentRequestDto request) {
        cancelAppointmentService.invoke(UUID.fromString(request.getDoctorId()), UUID.fromString(request.getAppointmentId()));
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder().errorCode(null)
                        .errorMessage(null)
                        .payload(null)
                        .build()
        );
    }


    private UserDetails getUserDetails(AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(), authenticationRequest.getPassword()
                )
        );
        return userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    }

    private ListAppointmentsResponse buildListAppointment(LocalDate localDate, List<Appointment> appointments) {
        return ListAppointmentsResponse.builder()
                .date(dateTimeUtil.formatDateString(localDate))
                .patientList(buildListAppointmentsResponseAppointmentList(appointments))
                .build();
    }

    private List<ListAppointmentsResponsePatients> buildListAppointmentsResponseAppointmentList(List<Appointment> appointments) {
        List<ListAppointmentsResponsePatients> list = new ArrayList<>();
        appointments.forEach(a -> list.add(ListAppointmentsResponsePatients.builder()
                .appointmentId(a.getId().toString())
                .patientName(a.getPatient().getName())
                .build())
        );
        return list;
    }

}
