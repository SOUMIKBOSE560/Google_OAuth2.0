package com.soumik.OAuth2.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soumik.OAuth2.Model.GoogleJsonCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Configuration
public class CredentialConfig {

    @Value("classpath:credential.json")
    private Resource googleCredentialsResource;

    @Bean
    public GoogleJsonCredentials googleJsonCredentials() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(googleCredentialsResource.getInputStream(), GoogleJsonCredentials.class);
    }

}
