package cc.mrbird.febs.common.configure;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedissionConfig {

    @Bean
    public RedissonClient redissonClient() {
        RedissonClient redissonClient;

        Config config = new Config();
        String url = "redis://120.24.81.225:6379";
        config.useSingleServer()
                .setAddress(url)
                .setDatabase(1);

        try {
            redissonClient = Redisson.create(config);
            return redissonClient;
        } catch (Exception e) {
            log.error("RedissonClient init redis url:[{}], Exception:", url, e);
            return null;
        }
    }
}
