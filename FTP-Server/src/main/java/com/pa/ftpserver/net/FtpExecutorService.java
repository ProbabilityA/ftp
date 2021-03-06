package com.pa.ftpserver.net;

import com.pa.ftpserver.ftp.constant.ResponseMessage;
import com.pa.ftpserver.ftp.task.CommunicateTask;
import com.pa.ftpserver.net.properties.ThreadPoolProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author pa
 * @date 2021/5/31 16:01
 */
@Component
@Slf4j
public class FtpExecutorService {

    private final ThreadPoolExecutor executor;

    public FtpExecutorService(ThreadPoolProperties properties) {
        executor = new ThreadPoolExecutor(properties.getCoreSize(), properties.getMaximumSize(),
                properties.getKeepAliveTime(), properties.getTimeUnit(),
                new ArrayBlockingQueue<>(properties.getWaitQueueSize()), Executors.defaultThreadFactory(),
                (runnable, executor) -> {
                    if (runnable instanceof CommunicateTask) {
                        CommunicateTask task = (CommunicateTask) runnable;
                        task.sendMessage(ResponseMessage.TOO_MANY_USERS);
                        log.warn("Communicate task rejected, principal is {}", task.principal());
                    } else {
                        log.warn("Unknown Task rejected");
                    }
                });
    }

    public <T extends CommunicateTask> void execute(T task) {
        executor.execute(task);
    }
}
