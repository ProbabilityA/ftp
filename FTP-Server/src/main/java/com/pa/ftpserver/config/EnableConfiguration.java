package com.pa.ftpserver.config;

import com.pa.ftpserver.net.properties.ServerPortProperties;
import com.pa.ftpserver.net.properties.ThreadPoolProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author pa
 * @date 2021/5/31 16:07
 */
@Configuration
@EnableConfigurationProperties({ThreadPoolProperties.class, ServerPortProperties.class, FunctionalProperties.class})
@EnableScheduling
public class EnableConfiguration {
}
