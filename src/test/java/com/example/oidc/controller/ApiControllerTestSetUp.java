package com.example.oidc.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.example.oidc.config.security.JwtTokenProvider;
import com.example.oidc.exception.ExceptionAdvice;
import com.example.oidc.service.util.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public abstract class ApiControllerTestSetUp {

  /********* Repository Start ********/

  /********* Service Start ********/
  @Autowired
  protected AuthService authService;

  /********* Others Start ********/
  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext ctx;

  @Autowired
  protected PasswordEncoder passwordEncoder;

  @Autowired
  protected MessageSource messageSource;

  @Autowired
  protected ExceptionAdvice exceptionAdvice;

  @Autowired
  protected JwtTokenProvider jwtTokenProvider;

  @BeforeEach
  public void setUpAll(RestDocumentationContextProvider restDocumentation) throws Exception {
    // mockMvc의 한글 사용을 위한 코드
    this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
        .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
        .apply(springSecurity())
        .apply(documentationConfiguration(restDocumentation)
            .operationPreprocessors()
            .withRequestDefaults(modifyUris().host("test.com").removePort(), prettyPrint())
            .withResponseDefaults(prettyPrint())
        )
        .build();
  }
}
