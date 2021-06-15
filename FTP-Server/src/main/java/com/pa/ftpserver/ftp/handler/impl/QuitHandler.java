package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.entity.QuitException;
import com.pa.ftpserver.ftp.handler.Handler;
import org.springframework.stereotype.Component;

/**
 * @author pa
 * @date 2021/6/15 17:32
 */
@Component("FTP_HANDLER_QUIT")
public class QuitHandler implements Handler {

    @Override
    public String handle(String message, String principal) {
        throw new QuitException();
    }
}
