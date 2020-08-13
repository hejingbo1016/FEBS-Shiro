package cc.mrbird.febs.common.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.setting.dialect.Props;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class ConfigUtil {

    private static final Properties pro = new Properties();
    private static Map<String, Props> propsMap = new HashMap<String, Props>();

    /**
     * 获取key 对应的value
     * @param propertyName
     * @return
     */
    public static String getProperty(String propertyName) {
        return getProperty("app", propertyName);
    }

    public static String getProperty(String propertyFile, String propertyName) {
        Props props = propsMap.get(propertyFile);
        if (props == null) {
            try {
                String fileName= propertyFile + ".properties";
                props = new Props(fileName);
				// props.autoLoad(true);
                propsMap.put(fileName, props);
            }
            catch (Exception e) {
                String errorMsg = propertyFile + ".properties 文件读取失败!";
                log.error(errorMsg + e);
                throw new RuntimeException(errorMsg);
            }
        }

        return props.getProperty(propertyName);
    }
}
