package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import org.springframework.stereotype.Component;

/**
 * @author pa
 * @date 2021/5/31 18:11
 */
@Component
public class UnsupportedOperationHandler implements Handler {

    @Override
    public String handle(String message, String principal) {
        return ResponseMessage.NOT_SUPPORTED.getMessage();
    }
}
