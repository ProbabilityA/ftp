package com.pa.ftpserver.ftp;

import com.pa.ftpserver.ftp.resolver.Resolver;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author pa
 * @date 2021/5/31 17:00
 */
@Slf4j
public class DefaultCommunicateTask implements CommunicateTask, AutoCloseable {

    private final Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    @Resource
    private Resolver resolver;

    public DefaultCommunicateTask(Socket clientSocket) {
        this.clientSocket = clientSocket;

        // build message reader
        try {
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("IOException occurred when task begin, connection will be closed", e);
        } catch (Exception ignore) {
            // AutoCloseable::close throws
        }
        doSendMessage("Connection established");
    }

    @Override
    public void ping() {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String principal() {
        return clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
    }

    @Override
    public void run() {
        try {
            doSendMessage("220 Service ready for new user.\r\n");
            while (!clientSocket.isClosed()) {
                // read message
                String message = reader.readLine();
                log.info("receive message from [{}]: {}", principal(), message);
                if (message.startsWith("OPTS")) {
                    doSendMessage("211 Okay.\r\n");
                    continue;
                }

                // resolve and handle message
                String respondMessage = resolver.resolve(message, principal());

                // respond message
                doSendMessage(respondMessage);
            }
        } catch(IOException e) {
            log.error("IOException occurred when reading message, connection will be closed", e);
        }
    }

    public void close() throws Exception {
        clientSocket.close();
    }

    private void doSendMessage(String message) {
        try {
            this.writer.write(message);
            this.writer.flush();
        } catch (IOException e) {
            log.error("IOException occurred when sending message", e);
        }
    }
}
