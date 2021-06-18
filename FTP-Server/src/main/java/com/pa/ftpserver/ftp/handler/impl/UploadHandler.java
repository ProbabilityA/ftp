package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.task.DataTransferTask;
import com.pa.ftpserver.ftp.task.consumer.UploadConsumer;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import com.pa.ftpserver.ftp.util.PreCheckMessage;
import com.pa.ftpserver.net.DataExecutorService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author pa
 * @date 2021/6/18 20:59
 */
@Component("FTP_HANDLER_STOR")
@PreAuthorize
@PreCheckMessage
public class UploadHandler implements Handler {

    @Resource
    private DataExecutorService dataExecutorService;

    @Override
    public String handle(String message, String principal) {
        DataTransferTask task = PortHandler.taskMap.remove(principal);
        if (task == null) {
            return ResponseMessage.CONNECTION_FAILED.getMessage();
        }
        File file = DirectoryHandler.getFile(principal);
        if (file.getFreeSpace() == 0) {
            // no space left on device
            return ResponseMessage.NO_SPACE.getMessage();
        }

        String fileName = new File(message.substring(message.indexOf(' ')).trim()).getName();
        if (new File(file, fileName).exists()) {
            // duplicate file name or invalid file name
            return ResponseMessage.DUPLICATE_FILE_NAME.getMessage();
        }

        task.setSocketConsumer(new UploadConsumer(file, fileName));
        dataExecutorService.execute(task);
        return null;
    }
}
