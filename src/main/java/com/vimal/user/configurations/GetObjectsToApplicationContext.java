package com.vimal.user.configurations;

import com.vimal.user.filters.CustomerFilter;
import com.vimal.user.filters.RetailerFilter;
import com.vimal.user.filters.SignInFilter;
import com.vimal.user.filters.SignUpFilter;
import com.vimal.user.services.authServices.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@Configuration
public class GetObjectsToApplicationContext {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public Random random() {
        return new Random();
    }


    @Bean
    public FilterRegistrationBean<SignUpFilter> signUpFilter(JwtService jwtService) {
        FilterRegistrationBean<SignUpFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SignUpFilter(jwtService));
        registrationBean.addUrlPatterns("/signUp/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SignInFilter> signInFilter(JwtService jwtService) {
        FilterRegistrationBean<SignInFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SignInFilter(jwtService));
        registrationBean.addUrlPatterns("/signIn/*");
        registrationBean.setOrder(2); // This sets the order in which filters are invoked
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RetailerFilter> retailerFilter(JwtService jwtService) {
        FilterRegistrationBean<RetailerFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RetailerFilter(jwtService));
        registrationBean.addUrlPatterns("/retailer/*");
        registrationBean.setOrder(3); // This sets the order in which filters are invoked
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<CustomerFilter> customerFilter(JwtService jwtService) {
        FilterRegistrationBean<CustomerFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CustomerFilter(jwtService));
        registrationBean.addUrlPatterns("/customer/*");
        registrationBean.setOrder(4); // This sets the order in which filters are invoked
        return registrationBean;
    }
}
