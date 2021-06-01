package com.pa.ftpserver.ftp.handler.impl;

import com.pa.ftpserver.config.FunctionalProperties;
import com.pa.ftpserver.ftp.constant.AuthorizationConstant;
import com.pa.ftpserver.ftp.dao.DataStore;
import com.pa.ftpserver.ftp.handler.Handler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author pa
 * @date 2021/5/31 18:11
 */
@Component("FTP_HANDLER_USER")
public class LoginHandler implements Handler {

    private static final String ANONYMOUS = "anonymous";

    private static final String HANDLER_NAME = "USER";

    private static final String LOGIN_STATUS = "LOGIN_STATUS_";

    @Resource
    private DataStore dataStore;

    @Resource
    private FunctionalProperties functionalProperties;

    @Override
    public String handle(String message, String principal) {
        AuthorizationConstant authorizationConstant;
        String status = dataStore.get(LOGIN_STATUS + principal);
        if (status == null) {
            authorizationConstant = AuthorizationConstant.NOT_LOGIN;
        } else {
            authorizationConstant = AuthorizationConstant.valueOf(status);
        }
        switch (authorizationConstant) {
            case NOT_LOGIN:
                // expect USER [username]
                String username = message.substring(message.indexOf(' ')).trim();
                // anonymous login
                if (ANONYMOUS.equals(username)) {
                    if (functionalProperties.isAllowAnonymous()) {
                        setLoginStatus(principal, AuthorizationConstant.SUCCESS);
                        return "230 User logged in, proceed.\r\n";
                    }
                    return "332 Need account for login.\r\n";
                }

                // common login

                break;
            case INPUTTING_PASSWORD:

                break;
            case SUCCESS:

                break;
            default:
                // won't happen
        }
        // USER 123 123
        String[] words = message.split(" ");
        System.out.println(Arrays.toString(words));
        return UUID.randomUUID().toString();
    }

    private void setLoginStatus(String principal, AuthorizationConstant constant) {
        dataStore.put(principal, constant.name());
    }
}
