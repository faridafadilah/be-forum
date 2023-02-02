package com.forum.server.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.forum.server.server.base.BasePageInterface;
import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.models.Comment;
import com.forum.server.server.repository.CommentRepository;
import com.forum.server.server.repository.ThreadRepository;
import com.forum.server.server.specification.CommentSpecification;
import com.forum.server.server.models.Thread;
import com.forum.server.server.payload.request.CommentRequest;
import com.forum.server.server.payload.response.CommentResponse;

import javax.validation.ValidationException;

@Service
public class CommentService implements BasePageInterface<Comment, CommentSpecification, CommentResponse, String> {
  @Autowired
  private CommentSpecification specification;

  @Autowired
  private ThreadRepository threadRepository;

  @Autowired
  private CommentRepository commentRepository;
  private ModelMapper objectMapper = new ModelMapper();

  public boolean createComment(CommentRequest body, ResponAPI<CommentResponse> responAPI) {
    try {
      Thread threads = findThreadId(body.getThreadId());
      if(threads == null) {
        responAPI.setErrorCode(ErrorCodeApi.FAILED);
        responAPI.setErrorMessage("Thread Not Found with Id= " + body.getThreadId());
        return false;
      }

      Comment comment = new Comment();
      comment.setContent(body.getContent());
      comment.setThread(threads);
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

  private Thread findThreadId(Long threadId) {
    Optional<Thread> forum = threadRepository.findById(threadId);
    return forum.isPresent() ? forum.get() : null;
  }

  public boolean deleteCommentById(Long id, ResponAPI<CommentResponse> responAPI) {
    Optional<Comment> cOptional = commentRepository.findById(id);
    if (!cOptional.isPresent()) {
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

  public Page<CommentResponse> getAllComment(String search, Integer page, Integer limit, List<String> sortBy,
      Boolean desc) {
        sortBy = (sortBy != null) ? sortBy : Arrays.asList("id");
        desc = (desc != null) ? desc : true;
        Pageable pageableRequest = this.defaultPage(search, page, limit, sortBy, desc);
        Page<Comment> settingPage = commentRepository.findAll(this.defaultSpec(search, specification), pageableRequest);
        List<Comment> comments = settingPage.getContent();
        List<CommentResponse> responseList = comments.stream().map(a -> objectMapper.map(a, CommentResponse.class)).collect(Collectors.toList());
        Page<CommentResponse> response = new PageImpl<>(responseList, pageableRequest, settingPage.getTotalElements());
        return response;
  }

}
