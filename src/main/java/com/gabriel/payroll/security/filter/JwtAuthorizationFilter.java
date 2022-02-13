package com.gabriel.payroll.security.filter;

import com.gabriel.payroll.security.tokenprovider.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.gabriel.payroll.security.SecurityConstants.OPTIONS_HTTP_METHOD;
import static com.gabriel.payroll.security.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JWTTokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
    throws ServletException, IOException {

    if (req.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
      res.setStatus(OK.value());
    } else {
      String authorizationHeader = req.getHeader(AUTHORIZATION);

      if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
        chain.doFilter(req, res);
        return;
      }

      String token = authorizationHeader.substring(TOKEN_PREFIX.length());
      String username = tokenProvider.getSubject(token);

      if (tokenProvider.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {
        List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
        Authentication authentication = tokenProvider.getAuthentication(username, authorities, req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        SecurityContextHolder.clearContext();
      }
    }

    chain.doFilter(req, res);
  }
}
