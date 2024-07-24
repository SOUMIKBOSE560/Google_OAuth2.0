package com.soumik.OAuth2.Controller;

import com.soumik.OAuth2.Service.GoogleChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/v2/googlechat")
public class GoogleChatController {

    @Autowired
    private GoogleChatService googleChatService;

    @GetMapping("/listspaces")
    public ResponseEntity<String> listSpaces(@RequestParam("accessToken") String accessToken) {
        return googleChatService.getSpaces(accessToken);
    }
}
