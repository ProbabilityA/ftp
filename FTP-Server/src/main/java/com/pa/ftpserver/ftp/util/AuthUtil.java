package com.pa.ftpserver.ftp.util;

import com.pa.ftpserver.ftp.dao.DataStore;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pa
 * @date 2021/6/15 16:07
 */
@Component
public class AuthUtil {

    private static final String USER = "USER";

    private static final String USERNAME = "USERNAME";

    public static final String LOGIN_STATUS = "LOGIN_STATUS:NEED_PASSWORD";

    @Resource
    private DataStore dataStore;

    public void setUsername(String principal, String username) {
        dataStore.put(String.join(":", USERNAME, principal), username);
    }

    public boolean isLogin(String principal) {
        return get(principal) != null;
    }

    public String get(String principal) {
        return dataStore.get(String.join(":", USERNAME, principal));
    }

    public void setLoginStatus(String principal, String username) {
        dataStore.put(String.join(":", LOGIN_STATUS, principal), username);
    }

    public String getLoginStatus(String principal) {
        return dataStore.get(String.join(":", LOGIN_STATUS, principal));
    }

    public boolean validatePassword(String principal, String password) {
        String username = getLoginStatus(principal);
        if (username == null) {
            return false;
        }
        String storedPassword = dataStore.get(String.join(":", USER, username));
        if (storedPassword == null) {
            return false;
        }
        boolean success = storedPassword.equals(password);
        if (success) {
            setUsername(principal, username);
        }
        return success;
    }
}
