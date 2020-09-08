package io.github.dnowo.DoitApp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dnowo.DoitApp.service.UserDetailsServiceCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final RestAuthFailureHandler loginFailure;
    private final RestAuthSuccessHandler loginSuccess;
    private final String secret;

    private final UserDetailsServiceCustom userDetailsService;

    @Autowired
    public SecurityConfig(ObjectMapper objectMapper,
                          RestAuthFailureHandler loginFailure,
                          RestAuthSuccessHandler loginSuccess,
                          @Value("${jwt.secret}") String secret,
                          UserDetailsServiceCustom userDetailsService) {
        this.objectMapper = objectMapper;
        this.loginFailure = loginFailure;
        this.loginSuccess = loginSuccess;
        this.secret = secret;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().authenticated()
                .and()

                //Stateless session. JwtObjectAuthFilter
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                //Login JSON authentication
                .formLogin().permitAll()
                .and()
                .addFilter(authFilter())
                .addFilter(new JwtObjectAuthFilter(authenticationManager(), userDetailsService, secret))

                //For REST-API error response 401.
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPointJwt())
                //H2-Console
                .and()
                .headers().frameOptions().disable();

    }

    public JsonObjectAuthFilter authFilter() throws Exception{
        JsonObjectAuthFilter jsonAuth = new JsonObjectAuthFilter(objectMapper);
        jsonAuth.setAuthenticationSuccessHandler(loginSuccess);
        jsonAuth.setAuthenticationFailureHandler(loginFailure);
        jsonAuth.setAuthenticationManager(super.authenticationManager()); //Default manager.

        return jsonAuth;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
