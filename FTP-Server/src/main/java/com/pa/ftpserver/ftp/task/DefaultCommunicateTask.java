package com.pa.ftpserver.ftp.task;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.entity.QuitException;
import com.pa.ftpserver.ftp.resolver.Resolver;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.*;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pa
 * @date 2021/5/31 17:00
 */
@Slf4j
public class DefaultCommunicateTask implements CommunicateTask, AutoCloseable {

    public static final Map<String, WeakReference<DefaultCommunicateTask>> taskMap = new ConcurrentHashMap<>(64);

    private final Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    @Resource
    private Resolver resolver;

    public DefaultCommunicateTask(Socket clientSocket) {
        this.clientSocket = clientSocket;
        taskMap.put(principal(), new WeakReference<>(this));
        // build message reader
        try {
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("IOException occurred when task begin, connection will be closed", e);
        }
    }

    @Override
    public void ping() {

    }

    @Override
    public void sendMessage(ResponseMessage message) {
        doSendMessage(message.getMessage());
    }

    @Override
    public String principal() {
        return clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
    }

    @Override
    public void run() {
        try (this) {
            doSendMessage(ResponseMessage.SERVICE_READY.getMessage());
            boolean run = true;
            while (!clientSocket.isClosed() && run) {
                // read message
                String message = reader.readLine();
                if (message == null) {
                    run = false;
                    continue;
                }
                log.info("receive message from [{}]: {}", principal(), message);

                // resolve and handle message
                String respondMessage = resolver.resolve(message, principal());

                // respond message, null occurs when client send 'LIST' command or others
                if (respondMessage != null) {
                    doSendMessage(respondMessage);
                }
            }
        } catch (IOException e) {
            log.error("IOException occurred when reading message, connection [{}] will be closed", principal());
        } catch (QuitException e) {
            // 'QUIT' command throws
        } catch (Exception e) {
            // AutoCloseable throws
        }
    }

    public void close() throws Exception {
        writer.close();
        reader.close();
        if (!clientSocket.isClosed()) {
            clientSocket.close();
        }
        log.info("[{}] quit peacefully", principal());
    }

    private void doSendMessage(String message) {
        try {
            this.writer.write(message);
            this.writer.flush();
        } catch (IOException e) {
            log.error("IOException occurred when sending message", e);
        }
    }

    public static String getLocalAddress(String principal) {
        WeakReference<DefaultCommunicateTask> weakReference = taskMap.get(principal);
        DefaultCommunicateTask task = Optional.ofNullable(weakReference)
                .map(WeakReference::get)
                .orElse(null);
        if (task == null) {
            return null;
        }
        return task.clientSocket.getLocalAddress().getHostAddress();
    }
}
