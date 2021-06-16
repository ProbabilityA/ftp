package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.config.FunctionalProperties;
import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.task.InitiativeDataTransferTask;
import com.pa.ftpserver.ftp.task.consumer.DirectoryConsumer;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import com.pa.ftpserver.net.DataExecutorService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
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

    private static String basePath = "";

    public DirectoryHandler(FunctionalProperties functionalProperties) {
        basePath = functionalProperties.getBasePath();
    }

    @Override
    public String handle(String message, String principal) {
        InitiativeDataTransferTask task = PortHandler.taskMap.get(principal);
        if (task == null) {
            return ResponseMessage.CONNECTION_FAILED.getMessage();
        }
        File file = getFile(principal);
        task.setConsumer(new DirectoryConsumer(file));
        dataExecutorService.execute(task);
        return null;
    }

    public static File getFile(String principal) {
        return fileMap.computeIfAbsent(principal, (ignore) -> new File(basePath));
    }

    public static void setFile(String principal, File file) {
        fileMap.put(principal, file);
    }
}
