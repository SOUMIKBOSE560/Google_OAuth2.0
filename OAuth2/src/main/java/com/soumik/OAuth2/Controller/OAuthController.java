package com.soumik.OAuth2.Controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.UserCredentials;
import com.soumik.OAuth2.Model.GoogleJsonCredentials;
import com.soumik.OAuth2.Service.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    @Autowired
    private OAuth2Service oAuth2Service;

    @Autowired
    private GoogleJsonCredentials googleCredentials;

    @GetMapping("/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        oAuth2Service.authorize(response);
    }

    @GetMapping("/appcredential")
    public GoogleJsonCredentials googleJsonCredentials(){
        return googleCredentials;
    }

    @GetMapping("/generatenewaccesstoken")
    public String generateNewAccessToken(@RequestParam(name = "refreshToken") String refreshToken) throws IOException {
       return oAuth2Service.newAccessToken(refreshToken);
    }

    @GetMapping("/loadappcredential")
    public UserCredentials loadAppCredential() throws IOException {
        return oAuth2Service.loadApplicationCredential();
    }

    @GetMapping("/generateLink")
    public String generateLink(){
        return oAuth2Service.createAccessLink();
    }

    //Callback function to obtain refresh and access token
    @GetMapping("/authsuccess")
    public String handleOAuthRedirect(@RequestParam("code") String authorizationCode) {
        return oAuth2Service.getAccessAndRefreshToken(authorizationCode);
    }


}
