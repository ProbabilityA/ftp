package com.pa.ftpserver.ftp.task.consumer;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author pa
 * @date 2021/6/15 19:57
 */
@Slf4j
public class DirectoryConsumer implements Consumer<Socket> {

    private final File file;

    private static final String SPLIT_SEQUENCE = "  ";
    private static final String SMALL_SPLIT_SEQUENCE = " ";
    private static final String COLON = ":";

    public DirectoryConsumer(File file) {
        this.file = file;
    }

    @Override
    public void accept(Socket socket) {
        try (final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII))) {
            if (!file.exists()) {
                return;
            }
            StringBuilder builder = new StringBuilder(512);
            Path[] paths = Files.list(file.toPath()).toArray(Path[]::new);
            for (Path path : paths) {
                PosixFileAttributes fileAttributes = Files.readAttributes(path, PosixFileAttributes.class);
                Set<PosixFilePermission> filePermissions = fileAttributes.permissions();
                LocalDateTime localDate = fileAttributes.creationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                // e.g. -rw-r--r--  1 pa  staff      209660  6 12 22:08 1623506929480.jpg
                builder
                        // file permission
                        .append(PosixFilePermissions.toString(filePermissions))
                        .append(SPLIT_SEQUENCE)
                        // file owner
                        .append(fileAttributes.owner().getName())
                        .append(SPLIT_SEQUENCE)
                        .append(fileAttributes.group().getName())
                        .append(SPLIT_SEQUENCE)
                        // file size
                        .append(fileAttributes.size())
                        .append(SPLIT_SEQUENCE)
                        // create time
                        .append(localDate.getDayOfMonth())
                        .append(SMALL_SPLIT_SEQUENCE)
                        .append(localDate.getMonthValue())
                        .append(SMALL_SPLIT_SEQUENCE)
                        .append(localDate.getHour())
                        .append(COLON)
                        .append(localDate.getSecond())
                        .append(SPLIT_SEQUENCE)
                        .append(path.getFileName())
                        .append("\n");
            }
            if (builder.length() != 0) {
                writer.write(builder.toString());
            }
        } catch (IOException e) {
            log.error("IOException occurred when transfer list files information to [{}:{}]", socket.getInetAddress().getHostAddress(), socket.getPort());
        }
    }
}
