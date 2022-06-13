package com.example.oidc.controller.auth;

import com.example.oidc.service.ResponseService;
import com.example.oidc.service.auth.SignInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth/sign-in")
public class SignInController {

  private final ResponseService responseService;
  private final SignInService signInService;

}
