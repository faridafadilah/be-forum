package com.forum.server.server.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.dto.request.ThreadRequest;
import com.forum.server.server.dto.response.DtoResListThread;
import com.forum.server.server.dto.response.ThreadResponse;
import com.forum.server.server.models.Thread;
import com.forum.server.server.repository.ThreadRepository;

import javax.validation.ValidationException;

@Service
public class ThreadService {
  @Autowired
  private ThreadRepository threadRepository;
  private ModelMapper objectMapper = new ModelMapper();

  public boolean getThreadById(ResponAPI<DtoResListThread> responAPI, Long id) {
    Optional<Thread> optionalThread = threadRepository.findById(id);
    if(!optionalThread.isPresent()) {
      responAPI.setErrorMessage("Data tidak ditemukan!");
      return false;
    }

    try {
      DtoResListThread response = DtoResListThread.getInstance(optionalThread.get());
      responAPI.setData(response);
    } catch (Exception e) {
      responAPI.setErrorMessage(e.getMessage());
    }
    return true;
  }

  public boolean createThread(ThreadRequest body, ResponAPI<ThreadResponse> responAPI) {
    try {
      Thread thread = objectMapper.map(body, Thread.class);
      threadRepository.save(thread);

      responAPI.setData(mapToThreadResponse(thread));
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

  public boolean updateThread(ThreadRequest body, Long id, ResponAPI<ThreadResponse> responAPI) {
    Optional<Thread> tOptional = threadRepository.findById(id);
    if(!tOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }
    try {
      Thread thread = tOptional.get();
      thread.setId(id);
      thread.setTitle(body.getTitle());
      thread.setContent(body.getContent());
      threadRepository.save(thread);

      responAPI.setData(mapToThreadResponse(thread));
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (ValidationException e) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(e.getMessage());
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean deleteThreadById(Long id, ResponAPI<ThreadResponse> responAPI) {
    Optional<Thread> tOptional = threadRepository.findById(id);
    if(!tOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
    }

    try {
      Thread thread = tOptional.get();
      threadRepository.deleteById(id);

      responAPI.setData(mapToThreadResponse(thread));
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

  private ThreadResponse mapToThreadResponse(Thread thread) {
    return objectMapper.map(thread, null);
  }
  
}
