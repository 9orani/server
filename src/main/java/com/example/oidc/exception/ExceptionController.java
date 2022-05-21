package com.example.oidc.exception;

import com.example.oidc.exception.sign.CustomAuthenticationEntryPointException;
import com.example.oidc.dto.response.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {

  @GetMapping(value = "/entrypoint")
  public CommonResult entrypointException() {
    throw new CustomAuthenticationEntryPointException();
  }

  @GetMapping(value = "/accessDenied")
  public CommonResult accessDeniedException() {
    throw new AccessDeniedException("");
  }
}