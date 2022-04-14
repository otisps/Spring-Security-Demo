package com.otisps.securitydemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.otisps.securitydemo.security.ApplicationUserRole.*;
import static com.otisps.securitydemo.security.ApplicationUserPermission.*;
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
                .csrf().disable() // TODO : Amigo will teach this
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*" , "/js/*").permitAll()
                .antMatchers("/api/students").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.DELETE,"/mangement/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT,"/mangement/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.POST,"/mangement/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.GET,"/mangement/api/**").hasAnyRole(ADMIN.getRole(), ADMINTRAINEE.getRole())
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
   //                     .roles(STUDENT.name()).build(); // User otis.ps  Role_ Student
                        .authorities(STUDENT.getGrantedAuthorities()).build();
        UserDetails moriUser=
                User.builder()
                        .username("mori.ph").password(passwordEncoder.encode("password"))
//                        .roles(ADMINTRAINEE.name()).build(); // user mori.ph Role admintrainee
                        .authorities(ADMINTRAINEE.getGrantedAuthorities()).build();
        UserDetails sherlockUser = User.builder()
                .username("sherlock.ph").password(passwordEncoder.encode("password"))
//                .roles(ADMIN.name()).build(); // user otis role admin
                .authorities(ADMIN.getGrantedAuthorities()).build();


        return new InMemoryUserDetailsManager(otisUser, sherlockUser, moriUser);
    }
}
