package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.task.DataTransferTask;
import com.pa.ftpserver.ftp.task.consumer.DownloadConsumer;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import com.pa.ftpserver.ftp.util.PreCheckMessage;
import com.pa.ftpserver.net.DataExecutorService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author pa
 * @date 2021/6/16 00:35
 */
@Component("FTP_HANDLER_RETR")
@PreAuthorize
@PreCheckMessage
public class DownloadHandler implements Handler {

    @Resource
    private DataExecutorService dataExecutorService;

    @Override
    public String handle(String message, String principal) {
        DataTransferTask task = PortHandler.taskMap.remove(principal);
        if (task == null) {
            return ResponseMessage.CONNECTION_FAILED.getMessage();
        }
        File file = DirectoryHandler.getFile(principal);
        file = new File(file, message.substring(message.indexOf(' ') + 1));
        if (!file.exists() && !file.isFile()) {
            return ResponseMessage.FILE_NOT_FOUND.getMessage();
        }
        task.setSocketConsumer(new DownloadConsumer(file));
        dataExecutorService.execute(task);
        return null;
    }
}
