package edu.config;

import edu.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        /**的意思是所有文件夹及里面的子文件夹
//         /*是所有文件夹，不含子文件夹
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //扩展消息转换器，为其设置对象映射器JacksonObjectMapper，并将它放于转换器容器中的第一个
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,messageConverter);
    }
}
/*
classpath：是指编译后的classes文件的路径（看target下的生成项）
classpath*：不仅包含classes路径，还包括jar文件中(classes路径)进行查找
再解释一下classes含义：
1. 存放各种资源配置文件 eg.init.properties log4j.properties struts.xml
2. 存放模板文件 eg.actionerror.ftl
3. 存放class文件 对应的是项目开发时的src目录编译文件
*/