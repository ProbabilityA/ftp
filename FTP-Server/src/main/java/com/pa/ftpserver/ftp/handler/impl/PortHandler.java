package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.task.InitiativeDataTransferTask;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import com.pa.ftpserver.ftp.util.PreCheckMessage;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pa
 * @date 2021/6/15 21:22
 */
@Component("FTP_HANDLER_PORT")
@PreAuthorize
@PreCheckMessage
public class PortHandler implements Handler {

    public static final Map<String, InitiativeDataTransferTask> taskMap = new ConcurrentHashMap<>(64);
    private static final char POINT = '.';

    @Override
    public String handle(String message, String principal) {
        // e.g. PORT 127,0,0,1,224,22
        String[] address = message.substring(message.indexOf(' ')).split(",");
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
        InitiativeDataTransferTask task = new InitiativeDataTransferTask(host, port);
        taskMap.put(principal, task);
        return ResponseMessage.PORT.getMessage();
    }
}
