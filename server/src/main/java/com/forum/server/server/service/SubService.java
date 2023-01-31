package com.forum.server.server.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.dto.request.SubRequest;
import com.forum.server.server.dto.response.DtoResListSub;
import com.forum.server.server.dto.response.SubResponse;
import com.forum.server.server.models.SubForum;
import com.forum.server.server.repository.SubForumRepository;

@Service
public class SubService {
  @Autowired
  private SubForumRepository subForumRepository;
  private ModelMapper objectMapper = new ModelMapper();

  public boolean createSubForum(SubRequest body, ResponAPI<SubResponse> responAPI) {
  try {
    SubForum subForum = objectMapper.map(body, SubForum.class);
    subForumRepository.save(subForum);

    responAPI.setData(mapToSubResponse(subForum));
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

  public boolean updateSubForum(SubRequest body, Long id, ResponAPI<SubResponse> responAPI) {
    Optional<SubForum> sOptional = subForumRepository.findById(id);
    if(!sOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    try {
      SubForum subForum = sOptional.get();
      subForum.setId(id);
      subForum.setJudul(body.getJudul());
      subForum.setDescription(body.getDescription());
      subForumRepository.save(subForum);

      responAPI.setData(mapToSubResponse(subForum));
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

  public boolean deleteSubForum(Long id, ResponAPI<SubResponse> responAPI) {
    Optional<SubForum> sOptional = subForumRepository.findById(id);
    if(!sOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
    }

    try {
      SubForum subForum = sOptional.get();
      subForumRepository.deleteById(id);

      responAPI.setData(mapToSubResponse(subForum));
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean getSubForumById(ResponAPI<DtoResListSub> responAPI, Long id) {
    Optional<SubForum> optionalSub = subForumRepository.findById(id);
    if(!optionalSub.isPresent()) {
      responAPI.setErrorMessage("Data tidak ditemukan!");
      return false;
    }
    
    try {
      DtoResListSub response = DtoResListSub.getInstance(optionalSub.get());
      responAPI.setData(response);
    } catch (Exception e) {
      responAPI.setErrorMessage(e.getMessage());
    }
    return true;
  }

  private SubResponse mapToSubResponse(SubForum subForum) {
    return objectMapper.map(subForum, SubResponse.class);
  }
}
