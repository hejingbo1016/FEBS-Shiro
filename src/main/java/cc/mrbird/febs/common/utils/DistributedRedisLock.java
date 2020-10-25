package cc.mrbird.febs.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author eetal
 * @create 2020/8/27
 */
@Slf4j
@Component
public class DistributedRedisLock {

    @Autowired
    private RedissonClient redissonClient;

    // 加锁
    public Boolean lock(String lockName) {
        try {
            if (redissonClient == null) {
                log.debug("DistributedRedisLock redissonClient is null");
                return false;
            }
            RLock lock = redissonClient.getLock(lockName);
            // 锁10秒后自动释放，防止死锁
            lock.lock(10, TimeUnit.SECONDS);

            log.debug("Thread [{}] DistributedRedisLock lock [{}] success", Thread.currentThread().getName(), lockName);
            // 加锁成功
            return true;
        } catch (Exception e) {
            log.error("DistributedRedisLock lock [{}] Exception:", lockName, e);
            return false;
        }
    }

    // 释放锁
    public Boolean unlock(String lockName) {
        try {
            if (redissonClient == null) {
                log.debug("DistributedRedisLock redissonClient is null");
                return false;
            }

            RLock lock = redissonClient.getLock(lockName);
            lock.unlock();
            log.debug("Thread [{}] DistributedRedisLock unlock [{}] success", Thread.currentThread().getName(), lockName);
            // 释放锁成功
            return true;
        } catch (Exception e) {
            log.error("DistributedRedisLock unlock [{}] Exception:", lockName, e);
            return false;
        }
    }
}
