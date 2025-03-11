package com.yiyayaya.shopmanage.exception;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException {
  private final Integer code = 500;

  public ServiceException(String message) {
    super(message);
  }

}
