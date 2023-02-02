package com.forum.server.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.forum.server.server.base.BaseResponsePage;
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

  // Get All Thread
  @GetMapping("/")
  public ResponseEntity<ResponAPI<BaseResponsePage>> getAllThread(
    @RequestParam(value = "search", required = false) String search,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "limit", required = false) Integer limit,
    @RequestParam(value = "sortBy", required = false) List<String>sortBy,
    @RequestParam(value = "descending", required = false) Boolean desc
  ) {
    Page<DtoResListThread> threadNotPending = threadService.getAllThread(search, page, limit, sortBy, desc);
    ResponAPI<BaseResponsePage> responAPI = new ResponAPI<>();
    BaseResponsePage<List<DtoResListThread>> basePageT = new BaseResponsePage<>();
    basePageT.setContent(threadNotPending.toList());
    basePageT.setCurrentPage(threadNotPending.getPageable().getPageNumber());
    basePageT.setTotalPage(threadNotPending.getPageable().getPageSize());
    basePageT.setTotalElement(threadNotPending.getTotalElements());
    responAPI.setData(basePageT);
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI); 
  }
  
  //Get By Id
  @GetMapping("/{id}")
  public ResponseEntity<ResponAPI<DtoResListThread>> getDetailThread(@PathVariable("id") Long id) {
    ResponAPI<DtoResListThread> responAPI = new ResponAPI<>();
    if (!threadService.getThreadById(responAPI, id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

  // Create Thread
  @PostMapping("/create")
  public ResponseEntity<ResponAPI<ThreadResponse>> createThread(@ModelAttribute ThreadRequest body,
      @RequestParam("file") MultipartFile file) {
    ResponAPI<ThreadResponse> responAPI = new ResponAPI<>();
    if (!threadService.createThread(body, file, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  // Edit Thread
  @PostMapping("/{id}")
  public ResponseEntity<ResponAPI<ThreadResponse>> updateThread(@PathVariable("id") Long id,
      @ModelAttribute ThreadRequest body, @RequestParam("file") MultipartFile file) {
    ResponAPI<ThreadResponse> responAPI = new ResponAPI<>();
    if (!threadService.updateThread(body, file, id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponAPI<ThreadResponse>> deleteThread(@PathVariable("id") Long id) {
    ResponAPI<ThreadResponse> responAPI = new ResponAPI<>();

    if (!threadService.deleteThreadById(id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

}
