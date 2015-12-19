/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.lucene.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zxc
 */
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan("com.mob")
public class WebAppConfig extends WebMvcConfigurerAdapter {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebAppConfig.class);
    }

    // @Bean
    // public HttpMessageConverters customConverters() {
    // return new HttpMessageConverters(new UTF8StringHttpMessageConverter());
    // }

    // @Bean
    // public InternalResourceViewResolver internalResourceViewResolver() {
    // InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    // viewResolver.setPrefix("/templates");
    // viewResolver.setSuffix(".jsp");
    // return viewResolver;
    // }
}
