package com.example.oidc.exception;

import com.example.oidc.exception.sign.CustomAuthenticationEntryPointException;
import com.example.oidc.exception.sign.CustomLoginIdSigninFailedException;
import com.example.oidc.exception.sign.CustomSignUpFailedException;
import com.example.oidc.dto.response.CommonResult;
import com.example.oidc.service.ResponseService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

  private final ResponseService responseService;
  private final MessageSource messageSource;

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected CommonResult defaultException(HttpServletRequest request, Exception e) {
    // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
    return responseService.getFailResult(Integer.parseInt(getMessage("unKnown.code")),
        getMessage("unKnown.msg"));
  }

  // code정보에 해당하는 메시지를 조회합니다.
  public String getMessage(String code) {
    return getMessage(code, null);
  }

  // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
  public String getMessage(String code, Object[] args) {
    return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
  }

  // ExceptionAdvice

  @ExceptionHandler(CustomAuthenticationEntryPointException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public CommonResult authenticationEntryPointException(HttpServletRequest request,
      CustomAuthenticationEntryPointException e) {
    return responseService.getFailResult(Integer.parseInt(getMessage("entryPointException.code")),
        getMessage("entryPointException.msg"));
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
    return responseService.getFailResult(Integer.parseInt(getMessage("accessDenied.code")),
        getMessage("accessDenied.msg"));
  }

  @ExceptionHandler(CustomLoginIdSigninFailedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  protected CommonResult loginIdSignInFailed(HttpServletRequest request,
      CustomLoginIdSigninFailedException e) {
    return responseService.getFailResult(Integer.parseInt(getMessage("signInFailed.code")),
        e.getMessage() == null ? getMessage("signInFailed.msg") : e.getMessage());
  }

  @ExceptionHandler(CustomSignUpFailedException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public CommonResult signUpFailedException(HttpServletRequest request,
      CustomSignUpFailedException e) {
    return responseService.getFailResult(Integer.parseInt(getMessage("signUpFailed.code")),
        e.getMessage() == null ? getMessage("signUpFailed.msg") : e.getMessage());
  }
}