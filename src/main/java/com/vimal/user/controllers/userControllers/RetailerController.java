package com.vimal.user.controllers.userControllers;

import com.vimal.user.dtos.RetailerDTO;
import com.vimal.user.services.userServices.RetailerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retailer")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RetailerController {
    @Autowired
    RetailerService retailerService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> retailerCheck() {
        System.out.println("In retailer Controller");

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/profile",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RetailerDTO> retailerGet(@CookieValue(value = "jwt_signIn_token_retailer", required = true) String jwtToken){
        System.out.println("In retailer Profile Controller");

        return ResponseEntity.ok(retailerService.getProfile(jwtToken));

    }

    @PostMapping(value = "/signout",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> retailerSignOut(HttpServletResponse response){
        System.out.println("In retailer SignOut Controller");
        retailerService.signOutRetailer(response);
        return ResponseEntity.ok().build();

    }

}
