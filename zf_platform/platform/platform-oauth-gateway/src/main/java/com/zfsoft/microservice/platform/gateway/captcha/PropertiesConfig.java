package com.zfsoft.microservice.platform.gateway.captcha;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;


@Configuration
public class PropertiesConfig {

    @Resource
    private Environment env;

    @PostConstruct
    public void setProperties() {
        PropertiesUtil.setEnvironment(env);
    }

    @Bean
    public DefaultKaptcha captchaProducer(){
        Properties properties = new Properties();
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty("kaptcha.border.style"))){
            properties.setProperty(Constants.KAPTCHA_BORDER,PropertiesUtil.getProperty("kaptcha.border.style"));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_BORDER_COLOR))){
            properties.setProperty(Constants.KAPTCHA_BORDER_COLOR,PropertiesUtil.getProperty(Constants.KAPTCHA_BORDER_COLOR));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_BORDER_THICKNESS))){
            properties.setProperty(Constants.KAPTCHA_BORDER_THICKNESS,PropertiesUtil.getProperty(Constants.KAPTCHA_BORDER_THICKNESS));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_PRODUCER_IMPL))){
            properties.setProperty(Constants.KAPTCHA_PRODUCER_IMPL,PropertiesUtil.getProperty(Constants.KAPTCHA_PRODUCER_IMPL));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_IMPL))){
            properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_IMPL,PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_IMPL));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING))){
            properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING,PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH))){
            properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES))){
            properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES,PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE))){
            properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE,PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR))){
            properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR,PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE))){
            properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE,PropertiesUtil.getProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_NOISE_IMPL))){
            properties.setProperty(Constants.KAPTCHA_NOISE_IMPL,PropertiesUtil.getProperty(Constants.KAPTCHA_NOISE_IMPL));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_NOISE_COLOR))){
            properties.setProperty(Constants.KAPTCHA_NOISE_COLOR,PropertiesUtil.getProperty(Constants.KAPTCHA_NOISE_COLOR));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL))){
            properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL,PropertiesUtil.getProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_WORDRENDERER_IMPL))){
            properties.setProperty(Constants.KAPTCHA_WORDRENDERER_IMPL,PropertiesUtil.getProperty(Constants.KAPTCHA_WORDRENDERER_IMPL));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_BACKGROUND_IMPL))){
            properties.setProperty(Constants.KAPTCHA_BACKGROUND_IMPL,PropertiesUtil.getProperty(Constants.KAPTCHA_BACKGROUND_IMPL));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM))){
            properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM,PropertiesUtil.getProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO))){
            properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO,PropertiesUtil.getProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_IMAGE_WIDTH))){
            properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH,PropertiesUtil.getProperty(Constants.KAPTCHA_IMAGE_WIDTH));
        }
        if(!StringUtils.isEmpty(PropertiesUtil.getProperty(Constants.KAPTCHA_IMAGE_HEIGHT))){
            properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT,PropertiesUtil.getProperty(Constants.KAPTCHA_IMAGE_HEIGHT));
        }


        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
