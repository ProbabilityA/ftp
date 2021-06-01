package com.pa.ftpserver.net.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author pa
 * @date 2021/5/31 16:05
 */
@ConfigurationProperties(prefix = "net.pool", ignoreUnknownFields = false)
@Data
public class ThreadPoolProperties {

    private Integer coreSize;

    private Integer maximumSize;

    private TimeUnit timeUnit;

    private Integer waitQueueSize;

    private Long keepAliveTime;

    private Integer retryTimes = 3;
}
