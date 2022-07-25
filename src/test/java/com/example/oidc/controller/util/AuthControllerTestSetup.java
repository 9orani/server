package com.example.oidc.controller.util;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.example.oidc.controller.ApiControllerTestHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;

public class AuthControllerTestSetup extends ApiControllerTestHelper {

  protected List<FieldDescriptor> generateTokenValidResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(List.of(
        fieldWithPath(prefix + ".valid").description("유효하면 true\n유효하지 않으면 false"),
        fieldWithPath(prefix + ".tokenMsg").description("유효하면 성공하였습니다.\n유효하지 않으면 유효하지 않은 이유 반환"),
        fieldWithPath(prefix + ".token").description("사용자가 보낸 token")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  protected List<FieldDescriptor> generateGetRolesResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(List.of(
        fieldWithPath(prefix).description("header로 보낸 JWT의 ROLES")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }
}
