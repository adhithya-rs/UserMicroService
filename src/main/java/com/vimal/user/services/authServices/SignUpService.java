package com.vimal.user.services.authServices;

import java.util.*;
import com.vimal.user.customEceptions.*;
import com.vimal.user.dtos.ResponseDTO;
import com.vimal.user.enums.UserStatus;
import com.vimal.user.enums.UserType;
import com.vimal.user.models.*;
import com.vimal.user.repositories.RegisterJWTRepository;
import com.vimal.user.repositories.UserRepository;
import com.vimal.user.repositories.VerificationCodeRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class SignUpService {
    @Autowired
    RegisterJWTRepository registerJWTRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    VerificationCodeRepository verificationCodeRepository;
    @Autowired
    PhoneNumberValidationService phoneNumberValidationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Random random;


    public ResponseDTO verifyUser(String email, String phoneNumber, String countryCode, UserType userType, boolean reset, HttpServletResponse response) {
        if(reset && registerJWTRepository.existsByEmail(email)){
            registerJWTRepository.deleteByEmail(email);
        }
        if(!emailService.isEmailValid(email)){
            throw new InvalidEmailException("Please enter a valid e-mail address");
        }
        if(userRepository.existsByEmail(email)){
            throw new UserAlreadyExistsException("User already exists with this e-mail :"+email);
        }
        if(userRepository.existsByPhoneNumberAndCountryCode(phoneNumber, countryCode)){
            throw new UserAlreadyExistsException("User already exist with this Phone Number :"+countryCode+phoneNumber);
        }
        if(registerJWTRepository.existsByEmail(email)){
            return new ResponseDTO("Reset");
        }
        String token = jwtService.generateRegistrationToken(email, userType);
        try{
            registerJWTRepository.save(new RegisterJWT(email, token));
        }catch (DataIntegrityViolationException ex) {
            throw new SignUpAlreadyInProgressException("SignUp process already on-going");
        }catch (DataAccessException e) {
            throw new DataBaseAccessException("Failed to connect to database");
        }
        Cookie jwtCookie = new Cookie("jwt_token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/signUp");
        response.addCookie(jwtCookie);
        return new ResponseDTO("e-mail and phone number verified in DB!!");
    }


    public ResponseDTO sendCode(String email, String phoneNumber, String countryCode, String jwtToken) {
        RegisterJWT registerJWT = registerJWTRepository.findByEmail(email).orElse(null);
        if(registerJWT == null || !registerJWT.getToken().equals(jwtToken)){
            throw new UnauthorizedException("Unauthorized/ Sign up process is already on-going");
        }
        if(verificationCodeRepository.existsByEmail(email)){
            verificationCodeRepository.deleteByEmail(email);
        }
        String emailCode = ""+(random.nextInt(900000) + 100000);
        emailService.sendVerificationEmail(email,emailCode);
        String phoneNumberCode=""+(random.nextInt(900000) + 100000);
        phoneNumberValidationService.sendVerificationPhoneNumber(countryCode+phoneNumber,phoneNumberCode);
        try{
            verificationCodeRepository.save(new VerificationCode(email,phoneNumberCode,emailCode));
        }catch (DataIntegrityViolationException ex) {
            throw new SignUpAlreadyInProgressException("SignUp process already on-going");
        }catch (DataAccessException e) {
            throw new DataBaseAccessException("Failed to connect to database");
        }
        return new ResponseDTO("Verification Codes sent successfully to Mobile Number And email-id");
    }

    public ResponseDTO verifyCode(String email, String emailVerificationCode, String phoneNumberVerificationCode, String jwtToken) {
        RegisterJWT registerJWT = registerJWTRepository.findByEmail(email).orElse(null);
        if(registerJWT == null || !registerJWT.getToken().equals(jwtToken)){
            throw new UnauthorizedException("Unauthorized/ Sign up process is already on-going");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
        if (verificationCode == null) {
            throw new VerificationCodeExpiredException("Verification code is expired, resend the code");
        }
        if(!emailVerificationCode.equals(verificationCode.getEmailVerificationCode())){
            throw new InvalidVerificationCodeException("Entered Email verification Code is incorrect.");
        }
        if(phoneNumberVerificationCode.equals(verificationCode.getPhoneNumberVerificationCode())){
            throw new InvalidVerificationCodeException("Entered Phone Number verification Code is incorrect.");
        }
        verificationCodeRepository.deleteByEmail(email);
        return new ResponseDTO("Entered verification Codes are correct.");
    }

    public ResponseDTO registerUser(String email,
                                    String firstName,
                                    String lastName,
                                    String password,
                                    UserType userType,
                                    String phoneNumber,
                                    String countryCode,
                                    String companyBrand,
                                    String companyAddress,
                                    String companyCity,
                                    String companyState,
                                    String companyPincode,
                                    HttpServletResponse response,
                                    String jwtToken) {

        RegisterJWT registerJWT = registerJWTRepository.findByEmail(email).orElse(null);
        if(registerJWT == null || !registerJWT.getToken().equals(jwtToken)){
            throw new UnauthorizedException("Unauthorized/ Sign up process is already on-going");
        }
        if(!emailService.isPasswordValid(password)){
            throw new InvalidPasswordException("Password entered should be 7+ characters");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user;
        if (userType == UserType.CUSTOMER) {
            // Create a Customer
            Customer customer = new Customer();
            customer.setEmail(email);
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setPassword(hashedPassword);
            customer.setPhoneNumber(phoneNumber);
            customer.setCountryCode(countryCode);
            customer.setUserType(userType);
            customer.setUserStatus(UserStatus.ACTIVE);
            user = customer; // Assign customer to the user reference
        } else if (userType == UserType.RETAILER) {
            // Create a Retailer
            Retailer retailer = new Retailer();
            retailer.setEmail(email);
            retailer.setFirstName(firstName);
            retailer.setLastName(lastName);
            retailer.setPassword(hashedPassword);
            retailer.setPhoneNumber(phoneNumber);
            retailer.setCountryCode(countryCode);
            retailer.setUserType(userType);
            retailer.setUserStatus(UserStatus.LOCKED); // Initial status as LOCKED
            retailer.setCompanyBrand(companyBrand);
            retailer.setCompanyAddress(companyAddress);
            retailer.setCompanyCity(companyCity);
            retailer.setCompanyState(companyState);
            retailer.setCompanyPincode(companyPincode);
            user = retailer; // Assign retailer to the user reference
        } else {
            throw new IllegalArgumentException("Unsupported user type: " + userType);
        }
        Cookie jwtCookie = new Cookie("jwt_token", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/signUp");  // setting the path to which is set when creating the cookie
        response.addCookie(jwtCookie);
        registerJWTRepository.deleteByEmail(email);
        try{
            // Save User (cascades to Customer/Retailer)
            userRepository.save(user);
            return new ResponseDTO("User registered successfully");
        }catch(DataIntegrityViolationException ex){
            throw new UserAlreadyExistsException("User already exists");
        }catch (DataAccessException e) {
            throw new DataBaseAccessException("Failed to connect to database");
        }
    }
}
