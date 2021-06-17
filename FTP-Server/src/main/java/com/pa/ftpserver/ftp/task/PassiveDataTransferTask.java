package com.pa.ftpserver.ftp.task;

import com.pa.ftpserver.ftp.handler.impl.PortHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @author pa
 * @date 2021/6/17 22:45
 */
@Slf4j
public class PassiveDataTransferTask extends AbstractDataTransferTask implements AutoCloseable {

    private ServerSocket server;
    private Socket client;

    public PassiveDataTransferTask(int port, String principal) {
        super(principal);
        try {
            this.server = new ServerSocket(port);
            // set connection time out
            this.server.setSoTimeout(10000);
            this.client = server.accept();
            PortHandler.taskMap.put(principal, this);
        } catch (SocketTimeoutException e) {
            this.server = null;
            this.client = null;
            log.warn("Timeout has been reached, server socket with port [{}], principal is [{}]", port, principal);
        } catch (IOException e) {
            this.server = null;
            this.client = null;
            log.error("IOException occurred when creating server socket with port [{}], target principal is [{}]", port, principal);
        }
    }

    @Override
    public void run() {
        if (client == null) {
            return;
        }

        startHook.accept(null);
        try (this) {
            consumer.accept(client);
            finishHook.accept(null);
        } catch (Exception ignore) {
            // AutoCloseable throws
        }
    }

    @Override
    public void close() throws Exception {
        if (client != null && !client.isClosed()) {
            client.close();
        }
        if (server != null && !server.isClosed()) {
            server.close();
        }
    }
}
