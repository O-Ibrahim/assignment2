package com.pwc.assignment.configuration;

import com.pwc.assignment.api.ErrorResponse;
import com.pwc.assignment.enums.ErrorCodesEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralError( Exception ex, WebRequest request) {
        return ErrorResponse.builder()
                .errorCode(ErrorCodesEnum.GENERAL_ERROR.getCode())
                .errorMessage(ErrorCodesEnum.GENERAL_ERROR.getMsg())
                .build();
    }
}
