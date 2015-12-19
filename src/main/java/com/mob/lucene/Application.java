/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.lucene;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mob.lucene.config.WebAppConfig;

/**
 * 启动web服务
 * 
 * @author zxc
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WebAppConfig.class);
        app.setShowBanner(false);
        app.run(args);
    }
}
