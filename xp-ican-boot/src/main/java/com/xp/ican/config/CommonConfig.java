package com.xp.ican.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nick.ye
 */
@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "xiaopeng.vehicle.common")
public class CommonConfig {


    /**
     * 用于标记不需要将response body转换成CommonResponse的url
     */
    private List<String> bodyExcludeUrl = new ArrayList<>();

}
