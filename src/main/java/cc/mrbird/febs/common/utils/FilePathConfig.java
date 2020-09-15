package cc.mrbird.febs.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilePathConfig {

    @Value("${minio.basePath}")
    private String basePath;

    public String getBasePath() {
        return basePath;
    }


}
