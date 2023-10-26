package com.ctsi.ssdc.handle;

import com.ctsi.ssdc.exception.Http404TransportException;
import com.ctsi.ssdc.exception.HttpTransportException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class ExceptionErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        int status=response.status();
        if (status==404){
            return new Http404TransportException("接口调用404异常",response.reason(),methodKey);
        }
        return new HttpTransportException("接口调用异常",response.reason(),methodKey);
    }
}
