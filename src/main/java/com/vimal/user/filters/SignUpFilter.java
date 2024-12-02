package com.vimal.user.filters;

import com.vimal.user.services.authServices.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SignUpFilter implements Filter{
    private JwtService jwtService;

    public SignUpFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Max-Age", "120");

        if (req.getMethod().equals("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
//		System.out.println("Request Method = " + req.getMethod() + ", URL = " + req.getRequestURL());
//		String body = IOUtils.toString(req.getReader());
//
//        System.out.println("Raw JSON: " + body);
        String path = req.getRequestURI();
        boolean excluded = path.equals("/signUp");

        if (!excluded) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                boolean tokenIsValid = false;

                for (Cookie cookie : cookies) {
                    if ("jwt_token".equals(cookie.getName())) {
                        String jwtToken = cookie.getValue();
                        try {
                            jwtService.validateToken(jwtToken);
                            tokenIsValid = true;
                            break;
                        } catch (ExpiredJwtException e) {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.getWriter().write("Token has expired");
                            return;
                        } catch (MalformedJwtException e) {
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            res.getWriter().write("Invalid token format");
                            return;
                        } catch (Exception e) {
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            res.getWriter().write("Unknown Error");
                            return;
                        }
                    }
                }

                if (!tokenIsValid) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write("Unauthorized");
                    return;
                }
            } else {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Unauthorized");
                return;
            }
        }
        chain.doFilter(request, response);

    }
}
