package com.reviewer;

import com.reviewer.user.CustomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService(){
        return new CustomDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    // movie pages secured
                    .antMatchers("/new_movie/**").hasAuthority("ADMIN")
                    .antMatchers("/edit_movie/**").hasAuthority("ADMIN")
                    .antMatchers("/delete_movie/**").hasAuthority("ADMIN")
                    .antMatchers("/save_movie/**").hasAuthority("ADMIN")
                    // review pages secured
                    .antMatchers("/new_review/").authenticated()
                    .antMatchers("/review_form").authenticated()
                    .antMatchers("/save_review/**").authenticated()
                    .antMatchers("/edit_review/**").authenticated()
                    .antMatchers("/delete_review/**").hasAuthority("ADMIN")
                    // comment pages secured
                    .antMatchers("/save_comment/**").authenticated()
                    .antMatchers("/delete_comment/**").hasAuthority("ADMIN")
                    // log movies
                    .antMatchers("/save_favourite/**").authenticated()
                    .antMatchers("/save_seen/**").authenticated()
                    .antMatchers("/save_want/**").authenticated()
                    .antMatchers("/save_favourite/**").authenticated()
                    // like review/comment
                    .antMatchers("/like_review/**").authenticated()
                    .antMatchers("/like_comment/**").authenticated()
                    // static files
                    .antMatchers("/images/**", "/js/**", "/css/**").permitAll()
                    .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .defaultSuccessUrl("/")
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }
}
