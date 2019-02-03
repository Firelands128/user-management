/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.config;

import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FormattingConversionService mvcConversionService() {
        FormattingConversionService f = super.mvcConversionService();
        f.addConverter(new OAuthTypeConverter());
        return f;
    }

    class OAuthTypeConverter implements Converter<String, OAuthType> {
        @Override
        public OAuthType convert(String type) {
            try {
                return OAuthType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                logger.warn("OAuth type {} invalid.", type);
                throw new MyException(ErrorCode.BAD_REQUEST, "OAuth type " + type + " invalid.");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
