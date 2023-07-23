package com.base.new_base.JWT;

import com.base.new_base.Service.User.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    final UserService userService;
    final jwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);


        if (jwtProvider.validateToken(token)) {
            System.out.println(jwtProvider.getAuthentication(token).toString());
            SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthentication(token));


        }
        else SecurityContextHolder.clearContext();



        filterChain.doFilter(request, response);
    }
}