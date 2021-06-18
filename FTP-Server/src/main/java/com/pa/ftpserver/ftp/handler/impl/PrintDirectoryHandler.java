package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.config.FunctionalProperties;
import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @author pa
 * @date 2021/6/18 21:38
 */
@Component("FTP_HANDLER_PWD")
public class PrintDirectoryHandler implements Handler {

    @Resource
    private FunctionalProperties functionalProperties;

    @Override
    public String handle(String message, String principal) {
        File file = DirectoryHandler.getFile(principal);
        try {
            // canonical path doesn't end with '/'
            String path = file.getCanonicalPath() + "/";
            path = path.substring(functionalProperties.getBasePath().length());
            return String.format(ResponseMessage.WORKING_DIRECTORY.getMessage(), path);
        } catch (IOException e) {
            // back to default directory when exception occurred
            DirectoryHandler.setFile(principal, new File(functionalProperties.getBasePath()));
            return String.format(ResponseMessage.WORKING_DIRECTORY.getMessage(), "/");
        }
    }
}
