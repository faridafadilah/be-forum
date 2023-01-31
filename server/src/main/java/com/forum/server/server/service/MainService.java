package com.forum.server.server.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forum.server.server.base.BasePageInterface;
import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.dto.request.MainRequest;
import com.forum.server.server.dto.response.DtoResListMain;
import com.forum.server.server.dto.response.MainResponse;
import com.forum.server.server.models.MainForum;
import com.forum.server.server.repository.MainForumRepository;
import com.forum.server.server.specification.MainSpecification;

import javax.validation.ValidationException;

@Service
public class MainService implements BasePageInterface<MainForum, MainSpecification, MainResponse, Long>{

  @Autowired
  private MainForumRepository mainForumRepository;
  private ModelMapper objectMapper = new ModelMapper();

  public boolean createMainForum(MainRequest body, ResponAPI<MainResponse> responAPI) {
    try {
      MainForum mainForum = objectMapper.map(body, MainForum.class);
      mainForumRepository.save(mainForum);

      responAPI.setData(mapToMainResponse(mainForum));
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (ValidationException e) {
      responAPI.setErrorMessage(e.getMessage());
    } catch (Exception e) {
      responAPI.setErrorMessage(e.getMessage());
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      return false;
    }
    return true;
  }

  public boolean updateMainForum(MainRequest body, Long id, ResponAPI<MainResponse> responAPI) {
    // findById
    Optional<MainForum> mOptional = mainForumRepository.findById(id);
    if(!mOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    try {
      MainForum mainForum = mOptional.get();
      mainForum.setId(id);
      mainForum.setTitle(body.getTitle());
      mainForum.setDescription(body.getDescription());
      mainForumRepository.save(mainForum);

      responAPI.setData(mapToMainResponse(mainForum));
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (ValidationException e) {
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean deleteMainForum(Long id, ResponAPI<MainResponse> responAPI) {
    Optional<MainForum> mOptional = mainForumRepository.findById(id);
    if(!mOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    try {
      MainForum mainForum = mOptional.get();
      if(!mainForum.getSubForums().isEmpty()) {
        responAPI.setData(null);
        responAPI.setErrorCode(ErrorCodeApi.FAILED);
        responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      } else {
        mainForumRepository.deleteById(id);
        responAPI.setData(mapToMainResponse(mainForum));
        responAPI.setErrorCode(ErrorCodeApi.SUCCESS);
        responAPI.setErrorMessage(MessageApi.SUCCESS);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  public boolean getMainForumById(ResponAPI<DtoResListMain> responAPI, Long id) {
    Optional<MainForum> optionalMain = mainForumRepository.findById(id);
    if(!optionalMain.isPresent()) {
      responAPI.setErrorMessage("Data tidak ditemukan!");
      return false;
    }
    try {
      DtoResListMain response = DtoResListMain.getInstance(optionalMain.get());
      responAPI.setData(response);
    } catch (Exception e) {
      responAPI.setErrorMessage(e.getMessage());
    }
    return true;
  }

  private MainResponse mapToMainResponse(MainForum mainForum) {
    return objectMapper.map(mainForum, MainResponse.class);
  }
  
}
