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
import com.forum.server.server.dto.request.CommentRequest;
import com.forum.server.server.dto.response.CommentResponse;
import com.forum.server.server.service.CommentService;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
  @Autowired
  private CommentService commentService;

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
