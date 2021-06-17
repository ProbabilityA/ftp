package com.pa.ftpserver.net;

import com.pa.ftpserver.ftp.task.PassiveDataTransferTask;
import com.pa.ftpserver.net.properties.ThreadPoolProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author pa
 * @date 2021/6/17 23:45
 */
@Component
public class PassiveTaskInitializeExecutorService {

    private final ThreadPoolExecutor executor;

    public PassiveTaskInitializeExecutorService(ThreadPoolProperties properties) {
        executor = new ThreadPoolExecutor(1, properties.getMaximumSize(), 0,
                properties.getTimeUnit(), new LinkedBlockingDeque<>(), Executors.defaultThreadFactory());
    }

    public void execute(int port, String principal) {
        executor.execute(() -> new PassiveDataTransferTask(port, principal));
    }
}
