package com.piesat.schedule.rpc.lock;

import com.piesat.common.utils.OwnException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-18 17:45
 **/
@Slf4j
@Component
public class RedisLock {
    public static final String LOCK_PREFIX = "lock:";
    public static final int LOCK_EXPIRE = 60000; // ms
    public static final int SEDN_LOCK_EXPIRE=1000*60*5;

    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    public boolean lock(String key){
        String lock = LOCK_PREFIX + key;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());


            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));

                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    public boolean tryLock(String key){
        boolean result=false;
        try {
            result = this.lock(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public boolean trySendLock(String key){
        boolean result=false;
        try {
            result = this.sendLock(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public boolean sendLock(String key){
        String lock = LOCK_PREFIX + key;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + SEDN_LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());


            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));

                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + SEDN_LOCK_EXPIRE + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 删除锁
     *
     * @param key
     */
    public void delete(String key) {
        try {
            redisTemplate.delete(LOCK_PREFIX+key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

