package com.pa.ftpserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author pa
 * @date 2021/6/1 20:54
 */
@ConfigurationProperties(prefix = "net.func")
@Data
public class FunctionalProperties {

    private boolean allowAnonymous = true;

    private String basePath = "";

    @PostConstruct
    public void checkPath() {
        File baseFile = new File(basePath).getAbsoluteFile();
        if (!baseFile.exists() || !baseFile.isDirectory()) {
            throw new IllegalArgumentException("Base path not exist or isn't directory!");
        }
        this.basePath = baseFile.getAbsolutePath();
    }
}
