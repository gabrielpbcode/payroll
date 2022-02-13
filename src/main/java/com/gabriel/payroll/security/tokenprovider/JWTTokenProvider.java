package com.gabriel.payroll.security.tokenprovider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gabriel.payroll.model.Authority;
import com.gabriel.payroll.model.PayrollUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.gabriel.payroll.security.SecurityConstants.*;

@Component
public class JWTTokenProvider {

  @Value("${jwt.secret}")
  private String secret;

  // The user already passed the authentication
  public String generateJwtToken(PayrollUser user) {
    String[] claims = getClaimsFromUser(user);

    return JWT.create().withIssuer(GET_GABRIEL_LLC)
      .withAudience(GET_ARRAYS_ADMINISTRATION)
      .withIssuedAt(new Date())
      .withSubject(user.getUsername())
      .withArrayClaim(AUTHORITIES, claims)
      .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .sign(HMAC512(secret));
  }

  public List<GrantedAuthority> getAuthorities(String token) {
    String[] claims = getClaimsFromToken(token);
    return Stream.of(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken uToken = new UsernamePasswordAuthenticationToken(username,null, authorities);

    // Setup user information inside the context
    uToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    return uToken;
  }

  public boolean isTokenValid(String username, String token) {
    JWTVerifier verifier = getJWTVerifier();
    return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
  }

  public String getSubject(String token) {
    JWTVerifier verifier = getJWTVerifier();
    return verifier.verify(token).getSubject();
  }

  private boolean isTokenExpired(JWTVerifier verifier, String token) {
    Date expiration = verifier.verify(token).getExpiresAt();
    return expiration.before(new Date());
  }

  private String[] getClaimsFromToken(String token) {
    JWTVerifier verifier = getJWTVerifier();
    return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
  }

  private JWTVerifier getJWTVerifier() {
    JWTVerifier verifier;

    try {
      Algorithm algorithm = HMAC512(secret);
      verifier = JWT.require(algorithm).withIssuer(GET_GABRIEL_LLC).build();
    } catch (JWTVerificationException e) {
      throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
    }

    return verifier;
  }

  private String[] getClaimsFromUser(PayrollUser user) {
    return user.getAuthorities().stream().map(Authority::getName).toArray(String[]::new);
  }
}
