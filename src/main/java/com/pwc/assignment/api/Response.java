package com.pwc.assignment.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String errorCode;
    private String errorMessage;
    private Object payload;
}
