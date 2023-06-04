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
                .antMatchers(  "/register.html","/api/clients/current/accounts", "/api/clients/current/**" , "/api/loans").permitAll()
                .antMatchers( HttpMethod.POST, "/api/login" ,"/api/clients", "/api/logout").permitAll()
                .antMatchers( HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers(HttpMethod.POST, "/api/loans/manager").hasAnyAuthority("ADMIN")
                .antMatchers( HttpMethod.PUT,"/api/clients", "/api/clients/current/cards", "/api/clients/current/accounts").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers( HttpMethod.POST,"/api/clients/current/accounts" , "/api/clients/current/cards" , "/api/clients/current/transactions" , "/api/logout" , "/api/loans" , "/api/current/loans").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers("/acconts.html","/account.html","/loan-application.html" , "/cards.html",
                        "/create-cards.html","/transfer.html","/api/clients/current" ,"/api/loans" ).hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers( HttpMethod.POST, "/api/transaction","/api/clients/current/cards", "/api/current/loans","/api/clients/current/accounts").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers( HttpMethod.PUT,"/api/clients", "/api/clients/current/cards", "/api/clients/current/accounts").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers("/manager.html" , "/rest/**" , "/h2-console","/api/clients" ).hasAuthority("ADMIN")
                .anyRequest().denyAll();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");
        http.logout().logoutUrl("/api/logout").deleteCookies( "JSESSIONID");

        // por cada peticion que se haga en general, te va a traer un tokes, por eso se desactiva
        http.csrf().disable();

        // esto se desactiva par poder utilizar el h2-console
        http.headers().frameOptions().disable();

        // cuando un usuario intenta navegar por paginas no autorizadas te manda un error
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // si el login salio bien, no te pide que se este autenticando mientras navegues por la pagina
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // si el login salio mal nos manda un error
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_BAD_REQUEST));

        // si el deslogueo salio bien te manda una respuesta exitosa
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
