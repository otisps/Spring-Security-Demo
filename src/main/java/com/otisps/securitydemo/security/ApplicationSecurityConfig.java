package com.otisps.securitydemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
                .csrf().disable() // TODO : Amigo will teach this
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*" , "/js/*").permitAll()
                .antMatchers("/api/students").hasRole(STUDENT.name())
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
                        .username("otis.ps").password(passwordEncoder.encode("password"))
                        .authorities(STUDENT.getGrantedAuthorities()).build();
        UserDetails moriUser=
                User.builder()
                        .username("mori.ph").password(passwordEncoder.encode("password"))
                        .authorities(ADMINTRAINEE.getGrantedAuthorities()).build();
        UserDetails sherlockUser = User.builder()
                .username("sherlock.ph").password(passwordEncoder.encode("password"))
                .authorities(ADMIN.getGrantedAuthorities()).build();


        return new InMemoryUserDetailsManager(otisUser, sherlockUser, moriUser);
    }
}
