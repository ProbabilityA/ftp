package com.pa.ftpserver.ftp.task.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * @author pa
 * @date 2021/6/16 00:41
 */
@Slf4j
public class DownloadConsumer implements Consumer<Socket> {

    private final File file;

    public DownloadConsumer(File file) {
        this.file = file;
    }

    @Override
    public void accept(Socket socket) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            StreamUtils.copy(fileInputStream, socket.getOutputStream());
        } catch (FileNotFoundException ignore) {
            // ignore, small probability event
        } catch (IOException e) {
            log.error("IOException occurred when transfer file to [{}:{}]", socket.getInetAddress().getHostAddress(), socket.getPort());
        }
    }
}
