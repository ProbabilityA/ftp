package com.pa.ftpserver.ftp.task;

import java.net.Socket;
import java.util.function.Consumer;

/**
 * @author pa
 * @date 2021/6/15 19:26
 */
public interface DataTransferTask extends Runnable {

    void setSocketConsumer(Consumer<Socket> consumer);

    void setStartHook(Consumer<Void> consumer);

    void setFinishHook(Consumer<Void> consumer);
}
