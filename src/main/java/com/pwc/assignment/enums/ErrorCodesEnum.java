package com.pwc.assignment.enums;

public enum ErrorCodesEnum {

    INVALID_CREDENTIALS("1", "Invalid Credentials"),
    INVALID_ROLE("2", "Invalid Role"),
    USERNAME_ALREADY_EXISTS("3", "Username Taken"),
    APPOINTMENT_SLOT_NOT_FREE("4", "Appointment Slot Not Free"),
    PATIENT_NOT_FOUND("5", "Patient Not Found"),
    DOCTOR_NOT_FOUND("6", "Doctor Not Found"),
    GENERAL_ERROR("500","Unknown error has occured"),
    JWT("401","JWT error"),
    NO_APPOINTMENTS_FOR_TODAY("401", "NO SCHEDUELED APPOINTMENTS FOR TODAY");


    private final String code;
    private final String msg;

    ErrorCodesEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}