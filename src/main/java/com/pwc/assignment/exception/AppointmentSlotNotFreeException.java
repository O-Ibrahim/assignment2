package com.pwc.assignment.exception;


public class AppointmentSlotNotFreeException extends Exception{
    public AppointmentSlotNotFreeException() {
        super();
    }

    public AppointmentSlotNotFreeException(String message) {
        super(message);
    }

    public AppointmentSlotNotFreeException(String message, Throwable cause) {
        super(message, cause);
    }
}