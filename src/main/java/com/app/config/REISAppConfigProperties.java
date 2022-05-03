package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Component
@PropertySource("classpath:application.properties")
public class REISAppConfigProperties {
	@Autowired
	private Environment env;

	public String getConfigValue(String configKey) {
		return env.getProperty(configKey);
	}

//	@Bean
//	public MultipartResolver multipartResolver() {
//	    CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
//	    commonsMultipartResolver.setMaxUploadSize(30000000L);
//	    commonsMultipartResolver.setDefaultEncoding("UTF-8");
//	    return commonsMultipartResolver;
//	}
}
