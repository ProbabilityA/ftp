package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.config.FunctionalProperties;
import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.util.AuthUtil;
import com.pa.ftpserver.ftp.util.PreCheckMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pa
 * @date 2021/5/31 18:11
 */
@Component("FTP_HANDLER_USER")
@PreCheckMessage
public class LoginHandler implements Handler {

    private static final String ANONYMOUS = "anonymous";

    @Resource
    private AuthUtil authUtil;

    @Resource
    private FunctionalProperties functionalProperties;

    @Override
    public String handle(String message, String principal) {
        // get username
        String username = message.substring(message.indexOf(' ')).trim();

        // anonymous login
        if (ANONYMOUS.equals(username)) {
            if (functionalProperties.isAllowAnonymous()) {
                authUtil.setUsername(principal, ANONYMOUS);
                return ResponseMessage.LOGIN_PROCEED.getMessage();
            }
            return ResponseMessage.NEED_ACCOUNT.getMessage();
        }

        // common login
        authUtil.setLoginStatus(principal, username);
        return ResponseMessage.NEED_PASSWORD.getMessage();
    }
}
