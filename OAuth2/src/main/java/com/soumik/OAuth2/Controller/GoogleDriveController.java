package com.soumik.OAuth2.Controller;

import com.soumik.OAuth2.Service.GoogleDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/googledrive")
public class GoogleDriveController {

    @Autowired
    private GoogleDriveService googleDriveService;

    @GetMapping("/getfiles")
    public Object getFiles(@RequestParam String accessToken){
        return googleDriveService.getDriveFiles(accessToken);
    }
}
