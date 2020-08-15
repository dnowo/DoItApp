package io.github.dnowo.DoitApp;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm");

    /**
     * For auth login purpose.
     * @see io.github.dnowo.DoitApp.security.SecurityConfig
     * */
    public static String LOGIN_AUTH = "danielnowo";
    public static String PASSWORD_AUTH = "doitapp";

    /**
     * JWT Token configuration
     * @see io.github.dnowo.DoitApp.security.JwtObjectAuthFilter
     * */
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}
