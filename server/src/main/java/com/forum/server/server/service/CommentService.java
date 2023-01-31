package com.forum.server.server.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.dto.request.CommentRequest;
import com.forum.server.server.dto.response.CommentResponse;
import com.forum.server.server.models.Comment;
import com.forum.server.server.repository.CommentRepository;

import javax.validation.ValidationException;

@Service
public class CommentService {
  @Autowired
  private CommentRepository commentRepository;
  private ModelMapper objectMapper = new ModelMapper();

  public boolean createComment(CommentRequest body, ResponAPI<CommentResponse> responAPI) {
    try {
      Comment comment = objectMapper.map(body, Comment.class);
      commentRepository.save(comment);
  
      responAPI.setData(mapToCommentResponse(comment));
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (ValidationException e) {
      responAPI.setErrorMessage(e.getMessage());
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean deleteCommentById(Long id, ResponAPI<CommentResponse> responAPI) {
    Optional<Comment> cOptional = commentRepository.findById(id);
    if(!cOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
    }

    try {
      Comment comment = cOptional.get();
      commentRepository.deleteById(id);

      responAPI.setData(mapToCommentResponse(comment));
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

  private CommentResponse mapToCommentResponse(Comment comment) {
    return objectMapper.map(comment, CommentResponse.class);
  }
  
}