package it.impresaconsulting.Gestic.utilities;

import it.impresaconsulting.Gestic.daos.UtenteDao;
import it.impresaconsulting.Gestic.entities.Utente;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** DOCS:
 * https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html
 */

@Log
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
@Component
public class SecurityImpl extends WebSecurityConfigurerAdapter implements AuthenticationProvider {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    @Autowired UtenteDao utenteDao;
    @Autowired EncryptionUtils encryptionUtils;

    /* authentication provider part */

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String username = auth.getName();
        String password = auth.getCredentials().toString();
        String ruolo = "";

        Optional<Utente> utenteOptional = utenteDao.findById(username);
        if(utenteOptional.isPresent()){
            Utente utente = utenteOptional.get();
            if(password.equals(encryptionUtils.decrypt(utente.getPassword()))) {
                ruolo = utenteOptional.get().getRuolo();
            }
        }
        if(ROLE_ADMIN.equals(ruolo)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority(ROLE_USER));
            grantedAuths.add(new SimpleGrantedAuthority(ROLE_ADMIN));
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else if(ROLE_USER.equals(ruolo)){
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority(ROLE_USER));
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else {
            throw new BadCredentialsException("Autenticazione fallita");
        }
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }



    /* websecurity adapter part: erase it if you don't want login alert but default spring login web page */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this); //this because it is either a WebSecurityAdapter than an AuthenticationProvider
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();   //con Spring Security bisognerebbe inserire il cookie csrf nelle richieste POST con Ajax, altrimenti da 403 Forbidden
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()  //.loginPage("/signin.html")
                .and()
                .httpBasic()
                .and()
                .logout().clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //puo' non esistere nel controller
                .logoutSuccessUrl("/test")    //deve esistere nel controller
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);


    }

    /*  per non filtrare con il login alcuni path  */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/test");
       // web.ignoring().antMatchers("/resources/**");
       // web.ignoring().antMatchers("/test", "/signin.html");
    }


}