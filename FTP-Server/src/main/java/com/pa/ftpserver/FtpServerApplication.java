package com.pa.ftpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pa
 * @date 2021/5/31 15:35
 */
@SpringBootApplication(scanBasePackages = "com.pa.ftpserver")
public class FtpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtpServerApplication.class, args);
    }

}
