package com.example.oidc.controller;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

public class ApiControllerTestHelper extends ApiControllerTestSetUp {

  public enum ResponseType {
    SINGLE("data"),
    LIST("list[]");

    private final String reponseFieldPrefix;

    ResponseType(String reponseFieldPrefix) {
      this.reponseFieldPrefix = reponseFieldPrefix;
    }

    public String getReponseFieldPrefix() {
      return reponseFieldPrefix;
    }
  }

  public List<FieldDescriptor> generateCommonResponseFields(String docSuccess, String docCode,
      String docMsg) {
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(Arrays.asList(
        fieldWithPath("success").description(docSuccess),
        fieldWithPath("code").description(docCode),
        fieldWithPath("msg").description(docMsg)));
    return commonFields;
  }

  public List<ParameterDescriptor> generateCommonPagingParameters(String sizeDescription,
      ParameterDescriptor... descriptors) {
    List<ParameterDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(Arrays.asList(
        parameterWithName("page").optional().description("페이지 번호(default = 0)"),
        parameterWithName("size").optional().description(sizeDescription)
    ));
    if (descriptors.length > 0) {
      commonFields.addAll(Arrays.asList(descriptors));
    }
    return commonFields;
  }
}