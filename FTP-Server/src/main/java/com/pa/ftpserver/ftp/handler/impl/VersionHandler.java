package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import org.springframework.stereotype.Component;

/**
 * @author pa
 * @date 2021/6/18 00:22
 */
@Component("FTP_HANDLER_SYST")
public class VersionHandler implements Handler {

    @Override
    public String handle(String message, String principal) {
        // TODO: make sure
        return ResponseMessage.VERSION.getMessage();
    }
}
