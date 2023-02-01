package com.forum.server.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.dto.request.SubRequest;
import com.forum.server.server.dto.response.DtoResListSub;
import com.forum.server.server.dto.response.SubResponse;
import com.forum.server.server.service.SubService;

@RestController
@RequestMapping("/api/sub")
public class SubForumController {
  @Autowired
  private SubService subService;

  //Get By Id
  @GetMapping("/{id}")
  public ResponseEntity<ResponAPI<DtoResListSub>> getDetailSubForum(@PathVariable("id") Long id) {
    ResponAPI<DtoResListSub> responAPI = new ResponAPI<>();
    if(!subService.getSubForumById(responAPI, id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

  // Add Sub Forum
  @PostMapping("/create")
  public ResponseEntity<ResponAPI<SubResponse>> createSubForum(@ModelAttribute SubRequest body, @RequestParam("file") MultipartFile file) {
    ResponAPI<SubResponse> responAPI = new ResponAPI<>();
    if(!subService.createSubForum(body, file, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Edit Sub Forum
  @PostMapping("/{id}")
  public ResponseEntity<ResponAPI<SubResponse>> updateSubForum(@PathVariable Long id, @RequestBody SubRequest body) {
    ResponAPI<SubResponse> responAPI = new ResponAPI<>();
    if(!subService.updateSubForum(body, id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Delete Sub Forum
  @DeleteMapping("/{id}")
  public ResponseEntity<ResponAPI<SubResponse>> deleteSubForum(@PathVariable("id") Long id) {
    ResponAPI<SubResponse> responAPI = new ResponAPI<>();
    if(!subService.deleteSubForum(id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }
}
