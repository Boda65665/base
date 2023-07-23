package com.base.new_base.configs;

import com.base.new_base.Entiti.Role;
import com.base.new_base.JWT.jwtProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    final jwtProvider jwtProvider;

    public CustomAccessDeniedHandler(com.base.new_base.JWT.jwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        System.out.println(jwtProvider.getRole(jwtProvider.resolveToken(request))==Role.UNVERIFIED);
                        if(jwtProvider.getRole(jwtProvider.resolveToken(request))==Role.UNVERIFIED) response.sendRedirect("/auth/email_confirmation");
                        else{
                            response.sendRedirect("/home");

                        }
    }
}