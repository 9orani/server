package com.example.oidc.exception;

import com.example.oidc.dto.response.CommonResult;
import com.example.oidc.exception.player.CustomPlayerNotFoundException;
import com.example.oidc.exception.room.CustomEmptyRoomIsNotExistException;
import com.example.oidc.exception.room.CustomRoomNotFoundException;
import com.example.oidc.exception.room.CustomRoomOvercapacityException;
import com.example.oidc.exception.sign.CustomAuthenticationEntryPointException;
import com.example.oidc.exception.sign.CustomLoginIdSigninFailedException;
import com.example.oidc.exception.sign.CustomSignUpFailedException;
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
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public CommonResult signUpFailedException(HttpServletRequest request,
      CustomSignUpFailedException e) {
    return responseService.getFailResult(Integer.parseInt(getMessage("signUpFailed.code")),
        e.getMessage() == null ? getMessage("signUpFailed.msg") : e.getMessage());
  }

  @ExceptionHandler(CustomPlayerNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public CommonResult playerNotFoundException(HttpServletRequest request,
      CustomPlayerNotFoundException e) {
    return responseService.getFailResult(Integer.parseInt(getMessage("playerNotFound.code")),
        e.getMessage() == null ? getMessage("playerNotFound.msg") : e.getMessage());
  }

  @ExceptionHandler(CustomEmptyRoomIsNotExistException.class)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public CommonResult emptyRoomIsNotExistException(CustomEmptyRoomIsNotExistException e) {
    return responseService.getSuccessResult(
        Integer.parseInt(getMessage("emptyRoomIsNotExist.code")),
        e.getMessage() == null ? getMessage("emptyRoomIsNotExist.msg") : e.getMessage());
  }

  @ExceptionHandler(CustomRoomNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public CommonResult roomNotFoundException(CustomRoomNotFoundException e) {
    return responseService.getFailResult(
        Integer.parseInt(getMessage("roomNotFound.code")),
        e.getMessage() == null ? getMessage("roomNotFound.msg") : e.getMessage());
  }

  @ExceptionHandler(CustomRoomOvercapacityException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public CommonResult overcapacityException(CustomRoomOvercapacityException e) {
    return responseService.getFailResult(
        Integer.parseInt(getMessage("overcapacity.code")),
        e.getMessage() == null ? getMessage("overcapacity.msg") : e.getMessage());
  }
}