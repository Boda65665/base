package com.base.new_base.JWT;

import com.base.new_base.Entiti.Role;
import com.base.new_base.Service.User.TemporaryUserService;
import com.base.new_base.Service.User.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Component
public class jwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Duration validityInMilliseconds;
    final UserService userService;
    final TemporaryUserService temporaryUserService;

    public jwtProvider(UserService userService, TemporaryUserService temporaryUserService) {
        this.userService = userService;
        this.temporaryUserService = temporaryUserService;
    }
    public Role getRole(String token){
        String email = getUsername(token);
        if (userService.findByEmail(email)!=null){
            return userService.findByEmail(email).getRole();

        }
        else {
            return temporaryUserService.findByEmail(email).getRole();
        }
    }
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
    public String generateToken(String email,HttpServletResponse response) {
        Claims claims = Jwts.claims().setSubject(email);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + validityInMilliseconds.toMillis());
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        Cookie cookie = new Cookie("JWT", token);//создаем объект Cookie,
        //в конструкторе указываем значения для name и value
        cookie.setPath("/");//устанавливаем путь
        cookie.setMaxAge(86400);//здесь устанавливается время жизни куки
        response.addCookie(cookie);//добавляем Cookie в запрос
        response.setContentType("text/plain");//устанавливаем контекст
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Claims claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return (userService.findByEmail(claimsJws.getSubject()) != null) || (temporaryUserService.findByEmail(claimsJws.getSubject())!=null);
        } catch (JwtException | IllegalArgumentException er) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String token;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("JWT")) {
                    token = c.getValue();
                    return token;
                }
            }
        }
        return null;
    }
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    public Authentication getAuthentication(String token) {
        String email = getUsername(token);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (userService.findByEmail(email)!=null) authorities=userService.findByEmail(email).getRole().getAuthorities();
        else authorities=temporaryUserService.findByEmail(email).getRole().getAuthorities();

        return new UsernamePasswordAuthenticationToken(
                email,
                null,
                authorities

        );    }

}
