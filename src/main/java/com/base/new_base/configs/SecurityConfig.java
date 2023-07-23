package com.base.new_base.configs;
import com.base.new_base.Entiti.Permission;
import com.base.new_base.JWT.JwtRequestFilter;
import com.base.new_base.JWT.jwtProvider;
import com.base.new_base.Service.User.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    final private UserService userService;
    final private JwtRequestFilter jwtRequestFilter;
    final jwtProvider providerJwt;

    public SecurityConfig(UserService userService, JwtRequestFilter jwtRequestFilter, jwtProvider providerJwt) {
        this.userService = userService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.providerJwt = providerJwt;
    }


    @Bean
    public AuthenticationEntryPoint delegatingEntryPoint() {
        final LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> map = new LinkedHashMap();



        map.put(new AntPathRequestMatcher("/"), new LoginUrlAuthenticationEntryPoint("/auth/login"));
        final DelegatingAuthenticationEntryPoint entryPoint = new DelegatingAuthenticationEntryPoint(map);
        entryPoint.setDefaultEntryPoint(new LoginUrlAuthenticationEntryPoint("/auth/login"));

        return entryPoint;
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(providerJwt);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(delegatingEntryPoint());

        http
                .csrf().disable()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()


                .authorizeRequests()

                .antMatchers("/auth/restore_password","/auth/restore_password/").authenticated()
                .antMatchers("/auth/logout/","/auth/logout").authenticated()
                .antMatchers("/auth/login","/auth/login/").anonymous()
                .antMatchers("/auth/reg","/auth/reg/").anonymous()
                .antMatchers("/adm/**").hasAuthority(Permission.ADMIN.getPermission())
                .antMatchers("/auth/email_confirm/*","/auth/email_confirm/*/").hasAuthority(Permission.UNVERIFIED.getPermission())
                .antMatchers("/auth/email_confirmation","/auth/email_confirmation/").hasAuthority(Permission.UNVERIFIED.getPermission())
                .antMatchers("/auth/edit_email/","/auth/edit_email").hasAuthority(Permission.UNVERIFIED.getPermission())
                .antMatchers("/auth/restoring_password","/auth/restoring_password/","/auth/restore_password/*").permitAll()
                .anyRequest()
                .hasAuthority(Permission.USER.getPermission()).and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler()).and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }





    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }




}