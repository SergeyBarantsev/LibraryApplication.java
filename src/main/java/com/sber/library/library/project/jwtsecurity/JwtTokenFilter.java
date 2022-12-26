package com.sber.library.library.project.jwtsecurity;

import com.sber.library.library.project.services.userDetails.CustomUserDetailsService;
import io.jsonwebtoken.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService authenticationService;

    @Getter
    private String token;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //Обнуление токена перед фильтрацией от предыдущей авторизации
        token = null;
        //Получение заголовка авторизации и подтверждение
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        //Получаем JWT Token
        //Authorization: Bearer 123gfskjhesfsmsfdsgjrhselgkjfsrlkjartfgergfsgfgsg
        token = header.split(" ")[1].trim();
        //Получить пользователя
        UserDetails userDetails;
        try {
            userDetails = authenticationService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
        } catch (SignatureException exception) {
            log.error("JwtTokenFilter#doFilterInternal(): " + exception.getMessage());
            return;
        }
        //Подтверждение токена
        if (!jwtTokenUtil.validateToken(token, userDetails)) {
            filterChain.doFilter(request, response);
            return;
        }
        //Установка сущности пользователя на spring security context
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
