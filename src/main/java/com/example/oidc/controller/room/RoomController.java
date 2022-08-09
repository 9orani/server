package com.example.oidc.controller.room;

import com.example.oidc.dto.response.CommonResult;
import com.example.oidc.dto.response.PageResult;
import com.example.oidc.dto.response.SingleResult;
import com.example.oidc.dto.room.RoomDetailDto;
import com.example.oidc.dto.room.RoomDto;
import com.example.oidc.service.ResponseService;
import com.example.oidc.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/rooms")
public class RoomController {

  private final ResponseService responseService;
  private final RoomService roomService;

  @Secured("ROLE_USER")
  @GetMapping
  public PageResult<RoomDto> getRoomPage(
      @PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable) {
    return responseService.getSuccessPageResult(roomService.getRoomPage(pageable));
  }

  @Secured("ROLE_USER")
  @PostMapping
  public SingleResult<RoomDetailDto> createRoom(@RequestBody RoomDetailDto roomInfo) {
    return responseService.getSuccessSingleResult(roomService.createRoom(roomInfo));
  }

  @Secured("ROLE_USER")
  @GetMapping("/{visitCode}")
  public SingleResult<String> getEnterUrl(@PathVariable String visitCode) {
    return responseService.getSuccessSingleResult(roomService.getEnterUrl(visitCode));
  }

  @DeleteMapping("/port/{port}")
  public CommonResult leaveRoom(@PathVariable Integer port) {
    return roomService.leaveRoom(port);
  }

  @PostMapping("/port/{port}")
  public CommonResult joinRoom(@PathVariable Integer port) {
    return roomService.joinRoom(port);
  }
}
