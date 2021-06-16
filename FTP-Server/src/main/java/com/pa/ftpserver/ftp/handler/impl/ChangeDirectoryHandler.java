package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.config.FunctionalProperties;
import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

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
        if (".".equals(target)) {
            return ResponseMessage.CWD_SUCCESS.getMessage();
        }

        File file = DirectoryHandler.getFile(principal);
        if ("..".equals(target)) {
            // back to parent directory
            if (!functionalProperties.getBasePath().equals(file.getAbsolutePath())) {
                File parentFile = file.getAbsoluteFile().getParentFile();
                DirectoryHandler.setFile(principal, parentFile);
            }
            return ResponseMessage.CWD_SUCCESS.getMessage();
        } else {
            // go to child directory
            file = new File(file, target);
            if (file.exists() && file.isDirectory()) {
                DirectoryHandler.setFile(principal, file);
                return ResponseMessage.CWD_SUCCESS.getMessage();
            } else {
                return ResponseMessage.DIRECTORY_NOT_FOUND.getMessage();
            }
        }
    }
}
