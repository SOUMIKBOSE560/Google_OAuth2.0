package com.soumik.OAuth2.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.chat.v1.HangoutsChat;
import com.google.api.services.chat.v1.model.ListSpacesResponse;
import com.google.api.services.chat.v1.model.Space;
import com.google.auth.http.HttpCredentialsAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleChatService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String APPLICATION_NAME = "Project-Use-GoogleChatAPI";

    public Object createNewChatSpace(String token) throws GeneralSecurityException, IOException {
        String accessToken = "ya29.a0AXooCgvC4UZcA5SbwLhp63aFzyjPL_DnawOKqX8gOs2XHog-KtxWcjHsjmGwdjKXswhkoKslXC4P0oguN9GYldamYOh2KNHCxF3c6SPdAszJynxJy74vkT42Pd733vbMVGAaWVixg-IgBuGYeywsPbdT06WOZEkOqV5-aCgYKAQYSARASFQHGX2MiGFGfTpZ-ukQhQXVpPrXlLA0171";

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/chat.bot"));

        HangoutsChat chatService = new HangoutsChat.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName(APPLICATION_NAME).build();

        ListSpacesResponse response = chatService.spaces().list().execute();
        for (Space space : response.getSpaces()) {
            System.out.println("Space: " + space.getName() + " - " + space.getDisplayName());
        }

        return response;
    }


    public ResponseEntity<String> getSpaces(String accessToken) {
        String apiEndpoint = "https://chat.googleapis.com/v1/spaces";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiEndpoint, HttpMethod.GET, entity, String.class);
        return response;
    }
}