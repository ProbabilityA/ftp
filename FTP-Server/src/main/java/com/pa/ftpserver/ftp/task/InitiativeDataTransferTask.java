package com.pa.ftpserver.ftp.task;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * @author pa
 * @date 2021/6/15 19:28
 */
@Slf4j
public class InitiativeDataTransferTask implements DataTransferTask, AutoCloseable {

    private Socket socket;

    @Setter
    private Consumer<Socket> consumer;

    @Setter
    private Consumer<Void> startHook;

    @Setter
    private Consumer<Void> finishHook;

    public InitiativeDataTransferTask(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            log.error("IOException occurred when connecting to [{}:{}]", host, port);
        }
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
