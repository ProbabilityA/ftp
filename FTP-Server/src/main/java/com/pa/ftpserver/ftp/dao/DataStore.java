package com.pa.ftpserver.ftp.dao;

/**
 * @author pa
 * @date 2021/6/1 19:10
 */
public interface DataStore {

    void put(String key, String value);

    String get(String key);
}
