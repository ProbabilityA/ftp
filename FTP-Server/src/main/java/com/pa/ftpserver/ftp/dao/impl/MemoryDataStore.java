package com.pa.ftpserver.ftp.dao.impl;

import com.pa.ftpserver.ftp.dao.DataStore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pa
 * @date 2021/6/1 19:12
 */
@Slf4j
@Component
public class MemoryDataStore implements DataStore {

    private final Map<String, WrappedValue> map = new ConcurrentHashMap<>(128) {{
        WrappedValue wrappedValue = new WrappedValue();
        wrappedValue.setValue("123456");
        put("USER:pa", wrappedValue);
        put("USER:chenbohan", wrappedValue);
    }};

    @Override
    public void put(String key, String value) {
        WrappedValue wrappedValue = new WrappedValue();
        wrappedValue.setExpireTime(LocalDateTime.now().plusHours(1L));
        wrappedValue.setValue(value);
        map.put(key, wrappedValue);
    }

    @Override
    public String get(String key) {
        WrappedValue wrappedValue = map.get(key);
        if (wrappedValue == null) {
            return null;
        }

        if (wrappedValue.getExpireTime() == null || wrappedValue.getExpireTime().isAfter(LocalDateTime.now())) {
            return wrappedValue.getValue();
        }

        // value expired
        map.remove(key);
        return null;
    }

    @Scheduled(cron = "0 30 * * * ?")
    public void removeEntry() {
        log.info("start to clear expired value...");
        LocalDateTime now = LocalDateTime.now();
        map.entrySet().removeIf(entry -> entry.getValue().getExpireTime() != null && entry.getValue().getExpireTime().isBefore(now));
    }

    @Data
    private static final class WrappedValue {
        private String value;
        private LocalDateTime expireTime;
    }
}
