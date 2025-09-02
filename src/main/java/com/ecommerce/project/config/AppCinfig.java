package com.ecommerce.project.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppCinfig {
    @Bean
    public ModelMapper modelMapper(){
        return  new ModelMapper();
    }
}
