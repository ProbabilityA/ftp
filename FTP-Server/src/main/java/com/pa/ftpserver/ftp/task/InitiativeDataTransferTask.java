package com.pa.ftpserver.ftp.task;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * @author pa
 * @date 2021/6/15 19:28
 */
@Slf4j
public class InitiativeDataTransferTask implements DataTransferTask, AutoCloseable {

    private final Socket socket;

    @Setter
    private Consumer<Socket> consumer;

    @Setter
    private Consumer<Void> startHook;

    @Setter
    private Consumer<Void> finishHook;

    public InitiativeDataTransferTask(String host, int port, String principal) throws IOException {
        socket = new Socket(host, port);
        WeakReference<DefaultCommunicateTask> weakReference = DefaultCommunicateTask.taskMap.get(principal);
        this.startHook = (ignore) -> {
            DefaultCommunicateTask communicateTask = weakReference.get();
            if (communicateTask != null) {
                communicateTask.sendMessage(ResponseMessage.OPEN_CHANNEL);
            }
        };
        this.finishHook = (ignore) -> {
            DefaultCommunicateTask communicateTask = weakReference.get();
            if (communicateTask != null) {
                communicateTask.sendMessage(ResponseMessage.TRANSFER_COMPLETE);
            }
        };
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
