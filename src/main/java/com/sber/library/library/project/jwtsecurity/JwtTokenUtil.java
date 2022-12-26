package com.sber.library.library.project.jwtsecurity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Lazy
public class JwtTokenUtil implements Serializable {
    public static final long JWT_TOKEN_VALIDITY = 7 * 24 * 60 * 60; //1 неделя
    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    //Секрет для формирования токена
    private final String secret = "zdtlD3JK56m6wTTgsNFhqzjqP";
    private static final ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        return new ObjectMapper();
    }

    //Метод для получения логина пользователя из JWT токена
    public String getUsernameFromToken(String token) {
        String subject = getClaimFromToken(token, Claims::getSubject);
        log.info("Subject: " + subject);
        JsonNode subjectJSON = null;
        try {
            subjectJSON = objectMapper.readTree(subject);
        } catch (JsonProcessingException e) {
            log.error("JwtTokenUtil#getUserNameFromToken: " + e.getMessage());
        }
        if (subjectJSON != null) {
            return subjectJSON.get("username").asText();
        } else {
            return null;
        }
    }

    //Получение id пользователя из jwt токена
    public String getUserIdFromToken(String token) {
        String subject = getClaimFromToken(token, Claims::getSubject);
        JsonNode subjectJSON = null;
        try {
            subjectJSON = objectMapper.readTree(subject);
        } catch (JsonProcessingException e) {
            log.error("JwtTokenUtil#getUserIdFromToken: " + e.getMessage());
        }
        if (subjectJSON != null) {
            return subjectJSON.get("user_id").asText();
        } else {
            return "";
        }
    }

    //Получение role пользователя из jwt токена
    public String getUserRoleFromToken(String token) {
        String subject = getClaimFromToken(token, Claims::getSubject);
        JsonNode subjectJSON = null;
        try {
            subjectJSON = objectMapper.readTree(subject);
        } catch (JsonProcessingException e) {
            log.error("JwtTokenUtil#getUserRoleFromToken: " + e.getMessage());
        }
        if (subjectJSON != null) {
            return subjectJSON.get("user_role").asText();
        } else {
            return "";
        }
    }

    //Получение даты истечения срока из jwt токена
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //Получение фиксированной информации из токена
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //Для получения любой информации из токена, необходим секретный ключ
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //Проверка истекло ли время действия токена
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //Генерация токена для пользователя
    public String generateToken(UserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerate(claims, customUserDetails.toString());
    }

    private String doGenerate(Map<String, Object> claims,
                              String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //Подтверждение токена
    public Boolean validateToken(String token,
                                 UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
