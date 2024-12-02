package com.vimal.user.controllers;

import com.vimal.user.dtos.CreateUserDTO;
import com.vimal.user.dtos.ResponseDTO;
import com.vimal.user.dtos.VerifyCodeDTO;
import com.vimal.user.dtos.VerifyUserDTO;
import com.vimal.user.services.authServices.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/signUp")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;


    @PostMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> verifyUser(@RequestBody VerifyUserDTO verifyUserDTO, HttpServletResponse response){
        ResponseDTO responseDTO = signUpService.verifyUser(verifyUserDTO.getEmail(),verifyUserDTO.getPhoneNumber(),verifyUserDTO.getCountryCode(),verifyUserDTO.getUserType(),verifyUserDTO.isReset(),response);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/sendVerificationCode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> sendCode(@CookieValue(value = "jwt_token", required = true) String jwtToken,
                                                 @RequestBody VerifyUserDTO verifyUserDTO)
    {
        return ResponseEntity.ok(signUpService.sendCode(verifyUserDTO.getEmail(), verifyUserDTO.getPhoneNumber(),verifyUserDTO.getCountryCode(), jwtToken));
    }

    @PostMapping(value = "/verifyCode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> verifyCode(@CookieValue(value = "jwt_token", required = true) String jwtToken,
                                                  @RequestBody VerifyCodeDTO verifyCodeDTO)
    {
        System.out.println(verifyCodeDTO.toString());
        return ResponseEntity.ok(signUpService.verifyCode(verifyCodeDTO.getEmail(),verifyCodeDTO.getEmailVerificationCode(), verifyCodeDTO.getPhoneNumberVerificationCode(), jwtToken));
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> registration(@CookieValue(value = "jwt_token", required = true) String jwtToken,@RequestBody CreateUserDTO createUserDTO, HttpServletResponse response)
    {
        System.out.println(createUserDTO.toString());
        return ResponseEntity.ok(signUpService.registerUser(createUserDTO.getEmail(),
                createUserDTO.getFirstName(),
                createUserDTO.getLastName(),
                createUserDTO.getPassword(),
                createUserDTO.getUserType(),
                createUserDTO.getPhoneNumber(),
                createUserDTO.getCountryCode(),
                createUserDTO.getCompanyBrand(),
                createUserDTO.getCompanyAddress(),
                createUserDTO.getCompanyCity(),
                createUserDTO.getCompanyState(),
                createUserDTO.getCompanyPincode(),
                response,
                jwtToken));
    }
}
