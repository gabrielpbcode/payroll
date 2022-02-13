package com.gabriel.payroll.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse {
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "America/Sao_Paulo")
  private Date timestamp;

  private int httpStatusCode;

  private HttpStatus httpStatus;

  private String reason;

  private String message;

  public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
    this.timestamp = new Date();
    this.httpStatusCode = httpStatusCode;
    this.httpStatus = httpStatus;
    this.reason = reason;
    this.message = message;
  }
}
