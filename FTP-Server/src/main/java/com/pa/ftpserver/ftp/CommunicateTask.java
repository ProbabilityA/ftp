package com.pa.ftpserver.ftp;

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
    void sendMessage(String message);

    /**
     * Get principal of the task
     *
     * @return principal
     */
    String principal();
}
