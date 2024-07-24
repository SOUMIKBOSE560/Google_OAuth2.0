package com.soumik.OAuth2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleDriveService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String DRIVE_API_URL = "https://www.googleapis.com/drive/v3/files";

    //Working..
    public Object getDriveFiles(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(DRIVE_API_URL + "?pageSize=10&fields=nextPageToken,files(id,name)", HttpMethod.GET, entity, Object.class);
        return response;
    }

}
