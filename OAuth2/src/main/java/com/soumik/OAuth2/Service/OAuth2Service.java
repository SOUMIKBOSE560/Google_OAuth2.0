package com.soumik.OAuth2.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.soumik.OAuth2.Model.GoogleJsonCredentials;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.io.IOException;
import java.util.List;

@Service
public class OAuth2Service {

    @Value("${client_secret}")
    private String CLIENT_SECRET;

    @Autowired
    private GoogleJsonCredentials googleJsonCredentials;

    public void authorize(HttpServletResponse response) throws IOException {
        String redirectUri = googleJsonCredentials.getRedirect_uris().get(0);
        String clientId = googleJsonCredentials.getClient_id();
        String authorizationEndpoint = "https://accounts.google.com/o/oauth2/v2/auth";
        String scopes = "https://www.googleapis.com/auth/chat.spaces.readonly https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/drive.readonly";

        // add access_type = offline to get refresh token
        String url = authorizationEndpoint + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code&scope=" + scopes + "&access_type=offline";
        response.sendRedirect(url);
    }


    public UserCredentials loadApplicationCredential() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(CLIENT_SECRET).getInputStream());
        if (googleCredentials instanceof UserCredentials) {
            return (UserCredentials) googleCredentials;
        } else {
            return UserCredentials.newBuilder().setClientId("NULL")
                    .setClientSecret("NULL")
                    .setAccessToken(null)
                    .build();
        }
    }


    // Create authorization URL
    public String createAccessLink() {
        String authorizationUrl = new GoogleAuthorizationCodeRequestUrl(googleJsonCredentials.getClient_id(),
                googleJsonCredentials.getRedirect_uris().get(0), Arrays.asList("https://www.googleapis.com/auth/userinfo.profile"))
                .setAccessType("offline")
                .build();

        return authorizationUrl;
    }


    //Get access and refresh token behalf of the user
    public String getAccessAndRefreshToken(String authorizationCode){
        try {
            // Exchange the authorization code for access and refresh tokens
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new JacksonFactory(),
                    googleJsonCredentials.getClient_id(),
                    googleJsonCredentials.getClient_secret(),
                    authorizationCode,
                    googleJsonCredentials.getRedirect_uris().get(0))
                    .execute();

            String accessToken = tokenResponse.getAccessToken();
            String refreshToken = tokenResponse.getRefreshToken();

            return "Access Token: " + accessToken + "<br>Refresh Token: " + refreshToken;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


    public String newAccessToken(String refreshToken) throws IOException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        NetHttpTransport transport = new NetHttpTransport();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(googleJsonCredentials.getClient_id(), googleJsonCredentials.getClient_secret())
                .addRefreshListener(new CredentialRefreshListener() {
                    @Override
                    public void onTokenResponse(Credential credential, TokenResponse tokenResponse) {
                        System.out.println("Access token refreshed & scopes are: " + tokenResponse.getScope());
                        // Optionally handle new refresh token if provided
                        String newRefreshToken = tokenResponse.getRefreshToken();
                        System.out.println("New Refresh Token: " + newRefreshToken);

                    }

                    @Override
                    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) {
                        System.err.println("Token refresh error: " + tokenErrorResponse.getError());
                    }
                })
                .build()
                .setRefreshToken(refreshToken);

        // Force a token refresh
        credential.refreshToken();

        String newAccessToken = credential.getAccessToken();
        System.out.println("New Access Token: " + newAccessToken);
        return newAccessToken;
    }

}
