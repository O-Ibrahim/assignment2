package com.pwc.assignment.util;

import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

public class EnvValueUtil {

    @Value("${appointment.hours}")
    String availableHoursString;

    @Value("${date.regex}")
    private String datePattern;

    @Value("${time.regex}")
    private String timePattern;

    public List<String> getAvailableHours(){
       return Arrays.asList(availableHoursString.split(","));
    }
    public String getDatePattern(){
        return  datePattern;
    }
    public String getTimePattern(){
        return timePattern;
    }
}
