package com.pa.ftpserver.ftp.task;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

/**
 * @author pa
 * @date 2021/6/15 19:28
 */
@Slf4j
public class InitiativeDataTransferTask extends AbstractDataTransferTask implements AutoCloseable {

    private final Socket socket;

    public InitiativeDataTransferTask(String host, int port, String principal) throws IOException {
        super(principal);
        socket = new Socket(host, port);
    }

    @Override
    public void run() {
        startHook.accept(null);
        try (this) {
            consumer.accept(socket);
            finishHook.accept(null);
        } catch (Exception ignored) {
            // AutoCloseable throws
        }
    }

    @Override
    public void close() throws Exception {
        if (!socket.isClosed()) {
            socket.close();
        }
    }
}
