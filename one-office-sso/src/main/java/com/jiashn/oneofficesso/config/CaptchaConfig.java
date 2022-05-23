package com.jiashn.oneofficesso.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 谷歌验证码设置
 * @author jiangjs
 * @date 2022-05-23 20:35
 */
@Configuration
public class CaptchaConfig {

    @Bean(name = "settingDefaultKaptcha")
    public DefaultKaptcha settingDefaultKaptcha(){

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        //是否有边框
        properties.setProperty("kaptcha.border", "yes");
        //边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        //字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "46,139,87");
        //验证码宽度
        properties.setProperty("kaptcha.image.width", "165");
        //验证码高度
        properties.setProperty("kaptcha.image.height", "50");
        //设置保存的session的key
        properties.setProperty("kaptcha.session.key", "code");
        //设置字符的个数
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        //设置字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
