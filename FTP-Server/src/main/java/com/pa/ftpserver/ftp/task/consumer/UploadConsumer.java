package com.pa.ftpserver.ftp.task.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * @author pa
 * @date 2021/6/18 21:01
 */
@Slf4j
public class UploadConsumer implements Consumer<Socket> {

    private final File file;

    public UploadConsumer(File file, String fileName) {
        this.file = new File(file, fileName);
    }

    @Override
    public void accept(Socket socket) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            StreamUtils.copy(socket.getInputStream(), out);
        } catch (IOException e) {
            log.error("IOException occurred when uploading file to [{}]", file.getAbsolutePath());
        }
    }
}
