package com.pa.ftpserver.ftp.resolver.impl;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.handler.impl.UnsupportedOperationHandler;
import com.pa.ftpserver.ftp.resolver.Resolver;
import com.pa.ftpserver.ftp.util.AuthUtil;
import com.pa.ftpserver.ftp.util.PreAuthorize;
import com.pa.ftpserver.ftp.util.PreCheckMessage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pa
 * @date 2021/5/31 18:09
 */
@Component
public class DefaultResolver implements Resolver {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private UnsupportedOperationHandler unsupportedOperationHandler;

    @Resource
    private AuthUtil authUtil;

    private static final String BEAN_NAME_PREFIX = "FTP_HANDLER_";

    @Override
    public String resolve(String message, String principal) {
        if (message == null || message.length() == 0) {
            return ResponseMessage.BAD_COMMAND.getMessage();
        }

        String handlerName = message;
        int spaceIndex = message.indexOf(" ");
        if (spaceIndex != -1) {
            handlerName = message.substring(0, spaceIndex);
        }
        handlerName = BEAN_NAME_PREFIX + handlerName;

        try {
            Object bean = applicationContext.getBean(handlerName);
            if (bean instanceof Handler) {
                if (bean.getClass().isAnnotationPresent(PreAuthorize.class) && !authUtil.isLogin(principal)) {
                    // not authorize
                    return ResponseMessage.NEED_ACCOUNT.getMessage();
                }
                if (bean.getClass().isAnnotationPresent(PreCheckMessage.class)) {
                    spaceIndex = message.indexOf(' ');
                    if (spaceIndex == -1) {
                        // message is like 'USER'
                        return ResponseMessage.INVALID_PARAM.getMessage();
                    }

                    // get param
                    String param = message.substring(spaceIndex).trim();
                    if (param.isEmpty()) {
                        // message is like 'USER '
                        return ResponseMessage.INVALID_PARAM.getMessage();
                    }
                }

                Handler handler = (Handler) bean;
                return handler.handle(message, principal);
            }
        } catch (BeansException e) {
            // bean do not exist
        }
        return unsupportedOperationHandler.handle(null, null);
    }
}
