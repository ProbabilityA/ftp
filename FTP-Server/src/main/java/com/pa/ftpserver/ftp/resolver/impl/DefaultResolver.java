package com.pa.ftpserver.ftp.resolver.impl;

import com.pa.ftpserver.ftp.dao.DataStore;
import com.pa.ftpserver.ftp.handler.Handler;
import com.pa.ftpserver.ftp.handler.impl.UnsupportedOperationHandler;
import com.pa.ftpserver.ftp.resolver.Resolver;
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
    private DataStore dataStore;

    private static final String BEAN_NAME_PREFIX = "FTP_HANDLER_";

    private static final String LAST_HANDLER = "LAST_HANDLER_";

    @Override
    public String resolve(String message, String principal) {
        if (message == null) {
            return "Empty message";
        }

        // check if last request is finished by last handler
        String handlerName = dataStore.get(LAST_HANDLER + principal);
        if (handlerName == null) {
            int spaceIndex = message.indexOf(" ");
            if (message.length() == 0 || spaceIndex == -1) {
                return "Invalid message";
            }
            handlerName = BEAN_NAME_PREFIX + message.substring(0, spaceIndex);
        }

        try {
            Object bean = applicationContext.getBean(handlerName);
            if (bean instanceof Handler) {
                Handler handler = (Handler) bean;
                return handler.handle(message, principal);
            }
        } catch (BeansException e) {
            // bean do not exist
        }
        return unsupportedOperationHandler.handle(null, null);
    }
}
