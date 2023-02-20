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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.forum.server.server.base.BaseModel;
import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.payload.request.CommentRequest;
import com.forum.server.server.payload.response.CommentResponse;
import com.forum.server.server.service.CommentService;

@CrossOrigin(origins = "http://10.10.102.97:8081")
// @CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/comment")
public class CommentController extends BaseModel {
  @Autowired
  private CommentService commentService;

  // Get All Question
  // @GetMapping("")
  // public ResponseEntity<ResponAPI<Page<CommentResponse>>> getAllComment(
  //     @RequestParam(value = "search", required = false) String search,
  //     @RequestParam(value = "page", required = false) Integer page,
  //     @RequestParam(value = "limit", required = false) Integer limit,
  //     @RequestParam(value = "sortBy", required = false) List<String> sortBy,
  //     @RequestParam(value = "descending", required = false) Boolean desc) {  
  //   Page<CommentResponse> responsePage = commentService.getAllComment(search, page, limit, sortBy, desc);
  //   ResponAPI<Page<CommentResponse>> responAPI = new ResponAPI<>();
  //   responAPI.setErrorMessage(MessageApi.SUCCESS);
  //   responAPI.setErrorCode(ErrorCode.SUCCESS); 
  //   System.out.println(responsePage.get());
  //   responAPI.setData(responsePage);
  //   return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  // }
  @GetMapping("")
  public ResponseEntity<ResponAPI<Page<CommentResponse>>> getAll(
    @RequestParam(name = "page") int page,
    @RequestParam(name = "threadId", required = true) Long id 
  ) {
    Page<CommentResponse> data = commentService.getAll(page, id);
    ResponAPI<Page<CommentResponse>> responAPI = new ResponAPI<>();
    responAPI.setData(data);
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

  //Create Comment
  @PostMapping("/create")
  public ResponseEntity<ResponAPI<CommentResponse>> createComment(@RequestBody CommentRequest body) {
    ResponAPI<CommentResponse> responAPI = new ResponAPI<>();
    if(!commentService.createComment(body, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Delete Comment

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponAPI<CommentResponse>> deleteComment(@PathVariable("id") Long id) {
    ResponAPI<CommentResponse> responAPI = new ResponAPI<>();

    if(!commentService.deleteCommentById(id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }
}
