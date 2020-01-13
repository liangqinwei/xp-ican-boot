package com.xp.ican.config.systemConfig;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Tomcat的高版本（具体从哪个版本开始没有具体了解）中增加了一个新特性，就是严格按照 RFC 3986规范进行访问解析，而 RFC 3986规范规定Url中只允许包含英文字母（a-zA-Z）、数字（0-9）、-_.~4个特殊字符以及所有保留字符(RFC3986中指定了以下字符为保留字符：! * ’ ( ) ; : @ & = + $ , / ? # [ ])。
//        那解决办法有两种，转义和配置Tomcat
//        ————————————————
//        版权声明：本文为CSDN博主「fang1344」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
//        原文链接：https://blog.csdn.net/fxz1535567862/article/details/88224242

@Configuration
public class TomcatConfig {

    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        fa.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return fa;
    }
}
