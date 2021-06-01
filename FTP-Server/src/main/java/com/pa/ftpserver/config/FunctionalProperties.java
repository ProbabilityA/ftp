package com.pa.ftpserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author pa
 * @date 2021/6/1 20:54
 */
@ConfigurationProperties(prefix = "net.func")
@Data
public class FunctionalProperties {

    private boolean allowAnonymous = true;
}
