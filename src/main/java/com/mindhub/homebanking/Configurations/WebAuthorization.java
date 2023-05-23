package com.mindhub.homebanking.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.authorizeRequests()
                .antMatchers("/index.html" , "/signup.html").permitAll()
                .antMatchers("/js/**" ).permitAll()
                .antMatchers("/css/**" ).permitAll()
                .antMatchers("/img/**" ).permitAll()
                .antMatchers(  "/register.html","/api/clients","/api/clients/current/accounts", "/api/clients/current/**" , "/api/loans").permitAll()
                .antMatchers( HttpMethod.POST, "/api/login" , "/api/logout").permitAll()
                .antMatchers( HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers(HttpMethod.POST, "/api/loans/manager").hasAnyAuthority("ADMIN")
                .antMatchers( HttpMethod.PUT,"/api/clients", "/api/clients/current/cards", "/api/clients/current/accounts").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers( HttpMethod.POST,"/api/clients/current/accounts" , "/api/clients/current/cards" , "/api/clients/current/transactions" , "/api/logout" , "/api/loans" , "/api/current/loans").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers("/acconts.html","/account.html","/loan-application.html" , "/cards.html",
                        "/create-cards.html","/transfer.html","/api/clients/current" ,"/api/loans" ).hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers( HttpMethod.POST, "/api/transaction","/api/clients/current/cards", "/api/current/loans","/api/clients/current/accounts").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers( HttpMethod.PUT,"/api/clients", "/api/clients/current/cards", "/api/clients/current/accounts").hasAnyAuthority("CLIENT","ADMIN")

                .antMatchers("/manager.html" , "/rest/**" , "/h2-console" ).hasAuthority("ADMIN")
                        .anyRequest().denyAll();




        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");
        http.logout().logoutUrl("/api/logout").deleteCookies( "JSESSIONID");

        // turn off checking for CSRF tokens
        http.csrf().disable();
        // disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();
        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_BAD_REQUEST));
        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
