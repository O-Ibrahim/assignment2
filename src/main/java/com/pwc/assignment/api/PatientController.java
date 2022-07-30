package com.pwc.assignment.api;

import com.pwc.assignment.domain.dto.auth.AuthenticationRequest;
import com.pwc.assignment.domain.dto.auth.AuthenticationResponse;
import com.pwc.assignment.domain.dto.patient.RegisterAppointmentRequestDto;
import com.pwc.assignment.domain.dto.patient.RegisterAppointmentResponseDto;
import com.pwc.assignment.domain.dto.patient.RegisterPatientRequestDto;
import com.pwc.assignment.domain.mapper.PatientRegisterRequestToDomainMapper;
import com.pwc.assignment.enums.ErrorCodesEnum;
import com.pwc.assignment.enums.RolesEnum;
import com.pwc.assignment.exception.AppointmentSlotNotFreeException;
import com.pwc.assignment.exception.DoctorNotFoundException;
import com.pwc.assignment.exception.PatientNotFoundException;
import com.pwc.assignment.exception.UsernameAlreadyUsedException;
import com.pwc.assignment.service.patient.AppointmentRegistrationService;
import com.pwc.assignment.service.patient.AvailableDoctorsService;
import com.pwc.assignment.service.patient.DoctorAppointments;
import com.pwc.assignment.service.patient.PatientRegistrationService;
import com.pwc.assignment.util.JwtUtil;
import com.pwc.assignment.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api/v1/patient/")
public class PatientController {
    @Autowired
    PatientRegistrationService patientRegistrationService;
    @Autowired
    PatientRegisterRequestToDomainMapper patientRegisterRequestToDomainMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ResponseUtil responseUtil;
    @Autowired
    AvailableDoctorsService availableDoctorsService;

    @Autowired
    AppointmentRegistrationService appointmentRegistrationService;

    @PostMapping("register-appointment")
    public ResponseEntity<Object> registerAppointment(@Valid @RequestBody RegisterAppointmentRequestDto requestDto) {
        try {
            appointmentRegistrationService.invoke(requestDto.getPatientId(), requestDto.getDrId(), requestDto.getDesiredDate(), requestDto.getDesiredTime());
            return ResponseEntity.ok().body(RegisterAppointmentResponseDto.builder()
                            .desiredTime(requestDto.getDesiredTime())
                            .desiredDate(requestDto.getDesiredDate())
                            .patientId(requestDto.getPatientId())
                            .drId(requestDto.getDrId())
                            .scheduled(true)
                    .build());
        } catch (AppointmentSlotNotFreeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder()
                    .errorCode(ErrorCodesEnum.APPOINTMENT_SLOT_NOT_FREE.getCode())
                    .errorMessage(ErrorCodesEnum.APPOINTMENT_SLOT_NOT_FREE.getMsg())
                    .build());
        } catch (DoctorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder()
                    .errorCode(ErrorCodesEnum.DOCTOR_NOT_FOUND.getCode())
                    .errorMessage(ErrorCodesEnum.DOCTOR_NOT_FOUND.getMsg())
                    .build());
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder()
                    .errorCode(ErrorCodesEnum.PATIENT_NOT_FOUND.getCode())
                    .errorMessage(ErrorCodesEnum.PATIENT_NOT_FOUND.getMsg())
                    .build());
        }

    }


    @GetMapping("available-doctors")
    public ResponseEntity<Object> seeAvailableDoctors(@RequestParam UUID patientId, @RequestParam String desiredDate) {
        List<DoctorAppointments> list = availableDoctorsService.getList(desiredDate);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("register")
    public ResponseEntity<Response> register(@Valid @RequestBody RegisterPatientRequestDto requestDto) {
        try {
            patientRegistrationService.registerPatientService(patientRegisterRequestToDomainMapper.toDomain(requestDto));
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
            if (userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equalsIgnoreCase(RolesEnum.PATIENT.name())).collect(Collectors.toList()).size() == 0) {
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


    private UserDetails getUserDetails(AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(), authenticationRequest.getPassword()
                )
        );
        return userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    }

}
