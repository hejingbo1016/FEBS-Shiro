package cc.mrbird.febs.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<Serializable, Object> operations;

    /**
     * 写入key value设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit){
        boolean result = false;
        try {
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            delete(key);
        }
        return result;
    }

    /**
     * 写入key value设置时效时间 并返回旧值
     * @param key
     * @param value
     * @param expireTime
     * @param timeUnit
     * @return
     */
    public Object getset(final String key, Object value, Long expireTime, TimeUnit timeUnit){
        Object result = null;
        try {
            result = operations.getAndSet(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
            delete(key);
            result = null;
        }
        return result;
    }

    public boolean delete(final String key){
        boolean result = false;
        try {
            if (isKeyExists(key)){
                redisTemplate.delete(key);
            }
            result = true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入key value
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value){
        boolean result = false;
        try {
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取
     *
     * @param key
     * @return
     */
    public Object get(final String key){
        Object result;
        result = operations.get(key);
        return result;
    }



    public boolean isKeyExists(String key){
        return redisTemplate.hasKey(key);
    }


    /**
     * 清空redis
     *
     * @param key
     */
    public void empty(String key){
        if (isKeyExists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 设置超时时间秒
     *
     * @param key
     * @param expireTime
     */
    public void setExpireSenconds(String key, Long expireTime){
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public Boolean remove(final String key){
        if (exists(key)) {
            return redisTemplate.delete(key);
        }
        return false;
    }

    /**
     * 判断是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key){
        return redisTemplate.hasKey(key);
    }
}
