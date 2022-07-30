package com.pwc.assignment.util;

import com.pwc.assignment.api.Response;
import com.pwc.assignment.enums.ErrorCodesEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public ResponseEntity<Response> getResponse(ErrorCodesEnum errorCode){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder()
                .errorCode(errorCode.getCode())
                .errorMessage(errorCode.getMsg())
                .build());
    }
}
