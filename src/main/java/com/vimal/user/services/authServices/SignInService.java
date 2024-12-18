package com.vimal.user.services.authServices;

import com.vimal.user.customEceptions.*;
import com.vimal.user.dtos.ResponseDTO;
import com.vimal.user.enums.UserStatus;
import com.vimal.user.enums.UserType;
import com.vimal.user.models.RegisterJWT;
import com.vimal.user.models.User;
import com.vimal.user.models.VerificationCode;
import com.vimal.user.repositories.RegisterJWTRepository;
import com.vimal.user.repositories.UserRepository;
import com.vimal.user.repositories.VerificationCodeRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SignInService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PhoneNumberValidationService phoneNumberValidationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Random random;
    @Autowired
    EmailService emailService;
    @Autowired
    JwtService jwtService;
    @Autowired
    VerificationCodeRepository verificationCodeRepository;
    @Autowired
    private RegisterJWTRepository registerJWTRepository;


    public ResponseDTO verifyUser(String email, String phoneNumber, String countryCode, String password, UserType userType, HttpServletResponse response) throws InvalidPasswordException {
        User user;
        try{
            user = email!=null? userRepository.findByEmail(email):userRepository.findByPhoneNumberAndCountryCode(phoneNumber, countryCode);
        }catch(DataAccessException e){
            throw new DataBaseAccessException("Could not get data from database");
        }
        if(user==null) {
            throw new UserNotFoundException("User not found with given details");
        }
        if(user.getUserType() != userType){
            throw new UserNotFoundException("User type not match, Please Sign In as :"+user.getUserType());
        }
        if(user.getUserStatus() != UserStatus.ACTIVE){
            throw new UserNotActiveException("Account is "+user.getUserStatus()+", Please contact customer service.");
        }
        if(!emailService.isPasswordValid(password)){
            throw new InvalidPasswordException("Password entered should be 7+ characters");
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new InvalidPasswordException("Password does not match");
        }
        String token =jwtService.generateSigninToken(""+user.getId() , user.getUserType());
        Cookie jwtCookie ;
        if(user.getUserType() == UserType.CUSTOMER) {
            jwtCookie = new Cookie("jwt_signIn_token_customer", token);
            jwtCookie.setPath("/");
        }else {
            jwtCookie = new Cookie("jwt_signIn_token_retailer", token);
            jwtCookie.setPath("/retailer");
        }
        // Set the lifespan of the cookie
        jwtCookie.setMaxAge(172800);
        jwtCookie.setHttpOnly(true);
        response.addCookie(jwtCookie);
        return new ResponseDTO( "Verification successful!!");
    }


    public ResponseDTO passwordReset(String email, String phoneNumber, String countryCode, UserType userType, boolean reset, HttpServletResponse response) {
        User user;
        try{
            user = email!=null? userRepository.findByEmail(email):userRepository.findByPhoneNumberAndCountryCode(phoneNumber, countryCode);
        }catch(DataAccessException e){
            throw new DataBaseAccessException("Could not get data from database");
        }
        if(user==null) {
            throw new UserNotFoundException("User not found with given details");
        }
        email = user.getEmail();
        if(user.getUserType() != userType){
            throw new UserNotFoundException("User type not match, Please Reset password as :"+user.getUserType());
        }
        if(reset && registerJWTRepository.existsByEmail(email)) {
            registerJWTRepository.deleteByEmail(email);
        }
        if(registerJWTRepository.existsByEmail(email)){
            return new ResponseDTO("Reset");
        }
        String token = jwtService.generateRegistrationToken(email, userType);
        try{
            registerJWTRepository.save(new RegisterJWT(email, token));
        }catch (DataIntegrityViolationException ex) {
            throw new SignUpAlreadyInProgressException("Password reset process already on-going");
        }catch (DataAccessException e) {
            throw new DataBaseAccessException("Failed to connect to database");
        }
        Cookie jwtCookie = new Cookie("jwt_reset_token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/signIn");
        response.addCookie(jwtCookie);
        return new ResponseDTO("Credentials Verified!!");
    }


    public ResponseDTO sendCode(String email, String phoneNumber, String countryCode, String jwtToken) {
        User user;
        try{
            user = email!=null? userRepository.findByEmail(email):userRepository.findByPhoneNumberAndCountryCode(phoneNumber, countryCode);
        }catch(DataAccessException e){
            throw new DataBaseAccessException("Could not get data from database");
        }
        if(user==null) {
            throw new UserNotFoundException("User not found with given details");
        }
        RegisterJWT registerJWT = registerJWTRepository.findByEmail(user.getEmail()).orElse(null);
        if(registerJWT == null || !registerJWT.getToken().equals(jwtToken)){
            throw new UnauthorizedException("Unauthorized/ Password Reset process is already on-going");
        }
        if(verificationCodeRepository.existsByEmail(user.getEmail())){
            verificationCodeRepository.deleteByEmail(user.getEmail());
        }
        String verificationCode = "123456";//"""+(random.nextInt(900000) + 100000);
        if(email==null){
            phoneNumberValidationService.sendVerificationPhoneNumber(countryCode+phoneNumber,verificationCode);

        }else{
            emailService.sendVerificationEmail(email,verificationCode);

        }
        try{
            verificationCodeRepository.save(new VerificationCode(jwtService.getSubjectFromToken(jwtToken),verificationCode));
        }catch (DataIntegrityViolationException ex) {
            throw new SignUpAlreadyInProgressException("SignUp process already on-going");
        }catch (DataAccessException e) {
            throw new DataBaseAccessException("Failed to connect to database");
        }
        if(email==null) {
            return new ResponseDTO( "Code sent successfully to "+countryCode+phoneNumber+"!!");
        }else {
            return new ResponseDTO( "Code sent successfully to "+email+"!!");
        }
    }

    public ResponseDTO verifyCode( String verificationCode, String jwtToken) {
        String email = jwtService.getSubjectFromToken(jwtToken);

        User user;
        try{
            user = userRepository.findByEmail(email);
        }catch(DataAccessException e){
            throw new DataBaseAccessException("Could not get data from database");
        }
        if(user==null) {
            throw new UserNotFoundException("User not found with given details");
        }
        RegisterJWT registerJWT = registerJWTRepository.findByEmail(user.getEmail()).orElse(null);
        if(registerJWT == null || !registerJWT.getToken().equals(jwtToken)){
            throw new UnauthorizedException("Unauthorized/ Password Reset process is already on-going");
        }
        VerificationCode verificationCodeDB = verificationCodeRepository.findByEmail(email);
        if(verificationCodeDB==null) {
            throw new VerificationCodeExpiredException("Verification code expired");
        }
        if(!verificationCode.equals(verificationCodeDB.getVerificationCode())) {
            throw new InvalidVerificationCodeException("Entered verification Code is incorrect.");
        }
        return new ResponseDTO("Entered verification Codes are correct.");
    }


    public ResponseDTO changePassword(String newPassword, String jwtToken, HttpServletResponse response) {
        String email = jwtService.getSubjectFromToken(jwtToken);
        User user;
        try{
            user = userRepository.findByEmail(email);
        }catch(DataAccessException e){
            throw new DataBaseAccessException("Could not get data from database");
        }
        if(user==null) {
            throw new UserNotFoundException("User not found with given details");
        }
        RegisterJWT registerJWT = registerJWTRepository.findByEmail(user.getEmail()).orElse(null);
        if(registerJWT == null || !registerJWT.getToken().equals(jwtToken)){
            throw new UnauthorizedException("Unauthorized/ Password Reset process is already on-going");
        }
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        try{
            userRepository.save(user);
        }catch (DataAccessException e) {
            throw new DataBaseAccessException("Failed to connect to database");
        }
        verificationCodeRepository.deleteByEmail(email);
        registerJWTRepository.deleteByEmail(email);
        Cookie jwtCookie = new Cookie("jwt_reset_token", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/signIn");  // set the path to what you set it to when creating the cookie
        response.addCookie(jwtCookie);
        return new ResponseDTO("Password reset successful!! Please Sign in.");
    }
}











