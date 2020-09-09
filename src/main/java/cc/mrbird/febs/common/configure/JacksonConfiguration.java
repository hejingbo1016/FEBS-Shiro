package cc.mrbird.febs.common.configure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Slf4j
@Order(2)
@Configuration
public class JacksonConfiguration {

    @Bean
    @ConditionalOnClass(ObjectMapper.class)
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        log.debug("Jackson全局设置 --> 序列化 --> 关闭空数据异常");
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        log.debug("Jackson全局设置 --> 反序列化 --> 关闭未知属性抛出异常");
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        return objectMapper;

    }

    @Bean
    @ConditionalOnClass(ObjectMapper.class)
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();

        jsonConverter.setObjectMapper(objectMapper);

        return jsonConverter;

    }

}
