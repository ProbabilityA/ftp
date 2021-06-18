package com.pa.ftpserver.net;

import com.pa.ftpserver.net.properties.ServerPortProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pa
 * @date 2021/6/16 20:30
 */
@Component
public class PortHelper {

    private final ServerPortProperties serverPortProperties;

    private final AtomicInteger nextPort;

    public PortHelper(ServerPortProperties serverPortProperties) {
        this.serverPortProperties = serverPortProperties;
        nextPort = new AtomicInteger(serverPortProperties.getTransferPortBegin());
    }

    public int nextPort() {
        int next;
        do {
            next = nextPort.getAndIncrement();
            if (next > serverPortProperties.getTransferPortEnd()) {
                nextPort.set(serverPortProperties.getTransferPortBegin() + 1);
                next = 0;
            }
        } while (next == 0 || !checkPort(next));
        return next;
    }

    public boolean checkPort(int port) {
        try (Socket ignore = new Socket("127.0.0.1", port)) {
            return false;
        } catch (IOException ignore) {
            return true;
        }
    }
}
