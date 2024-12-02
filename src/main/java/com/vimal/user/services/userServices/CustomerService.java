package com.vimal.user.services.userServices;

import com.vimal.user.customEceptions.UserNotFoundException;
import com.vimal.user.dtos.CustomerDTO;
import com.vimal.user.dtos.RetailerDTO;
import com.vimal.user.models.Customer;
import com.vimal.user.models.Retailer;
import com.vimal.user.repositories.CustomerRepository;
import com.vimal.user.services.authServices.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtService jwtService;


    public CustomerDTO getProfile(String jwtToken) {
        long userId = Long.parseLong(jwtService.getSubjectFromToken(jwtToken));
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Customer not found"));
        return new CustomerDTO(customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getCountryCode(),
                customer.getUserStatus());
    }

    public void signOutCustomer(HttpServletResponse response) {
        // TODO Auto-generated method stub
        Cookie jwtCookie = new Cookie("jwt_signIn_token_customer", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");  // set the path to what you set it to when creating the cookie
        response.addCookie(jwtCookie);

    }
}
