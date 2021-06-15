package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import org.springframework.stereotype.Component;

/**
 * @author pa
 * @date 2021/6/15 16:35
 */
@Component("FTP_HANDLER_OPT")
public class OptionHandler implements Handler {

    @Override
    public String handle(String message, String principal) {
        // Windows FTP may send 'OPT UTF8 ON'
        return ResponseMessage.OKAY.getMessage();
    }
}
