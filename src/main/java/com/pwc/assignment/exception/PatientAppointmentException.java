package com.pwc.assignment.exception;


public class PatientAppointmentException extends Exception{
    public PatientAppointmentException() {
        super();
    }

    public PatientAppointmentException(String message) {
        super(message);
    }

    public PatientAppointmentException(String message, Throwable cause) {
        super(message, cause);
    }
}