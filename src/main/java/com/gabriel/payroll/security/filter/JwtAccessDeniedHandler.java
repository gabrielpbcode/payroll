package com.gabriel.payroll.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.payroll.dto.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static com.gabriel.payroll.security.SecurityConstants.ACCESS_DENIED_MESSAGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

//This is a filter
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e)
    throws IOException, ServletException {
    HttpStatus status = UNAUTHORIZED;
    HttpResponse response = new HttpResponse(status.value(), status, status.getReasonPhrase(), ACCESS_DENIED_MESSAGE);

    res.setContentType(APPLICATION_JSON_VALUE);
    res.setStatus(status.value());

    OutputStream outputStream = res.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(outputStream, response);
    outputStream.flush();
  }
}
