package com.vimal.user.services.userServices;

import com.vimal.user.customEceptions.UserNotFoundException;
import com.vimal.user.dtos.RetailerDTO;
import com.vimal.user.enums.UserStatus;
import com.vimal.user.models.Retailer;
import com.vimal.user.repositories.CustomerRepository;
import com.vimal.user.repositories.RetailerRepository;
import com.vimal.user.services.authServices.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetailerService{
    @Autowired
    private RetailerRepository retailerRepository;
    @Autowired
    private JwtService jwtService; // Service to parse JWT token
    @Autowired
    private CustomerRepository customerRepository;

    public RetailerDTO getProfile(String jwtToken) {
        long userId = Long.parseLong(jwtService.getSubjectFromToken(jwtToken));
        Retailer retailer = retailerRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Retailer not found"));
        return new RetailerDTO(retailer.getFirstName(),
                retailer.getLastName(),
                retailer.getEmail(),
                retailer.getPhoneNumber(),
                retailer.getCountryCode(),
                retailer.getUserStatus(),
                retailer.getCompanyAddress(),
                retailer.getCompanyBrand(),
                retailer.getCompanyCity(),
                retailer.getCompanyState(),
                retailer.getCompanyPincode());
    }

    public void signOutRetailer(HttpServletResponse response) {
        // TODO Auto-generated method stub
        Cookie jwtCookie = new Cookie("jwt_signIn_token_retailer", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/retailer");  // set the path to what you set it to when creating the cookie
        response.addCookie(jwtCookie);

    }
}
