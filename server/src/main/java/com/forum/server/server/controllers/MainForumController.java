package com.forum.server.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.dto.request.MainRequest;
import com.forum.server.server.dto.response.DtoResListMain;
import com.forum.server.server.dto.response.MainResponse;
import com.forum.server.server.service.MainService;


@RestController
@RequestMapping("/api/main")
public class MainForumController {
  @Autowired
  private MainService mainService;

  //Get Main Forum By Id
  @GetMapping("/{id}")
  public ResponseEntity<ResponAPI<DtoResListMain>> getDetailMainForum(@PathVariable("id") Long id) {
    ResponAPI<DtoResListMain> responAPI = new ResponAPI<>();
    if(!mainService.getMainForumById(responAPI, id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

  //Create Main Forum
  @PostMapping("/create")
  public ResponseEntity<ResponAPI<MainResponse>> createMainForum(@RequestBody MainRequest body) {
    ResponAPI<MainResponse> responAPI = new ResponAPI<>();
    if(!mainService.createMainForum(body, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Edit Main Forum
  @PostMapping("/{id}")
  public ResponseEntity<ResponAPI<MainResponse>> updateMainForum(@PathVariable Long id, @RequestBody MainRequest body) {
    ResponAPI<MainResponse> responAPI = new ResponAPI<>();
    if(!mainService.updateMainForum(body, id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Delete Main Forum
  @DeleteMapping("/{id}")
  public ResponseEntity<ResponAPI<MainResponse>> deleteMainForum(@PathVariable Long id) {
    ResponAPI<MainResponse> responAPI = new ResponAPI<>();
    if(!mainService.deleteMainForum(id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }
}
