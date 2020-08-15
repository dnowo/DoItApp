package io.github.dnowo.DoitApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class RestAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final long tokenExpirationTime;
    private final String secret;

    public RestAuthSuccessHandler(
            @Value("${jwt.tokenExpirationTime}") long tokenExpirationTime,
            @Value("${jwt.secret}") String secret) {
        this.tokenExpirationTime = tokenExpirationTime;
        this.secret = secret;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails authenticationPrincipal = (UserDetails) authentication.getPrincipal();
        String jToken = JWT.create()
                .withSubject(authenticationPrincipal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .sign(Algorithm.HMAC256(secret));
        //response.getOutputStream().print("{\"token\": \""+ jToken + "\"}");
        response.addHeader("Authorization", "Bearer " + jToken);
    }
}
