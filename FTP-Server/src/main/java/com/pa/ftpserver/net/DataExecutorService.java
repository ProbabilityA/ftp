package com.pa.ftpserver.net;

import com.pa.ftpserver.ftp.task.DataTransferTask;
import com.pa.ftpserver.net.properties.ThreadPoolProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author pa
 * @date 2021/6/15 21:08
 */
@Component
@Slf4j
public class DataExecutorService {

    private final ThreadPoolExecutor executor;

    public DataExecutorService(ThreadPoolProperties properties) {
        executor = new ThreadPoolExecutor(properties.getCoreSize(), properties.getMaximumSize(), 0,
                properties.getTimeUnit(), new LinkedBlockingDeque<>(), Executors.defaultThreadFactory());
    }

    public <T extends DataTransferTask> void execute(T task) {
        executor.execute(task);
    }
}
