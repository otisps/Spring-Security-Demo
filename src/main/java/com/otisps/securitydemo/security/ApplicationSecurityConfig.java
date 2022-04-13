package com.otisps.securitydemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.otisps.securitydemo.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private final PasswordEncoder passwordEncoder;

    @Autowired private final PasswordEncoder superSlowPasswordEncoder;


    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, PasswordEncoder superSlowPasswordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.superSlowPasswordEncoder = superSlowPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*" , "/js/*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }


    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails otisUser=
                User.builder()
                        .username("otisps").password(passwordEncoder.encode("password"))
                        .roles(STUDENT.name()).build();

        UserDetails otisAdmin = User.builder()
                .username("otis").password(passwordEncoder.encode("password"))
                .roles(ADMIN.name()).build();
        return new InMemoryUserDetailsManager(otisUser, otisAdmin);
    }
}
