package com.pa.ftpserver.ftp.resolver;

/**
 * @author pa
 * @date 2021/5/31 18:05
 */
public interface Resolver {

    /**
     * Resolve message, find a specific handler to handler the request
     *
     * @param message   the message to be resolved
     * @param principal the principal who sent the message
     * @return the data return from the {@link com.pa.ftpserver.ftp.handler.Handler} or failed message
     */
    String resolve(String message, String principal);
}
