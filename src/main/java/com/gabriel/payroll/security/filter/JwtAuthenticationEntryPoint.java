package com.gabriel.payroll.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.payroll.dto.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static com.gabriel.payroll.security.SecurityConstants.FORBIDDEN_MESSAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

//This is a filter from the filter chain
@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {
  @Override
  public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e)
    throws IOException {
    HttpStatus status = FORBIDDEN;
    HttpResponse response = new HttpResponse(status.value(), status, status.getReasonPhrase(), FORBIDDEN_MESSAGE);

    res.setContentType(APPLICATION_JSON_VALUE);
    res.setStatus(status.value());

    OutputStream outputStream = res.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();

    mapper.writeValue(outputStream, response);
    outputStream.flush();
  }
}
