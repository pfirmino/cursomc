package com.pfirmino.cursomc.config;

import com.pfirmino.cursomc.services.DBService;
import com.pfirmino.cursomc.services.EmailService;
import com.pfirmino.cursomc.services.MockMailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantiateDataBase() throws Exception {
        dbService.instantiateTestDatabase();
        return true;
    }

    @Bean
    public EmailService emailService() {
        return new MockMailService();
    }
}
