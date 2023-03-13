package com.forum.server.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.forum.server.server.base.BaseModel;
import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.payload.request.ThreadRequest;
import com.forum.server.server.payload.response.DtoResListThread;
import com.forum.server.server.payload.response.LikeResponse;
import com.forum.server.server.payload.response.ThreadResponse;
import com.forum.server.server.service.ThreadService;

// @CrossOrigin(origins = "http://10.10.102.90:8081")
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/thread")
public class ThreadController extends BaseModel {
  @Autowired
  private ThreadService threadService;

  // // Get All Thread
  // @GetMapping("")
  // public ResponseEntity<ResponAPI<Page<DtoResListThread>>>  getAllThread(
  //   @RequestParam(value = "search", required = false) String search,
  //   @RequestParam(value = "page", required = false) Integer page,
  //   @RequestParam(value = "limit", required = false) Integer limit,
  //   @RequestParam(value = "sortBy", required = false) List<String>sortBy,
  //   @RequestParam(value = "descending", required = false) Boolean desc
  // ) {
  //   Page<DtoResListThread> threadNotPending = threadService.getAllThread(search, page, limit, sortBy, desc);
  //   ResponAPI<Page<DtoResListThread>> responAPI = new ResponAPI<>();
  //   responAPI.setData(threadNotPending);
  //   responAPI.setErrorCode(ErrorCode.SUCCESS);
  //   responAPI.setErrorMessage(MessageApi.SUCCESS);
  //   return ResponseEntity.status(HttpStatus.OK).body(responAPI); 
  // }

  @GetMapping("")
  public ResponseEntity<ResponAPI<Page<DtoResListThread>>> getAll(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "limit", defaultValue = "5") int limit,
    @RequestParam(name = "subforumId", required = true) Long id 
  ) {
    Page<DtoResListThread> data = threadService.getAll(page, limit, id);
    ResponAPI<Page<DtoResListThread>> responAPI = new ResponAPI<>();
    responAPI.setData(data);
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<ResponAPI<Page<DtoResListThread>>> getByUserId(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "limit", defaultValue = "5") int limit,
    @PathVariable("id") Long id
  ) {
    Page<DtoResListThread> data = threadService.getByUserId(page, limit, id);
    ResponAPI<Page<DtoResListThread>> responAPI = new ResponAPI<>();
    responAPI.setData(data);
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

  // @GetMapping("/user/{id}")
  // public ResponseEntity<ResponAPI<Page<DtoResListThread>>> getByUserId(
  //           @RequestParam(name = "page", defaultValue= "1") int page,
  //           @PathVariable(name = "id") long id,
  //           @RequestParam(name = "limit", defaultValue = "5") int limit) {
  //       Page<DtoResListThread> data = threadService.getAllByIdUser(page, limit, id);
  //       ResponAPI<Page<DtoResListThread>> responAPI = new ResponAPI<>();
  //       responAPI.setErrorMessage(null);
  //       responAPI.setErrorCode(ErrorCode.SUCCESS);
  //       responAPI.setData(data);
  //       return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  //   }

  // Create Thread
  @PostMapping("/create")
  public ResponseEntity<ResponAPI<ThreadResponse>> createThread(@ModelAttribute ThreadRequest body,
      @RequestParam(value = "file", required = false) MultipartFile file) {
    ResponAPI<ThreadResponse> responAPI = new ResponAPI<>();
    if (!threadService.createThread(body, file, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  // Edit Thread
  @PostMapping("/{id}")
  public ResponseEntity<ResponAPI<ThreadResponse>> updateThread(@PathVariable("id") Long id,
      @ModelAttribute ThreadRequest body, @RequestParam(value = "file", required = false) MultipartFile file) {
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

  @PostMapping("like/{id}")
  public ResponseEntity<ResponAPI<ThreadResponse>> addLikeThread(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
    ResponAPI<ThreadResponse> responAPI = new ResponAPI<>();

    if (!threadService.addLikeThreadById(id, responAPI, userId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  @DeleteMapping("unlike/{id}")
  public ResponseEntity<ResponAPI<ThreadResponse>> unLikeThread(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
    ResponAPI<ThreadResponse> responAPI = new ResponAPI<>();

    if (!threadService.unLikeThreadById(id, responAPI, userId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Get like By userId
  @GetMapping("like/user/{id}")
  public ResponseEntity<ResponAPI<List<LikeResponse>>> getLikeByUser(@PathVariable("id") Long id) {
    List<LikeResponse> data = threadService.getLikeByUserId(id);
    ResponAPI<List<LikeResponse>> responAPI = new ResponAPI<>();
    responAPI.setData(data);
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

   //Get Populer Thread
   @GetMapping("populer")
   public ResponseEntity<ResponAPI<List<DtoResListThread>>> getPopulerThread() {
     List<DtoResListThread> data = threadService.getPopularThreads();
     ResponAPI<List<DtoResListThread>> responAPI = new ResponAPI<>();
     responAPI.setData(data);
     responAPI.setErrorCode(ErrorCode.SUCCESS);
     responAPI.setErrorMessage(MessageApi.SUCCESS);
     return ResponseEntity.status(HttpStatus.OK).body(responAPI);
   }

}
