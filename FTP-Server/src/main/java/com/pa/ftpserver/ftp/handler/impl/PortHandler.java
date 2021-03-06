package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.task.DataTransferTask;
import com.pa.ftpserver.ftp.task.InitiativeDataTransferTask;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import com.pa.ftpserver.ftp.util.PreCheckMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pa
 * @date 2021/6/15 21:22
 */
@Component("FTP_HANDLER_PORT")
@PreAuthorize
@PreCheckMessage
@Slf4j
public class PortHandler implements Handler {

    public static final Map<String, DataTransferTask> taskMap = new ConcurrentHashMap<>(64);
    private static final char POINT = '.';

    @Override
    public String handle(String message, String principal) {
        // e.g. PORT 127,0,0,1,224,22
        String[] address = message.substring(message.indexOf(' ') + 1).split(",");
        if (address.length != 6) {
            return ResponseMessage.INVALID_PARAM.getMessage();
        }
        int port;
        try {
            port = Integer.parseInt(address[4]) * 256 + Integer.parseInt(address[5]);
        } catch (NumberFormatException ignore) {
            return ResponseMessage.INVALID_PARAM.getMessage();
        }
        String host = address[0] +
                POINT +
                address[1] +
                POINT +
                address[2] +
                POINT +
                address[3];
        InitiativeDataTransferTask task;
        try {
            task = new InitiativeDataTransferTask(host, port, principal);
        } catch (IOException e) {
            log.error("IOException occurred when connecting to [{}:{}]", host, port);
            return ResponseMessage.CONNECTION_FAILED.getMessage();
        }
        // successful connect
        taskMap.put(principal, task);
        return ResponseMessage.PORT.getMessage();
    }
}
