package com.vinod.saga.choreography.customer.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Model mapper.
     *
     * @return the model mapper
     */
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
