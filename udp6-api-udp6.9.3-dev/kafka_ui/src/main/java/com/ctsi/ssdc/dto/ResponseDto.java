package com.ctsi.ssdc.dto;

import lombok.Data;

/**
 * The code change the world !!!
 *
 * @author guochui
 * @create 2022-01-05 11:27
 **/

@Data
public class ResponseDto {
    boolean success;
    String message;
    Object data;

    public static ResponseDto fail(String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(false);
        responseDto.setMessage(message);
        return responseDto;
    }

    public static ResponseDto success(String message, Object data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setData(data);
        responseDto.setMessage(message);
        return responseDto;
    }

    public static ResponseDto success(Object data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setData(data);
        return responseDto;
    }

    public static ResponseDto success() {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        return responseDto;
    }
}

