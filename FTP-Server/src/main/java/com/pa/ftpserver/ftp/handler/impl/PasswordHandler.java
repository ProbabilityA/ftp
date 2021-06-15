package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.util.AuthUtil;
import com.pa.ftpserver.ftp.util.PreCheckMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pa
 * @date 2021/6/15 16:46
 */
@Component("FTP_HANDLER_PASS")
@PreCheckMessage
public class PasswordHandler implements Handler {

    @Resource
    private AuthUtil authUtil;

    @Override
    public String handle(String message, String principal) {
        String username = authUtil.getLoginStatus(principal);
        if (username == null) {
            return ResponseMessage.NEED_ACCOUNT.getMessage();
        }

        String password = message.substring(message.indexOf(' ')).trim();
        if (authUtil.validatePassword(principal, password)) {
            return ResponseMessage.LOGIN_PROCEED.getMessage();
        }
        return ResponseMessage.LOGIN_FAILED.getMessage();
    }
}
