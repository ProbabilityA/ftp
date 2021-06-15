package com.pa.ftpserver.ftp.task;

import com.pa.ftpserver.ftp.constant.ResponseMessage;

/**
 * @author pa
 * @date 2021/5/31 16:49
 */
public interface CommunicateTask extends Runnable {

    /**
     * Checkout whether the Client is still Alive
     */
    void ping();

    /**
     * Send the request
     *
     * @param message message to be sent
     */
    void sendMessage(ResponseMessage message);

    /**
     * Get principal of the task
     *
     * @return principal
     */
    String principal();
}
