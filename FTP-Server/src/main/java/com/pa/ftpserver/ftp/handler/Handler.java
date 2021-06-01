package com.pa.ftpserver.ftp.handler;

/**
 * @author pa
 * @date 2021/5/31 18:10
 */
public interface Handler {

    /**
     * Split the message and handle it
     *
     * @param message   the message to be handle
     * @param principal the principal who sent the message
     * @return handled result
     */
    String handle(String message, String principal);
}
