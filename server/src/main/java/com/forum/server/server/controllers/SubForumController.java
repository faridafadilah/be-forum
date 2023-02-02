package com.forum.server.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.payload.request.SubRequest;
import com.forum.server.server.payload.response.DtoResListSub;
import com.forum.server.server.payload.response.SubResponse;
import com.forum.server.server.service.SubService;

@RestController
@RequestMapping("/api/sub")
public class SubForumController {
  @Autowired
  private SubService subService;

  //Get ALl
  @GetMapping("/")
  public ResponseEntity<ResponAPI<Page<DtoResListSub>>> getAllSurvey(
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "limit", required = false) Integer limit,
      @RequestParam(value = "sortBy", required = false) List<String>sortBy,
      @RequestParam(value = "descending", required = false) Boolean desc) {
    Page<DtoResListSub> subNotPending = subService.getAllSub(search, page, limit, sortBy, desc);
    ResponAPI<Page<DtoResListSub>> responAPI = new ResponAPI<>();
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setData(subNotPending);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

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
  @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
  public ResponseEntity<ResponAPI<SubResponse>> createSubForum(@ModelAttribute SubRequest body, @RequestParam("file") MultipartFile file) {
    ResponAPI<SubResponse> responAPI = new ResponAPI<>();
    if(!subService.createSubForum(body, file, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Edit Sub Forum
  @PostMapping("/{id}")
  @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
  public ResponseEntity<ResponAPI<SubResponse>> updateSubForum(@PathVariable Long id, @ModelAttribute SubRequest body, @RequestParam("file") MultipartFile file) {
    ResponAPI<SubResponse> responAPI = new ResponAPI<>();
    if(!subService.updateSubForum(body, file, id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Delete Sub Forum
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
  public ResponseEntity<ResponAPI<SubResponse>> deleteSubForum(@PathVariable("id") Long id) {
    ResponAPI<SubResponse> responAPI = new ResponAPI<>();
    if(!subService.deleteSubForum(id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }
}
