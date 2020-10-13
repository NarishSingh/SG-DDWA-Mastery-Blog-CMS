package com.sg.hellosecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetails;
    
    /*
    @Autowired
    public void configureGlobalInMemory(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}password").roles("ADMIN", "USER");
    }
    */
    
    @Autowired
    public void configureGlobalInDB(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder());
    }
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //admin page is only for admin
                .antMatchers("/admin").hasRole("ADMIN")
                //this will be for all users
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/css/**", "/js/**", "/fonts/**").permitAll()
                //anything else, is for all users
                .anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?login_error=1") //if login fails, go to this url
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/") //on log out, go to base page
                .permitAll();
    }

}
