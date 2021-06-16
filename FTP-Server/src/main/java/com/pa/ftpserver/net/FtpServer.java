package com.pa.ftpserver.net;

import com.pa.ftpserver.ftp.task.CommunicateTask;
import com.pa.ftpserver.ftp.task.DefaultCommunicateTask;
import com.pa.ftpserver.net.properties.ServerPortProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author pa
 * @date 2021/5/31 18:43
 */
@Component
@Slf4j
public class FtpServer {

    private final ServerSocket server;

    @Resource
    private FtpExecutorService executorService;
    @Resource
    private ApplicationContext applicationContext;

    public FtpServer(ServerPortProperties properties) throws IOException {
        this.server = new ServerSocket(properties.getServerPort());
        log.info("Socket server initialize with port {}", properties.getServerPort());
        new Thread(() -> {
            while (true) {
                try {
                    // block to wait for new connection
                    Socket client = server.accept();
                    log.info("Client socket [{}:{}] connected", client.getInetAddress().getHostAddress(), client.getPort());
                    // create runnable task
                    CommunicateTask task = new DefaultCommunicateTask(client);
                    // autowire resolver
                    applicationContext.getAutowireCapableBeanFactory().autowireBean(task);
                    // async execute task
                    executorService.execute(task);
                } catch (IOException e) {
                    log.error("IOException occurred when waiting for a connection", e);
                }
            }
        }, "ftp-server-thread").start();
    }
}
