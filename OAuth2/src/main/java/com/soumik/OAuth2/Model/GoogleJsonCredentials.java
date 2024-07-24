package com.soumik.OAuth2.Model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GoogleJsonCredentials {
    private String type;
    private String client_id;
    private String project_id;
    private String auth_uri;
    private String token_uri;
    private String auth_provider_x509_cert_url;
    private String client_secret;
    private List<String> redirect_uris;
    private String refresh_token;
}
