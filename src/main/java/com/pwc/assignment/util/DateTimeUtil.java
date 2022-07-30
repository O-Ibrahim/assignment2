package com.pwc.assignment.util;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {


    private final EnvValueUtil envValueUtil;

    public DateTimeUtil(EnvValueUtil envValueUtil) {
        this.envValueUtil = envValueUtil;
    }

    public LocalDate parseDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(envValueUtil.getDatePattern()));
    }

    public LocalTime parseTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(envValueUtil.getTimePattern()));
    }

    public String formatDateString(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(envValueUtil.getDatePattern()));
    }

    public String formatTimeString(LocalTime time){
        return time.format(DateTimeFormatter.ofPattern(envValueUtil.getTimePattern()));
    }

}
