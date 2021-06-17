package com.pa.ftpserver.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author pa
 * @date 2021/6/16 20:30
 */
public class PortHelper {

    public static boolean checkPort(int port) {
        try {
            // TODO: only work under linux with lsof
            final Process exec = Runtime.getRuntime().exec("lsof -i:" + port);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            if (reader.readLine() == null) {
                return true;
            }
        } catch (IOException ignore) {
        }
        return false;
    }
}
