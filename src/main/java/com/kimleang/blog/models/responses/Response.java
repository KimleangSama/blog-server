package com.kimleang.blog.models.responses;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Response<T> {
  private int code;
  private T data;
  private String message;
  private Status status;

  private enum Status {
    OK, BAD_REQUEST, UNAUTHORIZED, VALIDATION_EXCEPTION, EXCEPTION, WRONG_CREDENTIALS, ACCESS_DENIED, NOT_FOUND, DUPLICATE_ENTITY
  }

  public static <T> Response<T> badRequest() {
    Response<T> response = new Response<>();
    response.setStatus(Status.BAD_REQUEST);
    return response;
  }

  public static <T> Response<T> ok(String message) {
    Response<T> response = new Response<>();
    response.setCode(200);
    response.setStatus(Status.OK);
    response.setMessage(message);
    return response;
  }

  public static <T> Response<T> unauthorized() {
    Response<T> response = new Response<>();
    response.setStatus(Status.UNAUTHORIZED);
    return response;
  }

  public static <T> Response<T> validationException() {
    Response<T> response = new Response<>();
    response.setStatus(Status.VALIDATION_EXCEPTION);
    return response;
  }

  public static <T> Response<T> wrongCredentials() {
    Response<T> response = new Response<>();
    response.setStatus(Status.WRONG_CREDENTIALS);
    return response;
  }

  public static <T> Response<T> accessDenied() {
    Response<T> response = new Response<>();
    response.setStatus(Status.ACCESS_DENIED);
    return response;
  }

  public static <T> Response<T> exception() {
    Response<T> response = new Response<>();
    response.setStatus(Status.EXCEPTION);
    return response;
  }

  public static <T> Response<T> notFound(String message) {
    Response<T> response = new Response<>();
    response.setStatus(Status.NOT_FOUND);
    response.setCode(404);
    response.setMessage(message);
    return response;
  }

  public static <T> Response<T> duplicateEntity() {
    Response<T> response = new Response<>();
    response.setStatus(Status.DUPLICATE_ENTITY);
    return response;
  }

}
