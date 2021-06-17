package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.task.DefaultCommunicateTask;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import com.pa.ftpserver.net.PassiveTaskInitializeExecutorService;
import com.pa.ftpserver.net.PortHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author pa
 * @date 2021/6/17 23:08
 */
@Component("FTP_HANDLER_PASV")
@PreAuthorize
public class PassiveHandler implements Handler {

    @Resource
    private PortHelper portHelper;

    @Resource
    private PassiveTaskInitializeExecutorService passiveTaskInitializeExecutorService;

    @Override
    public String handle(String message, String principal) {
        int port = portHelper.nextPort();
        passiveTaskInitializeExecutorService.execute(port, principal);
        // get address which user connect to
        String localAddress = Optional.ofNullable(DefaultCommunicateTask.getLocalAddress(principal))
                .orElse("127,0,0,1")
                .replace('.', ',');
        return String.format(ResponseMessage.PASSIVE_MODE.getMessage(), localAddress, port / 256, port % 256);
    }
}
