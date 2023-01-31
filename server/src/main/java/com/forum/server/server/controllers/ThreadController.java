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
import com.forum.server.server.dto.request.ThreadRequest;
import com.forum.server.server.dto.response.DtoResListThread;
import com.forum.server.server.dto.response.ThreadResponse;
import com.forum.server.server.service.ThreadService;

@RestController
@RequestMapping("/api/thread")
public class ThreadController {
  @Autowired
  private ThreadService threadService;

  //Get By Id
  @GetMapping("/{id}")
  public ResponseEntity<ResponAPI<DtoResListThread>> getDetailThread(@PathVariable("id") Long id) {
    ResponAPI<DtoResListThread> responAPI = new ResponAPI<>();
    if(!threadService.getThreadById(responAPI, id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

  //Create Thread
  @PostMapping("/create")
  public ResponseEntity<ResponAPI<ThreadResponse>> createThread(@RequestBody ThreadRequest body) {
    ResponAPI<ThreadResponse> responAPI = new ResponAPI<>();
    if(!threadService.createThread(body, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  // Edit Thread
  @PostMapping("/{id}")
  public ResponseEntity<ResponAPI<ThreadResponse>> updateThread(@PathVariable("id") Long id, @RequestBody ThreadRequest body) {
    ResponAPI<ThreadResponse> responAPI = new ResponAPI<>();
    if(!threadService.updateThread(body, id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponAPI<ThreadResponse>> deleteThread(@PathVariable("id") Long id) {
    ResponAPI<ThreadResponse> responAPI = new ResponAPI<>();

    if(!threadService.deleteThreadById(id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

}
