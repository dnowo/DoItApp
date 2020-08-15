package io.github.dnowo.DoitApp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonObjectAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public JsonObjectAuthFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            BufferedReader bufferedReader = request.getReader();
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }

            LoginAuth loginAuth = objectMapper.readValue(stringBuilder.toString(), LoginAuth.class);
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(loginAuth.getUsername(), loginAuth.getPassword());

            setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);
        }catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
