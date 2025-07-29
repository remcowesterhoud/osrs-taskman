package com.westerhoud.osrs.taskman.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        ((MappingJackson2HttpMessageConverter) converter)
            .setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.TEXT_PLAIN  // Add support for text/plain
            ));
      }
    }
    return restTemplate;
  }
}
