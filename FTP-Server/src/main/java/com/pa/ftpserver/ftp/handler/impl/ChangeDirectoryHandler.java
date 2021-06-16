package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.config.FunctionalProperties;
import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @author pa
 * @date 2021/6/16 14:46
 */
@Component("FTP_HANDLER_CWD")
@PreAuthorize
public class ChangeDirectoryHandler implements Handler {

    @Resource
    private FunctionalProperties functionalProperties;

    @Override
    public String handle(String message, String principal) {
        int spaceIndex = message.indexOf(' ');
        if (spaceIndex == -1) {
            return ResponseMessage.CWD_SUCCESS.getMessage();
        }
        String target = message.substring(spaceIndex).trim();

        File file = DirectoryHandler.getFile(principal);
        File targetFile = new File(file, target);
        // directory not exist
        if (!targetFile.exists() || !targetFile.isDirectory()) {
            return ResponseMessage.DIRECTORY_NOT_FOUND.getMessage();
        }

        String path;
        try {
            path = targetFile.getCanonicalPath();
        } catch (IOException e) {
            // force it back to base directory
            path = "";
        }

        // check path
        if (!path.startsWith(functionalProperties.getBasePath())) {
            targetFile = new File(functionalProperties.getBasePath());
        }
        DirectoryHandler.setFile(principal, targetFile);
        return ResponseMessage.CWD_SUCCESS.getMessage();
    }
}
