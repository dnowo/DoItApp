package io.github.dnowo.DoitApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.dnowo.DoitApp.Constants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtObjectAuthFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;
    private final String secret;

    public JwtObjectAuthFilter(AuthenticationManager authenticationManager,
                               UserDetailsService userDetailsService,
                               String secret) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken auth = getAuthentication(request);

        if (auth == null) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String jToken = request.getHeader(Constants.TOKEN_HEADER);

        if (jToken != null && jToken.startsWith(Constants.TOKEN_PREFIX)) {
            String username = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(jToken.replace(Constants.TOKEN_PREFIX, ""))
                    .getSubject();

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            }

        }
        return null;
    }
}
