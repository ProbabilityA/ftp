package com.pa.ftpserver.net.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author pa
 * @date 2021/5/31 16:21
 */
@ConfigurationProperties(prefix = "net.port", ignoreUnknownFields = false)
@Data
public class ServerPortProperties {

    private Integer serverPort;

    private Integer transferPortBegin;
    private Integer transferPortEnd;
}
