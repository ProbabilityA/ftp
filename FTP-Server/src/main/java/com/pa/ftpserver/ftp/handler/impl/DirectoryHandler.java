package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.task.DefaultCommunicateTask;
import com.pa.ftpserver.ftp.task.InitiativeDataTransferTask;
import com.pa.ftpserver.ftp.task.consumer.DirectoryConsumer;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import com.pa.ftpserver.net.DataExecutorService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pa
 * @date 2021/6/15 21:50
 */
@Component("FTP_HANDLER_LIST")
@PreAuthorize
public class DirectoryHandler implements Handler {

    private static final Map<String, File> fileMap = new ConcurrentHashMap<>(64);

    @Resource
    private DataExecutorService dataExecutorService;

    @Override
    public String handle(String message, String principal) {
        InitiativeDataTransferTask task = PortHandler.taskMap.get(principal);
        int retry = 3;
        while (task == null && retry-- > 0) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignore) {

            }
            task = PortHandler.taskMap.get(principal);
        }

        if (task == null) {
            return ResponseMessage.CONNECTION_FAILED.getMessage();
        }
        // TODO: !!!!!
        task.setConsumer(new DirectoryConsumer(new File("/Users/Pa/Desktop")));
        WeakReference<DefaultCommunicateTask> weakReference = DefaultCommunicateTask.taskMap.get(principal);
        task.setStartHook((ignore) -> {
            DefaultCommunicateTask communicateTask = weakReference.get();
            if (communicateTask != null) {
                communicateTask.sendMessage(ResponseMessage.OPEN_CHANNEL);
            }
        });
        task.setFinishHook((ignore) -> {
            DefaultCommunicateTask communicateTask = weakReference.get();
            if (communicateTask != null) {
                communicateTask.sendMessage(ResponseMessage.TRANSFER_COMPLETE);
            }
        });
        dataExecutorService.execute(task);
        return null;
    }
}
