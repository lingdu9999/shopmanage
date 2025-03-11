package com.yiyayaya.shopmanage.common;

import lombok.Data;

@Data
public class Result<T> {
  private Integer code;
  private String message;
  private T data;

  public Result(Integer code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> Result<T> success(T data, String message) {
    return new Result<>(Code.Code_200, message, data);
  }

  public static <T> Result<T> success(T data) {
    return success(data, "success");
  }

  public static <T> Result<T> error(Integer code, String message) {
    return new Result<>(code, message, null);
  }

  public static <T> Result<T> error(Integer code) {
    return error(code, "error");
  }

  // Getters and Setters omitted for brevity
}
