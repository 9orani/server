package com.example.oidc.controller;

import static com.example.oidc.config.security.JwtTokenProvider.tokenValidMillisecond;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import com.example.oidc.entity.PlayerEntity;
import com.example.oidc.entity.RoomEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

public class ApiControllerTestHelper extends ApiControllerTestSetUp {

  public enum ResponseType {
    SINGLE("data"),
    LIST("list[]"),
    PAGE("page.content[]");

    private final String responseFieldPrefix;

    ResponseType(String responseFieldPrefix) {
      this.responseFieldPrefix = responseFieldPrefix;
    }

    public String getResponseFieldPrefix() {
      return responseFieldPrefix;
    }
  }

  protected String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final String jsonContent = mapper.writeValueAsString(obj);
      return jsonContent;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public PlayerEntity generatePlayerEntity() {
    String uuid = UUID.randomUUID().toString().substring(0, 10);
    return playerRepository.save(
        PlayerEntity.builder()
            .loginId("loginId_" + uuid)
            .password("password_" + uuid)
            .username("username_" + uuid)
            .emailAddress("emailAddress_" + uuid)
            .registerTime(LocalDateTime.now())
            .recentLoginTime(LocalDateTime.now())
            .build());
  }

  public String getToken(PlayerEntity player) {
    return jwtTokenProvider.createToken(String.valueOf(player.getId()),
        List.of("ROLE_USER"), tokenValidMillisecond);
  }

  public RoomEntity generateRoomEntity(PlayerEntity creator, Long maxPlayer) {
    String uuid = UUID.randomUUID().toString().substring(0, 10);
    return roomRepository.save(
        RoomEntity.builder()
            .name("name_" + uuid)
            .visitCode(uuid)
            .createTime(LocalDateTime.now())
            .maxPlayer(maxPlayer)
            .creatorPlayerEntity(creator)
            .build());
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
        parameterWithName("page").optional().description("????????? ??????(default = 0)"),
        parameterWithName("size").optional().description(sizeDescription)
    ));
    if (descriptors.length > 0) {
      commonFields.addAll(Arrays.asList(descriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generateSignUpRequestFields(FieldDescriptor... addDescriptors) {
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(Arrays.asList(
        fieldWithPath("loginId").description("????????? ?????????"),
        fieldWithPath("emailAddress").description("????????? ??????"),
        fieldWithPath("password").description("????????? ????????????"),
        fieldWithPath("username").description("?????????")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generateSignInRequestFields(FieldDescriptor... addDescriptors) {
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(Arrays.asList(
        fieldWithPath("loginId").description("????????? ?????????"),
        fieldWithPath("password").description("????????? ????????????")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generatePlayerDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + ".id").description("?????? ?????? ID"),
        fieldWithPath(prefix + ".loginId").description("????????? ?????????"),
        fieldWithPath(prefix + ".emailAddress").description("????????? ??????"),
        fieldWithPath(prefix + ".username").description("?????????")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generateAuthDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + ".id").description("?????? ?????? ID"),
        fieldWithPath(prefix + ".loginId").description("????????? ?????????"),
        fieldWithPath(prefix + ".emailAddress").description("????????? ??????"),
        fieldWithPath(prefix + ".username").description("?????????"),
        fieldWithPath(prefix + ".token").description("JWT ??????")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generatePlayerDetailDtoResponseFields(ResponseType type,
      String success, String code, String msg, String dtoName, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + dtoName + ".id").description("?????? ?????? ID"),
        fieldWithPath(prefix + dtoName + ".loginId").description("????????? ?????????"),
        fieldWithPath(prefix + dtoName + ".emailAddress").description("????????? ??????"),
        fieldWithPath(prefix + dtoName + ".username").description("?????????"),
        fieldWithPath(prefix + dtoName + ".registerTime").description("?????? ??????"),
        fieldWithPath(prefix + dtoName + ".recentLoginTime").description("?????? ????????? ??????"),
        fieldWithPath(prefix + dtoName + ".createRoomList").description("????????? ??? ??????"),
        fieldWithPath(prefix + dtoName + ".joinRoomList").description("????????? ??? ??????")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generatePlayerDetailDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    return generatePlayerDetailDtoResponseFields(type, success, code, msg, "", addDescriptors);
  }

  public List<FieldDescriptor> generateRoomDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + ".id").description("??? ?????? ID"),
        fieldWithPath(prefix + ".name").description("??? ??????"),
        fieldWithPath(prefix + ".visitCode").description("??? ?????? ??????"),
        fieldWithPath(prefix + ".maxPlayer").description("?????? ???????????? ???")
    ));
    if (type.equals(ResponseType.PAGE)) {
      commonFields.addAll(Arrays.asList(
          fieldWithPath("page.empty").description("???????????? ????????? ??? ??????"),
          fieldWithPath("page.first").description("??? ????????? ??????"),
          fieldWithPath("page.last").description("????????? ????????? ??????"),
          fieldWithPath("page.number").description("????????? ?????? ??? ????????? ?????? (0?????? ??????)"),
          fieldWithPath("page.numberOfElements").description("?????? ??????"),
          subsectionWithPath("page.pageable").description("?????? ???????????? ?????? DB ??????"),
          fieldWithPath("page.size").description("????????? ????????? ??????"),
          subsectionWithPath("page.sort").description("????????? ?????? ??????"),
          fieldWithPath("page.totalElements").description("??? ?????? ??????"),
          fieldWithPath("page.totalPages").description("??? ?????????")));
    }
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }

  public List<FieldDescriptor> generateRoomDetailDtoResponseFields(ResponseType type,
      String success, String code, String msg, FieldDescriptor... addDescriptors) {
    String prefix = type.getResponseFieldPrefix();
    List<FieldDescriptor> commonFields = new ArrayList<>();
    commonFields.addAll(generateCommonResponseFields(success, code, msg));
    commonFields.addAll(generateRoomDtoResponseFields(type, success, code, msg));
    commonFields.addAll(Arrays.asList(
        fieldWithPath(prefix + ".createTime").description("????????? ??????"),
        subsectionWithPath(prefix + ".creatorPlayer").description("????????? ????????????"),
        subsectionWithPath(prefix + ".joinPlayerList").description("????????? ???????????? ?????????")
    ));
    if (addDescriptors.length > 0) {
      commonFields.addAll(Arrays.asList(addDescriptors));
    }
    return commonFields;
  }
}