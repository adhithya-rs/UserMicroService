package com.vimal.user.controllers;

import com.vimal.user.dtos.ChangePasswordDTO;
import com.vimal.user.dtos.ResponseDTO;
import com.vimal.user.dtos.VerifyCodeDTO;
import com.vimal.user.dtos.VerifyUserDTO;
import com.vimal.user.services.authServices.SignInService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signIn")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SignInControler {

    @Autowired
    private SignInService signInService;

    @PostMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> verifyUser(@RequestBody VerifyUserDTO verifyUserDTO, HttpServletResponse response){
        ResponseDTO responseDTO = signInService.verifyUser(verifyUserDTO.getEmail(),verifyUserDTO.getPhoneNumber(),verifyUserDTO.getCountryCode(), verifyUserDTO.getPassword(), verifyUserDTO.getUserType(),response);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/passwordreset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> passwordReset(@RequestBody VerifyUserDTO verifyUserDTO, HttpServletResponse response)
    {
        System.out.println(verifyUserDTO.toString());
        ResponseDTO responseDTO = signInService.passwordReset(verifyUserDTO.getEmail(), verifyUserDTO.getPhoneNumber(), verifyUserDTO.getCountryCode(), verifyUserDTO.getUserType(), verifyUserDTO.isReset(), response);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/sendCode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> sendCode(@CookieValue(value = "jwt_reset_token", required = true) String jwtToken, @RequestBody VerifyUserDTO verifyUserDTO)
    {
        System.out.println(verifyUserDTO.toString());
        return ResponseEntity.ok(signInService.sendCode(verifyUserDTO.getEmail(), verifyUserDTO.getPhoneNumber(),verifyUserDTO.getCountryCode(), jwtToken));
    }

    @PostMapping(value = "/verifycode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> verifyCode(@CookieValue(value = "jwt_reset_token", required = true) String jwtToken,
                                                  @RequestBody VerifyCodeDTO verifyCodeDTO)
    {
        System.out.println(verifyCodeDTO.toString());
        return ResponseEntity.ok(signInService.verifyCode(verifyCodeDTO.getVerificationCode(), jwtToken));
    }

    @PostMapping(value = "/changePassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> changePassword(@CookieValue(value = "jwt_reset_token", required = true) String jwtToken,
                                                      @RequestBody ChangePasswordDTO changePasswordDTO, HttpServletResponse response)
    {
        System.out.println(changePasswordDTO.toString());
        return ResponseEntity.ok(signInService.changePassword(changePasswordDTO.getNewPassword(), jwtToken, response));
    }

}
