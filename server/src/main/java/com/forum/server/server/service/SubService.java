package com.forum.server.server.service;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.models.MainForum;
import com.forum.server.server.models.SubForum;
import com.forum.server.server.payload.request.SubRequest;
import com.forum.server.server.payload.response.DtoResListSub;
import com.forum.server.server.payload.response.SubResponse;
import com.forum.server.server.repository.MainForumRepository;
import com.forum.server.server.repository.SubForumRepository;
import com.forum.server.server.specification.SubSpecification;

@Service
public class SubService{
  private final Path root = Paths.get("./imageSub");
  private String url = "http://10.10.102.97:8080/imageSub/";

  @Autowired
  private SubSpecification specification;

  @Autowired
  private MainForumRepository mainForumRepository;

  @Autowired
  private SubForumRepository subForumRepository;
  private ModelMapper objectMapper = new ModelMapper();

  public boolean createSubForum(SubRequest body, MultipartFile file, ResponAPI<SubResponse> responAPI) {
    try {
      MainForum mainforum = findMainForum(body.getMainforumId());
      if(mainforum == null) {
        responAPI.setErrorCode(ErrorCodeApi.FAILED);
        responAPI.setErrorMessage("Main Forum Not Found with Id=" + body.getMainforumId());
        return false;
      }

      SubForum subForum = new SubForum();
      subForum.setJudul(body.getJudul());
      subForum.setDescription(body.getDescription());
      subForum.setMainForum(mainforum);
      String filename = StringUtils.cleanPath(file.getOriginalFilename());
      subForum.setNameImage(filename);
      subForum.setUrlImage(url+filename);
      subForumRepository.save(subForum);

      try {
        Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
      } catch (Exception e) {
        if (e instanceof FileAlreadyExistsException) {
          throw new RuntimeException("A file of that name already exists.");
        }
  
        throw new RuntimeException(e.getMessage());
      }

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

  private MainForum findMainForum(Long mainforumId) {
    Optional<MainForum> forum = mainForumRepository.findById(mainforumId);
    return forum.isPresent() ? forum.get() : null;
  }

  public boolean updateSubForum(SubRequest body, MultipartFile file, Long id, ResponAPI<SubResponse> responAPI) {
    Optional<SubForum> sOptional = subForumRepository.findById(id);
    if (!sOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    try {
      SubForum subForum = sOptional.get();
      if (file.isEmpty()) {
        responAPI.setErrorMessage("File tidak boleh kosong");
        responAPI.setErrorCode(ErrorCodeApi.FAILED);
        return false;
      }
      try {
        String nameImage = subForum.getNameImage();
        Path oldFile = root.resolve(nameImage);
        Files.deleteIfExists(oldFile);
        Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        subForum.setNameImage(filename);
        subForum.setUrlImage(url+filename);
      } catch (Exception e) {
        if (e instanceof FileAlreadyExistsException) {
          throw new RuntimeException("A file of that name already exists.");
        }

        throw new RuntimeException(e.getMessage());
      }
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
    if (!sOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
    }

    try {
      SubForum subForum = sOptional.get();
      String nameImage = subForum.getNameImage();
      Path file = root.resolve(nameImage);
      try {
        Files.delete(file);
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
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
    if (!optionalSub.isPresent()) {
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

  public Page<DtoResListSub> getAll(int page, int limit, Long id) {
    Pageable pageable = PageRequest.of(page, limit);
    Page<SubForum> settinPage = subForumRepository.findAll(specification.mainEqual(id), pageable);
    List<DtoResListSub> response = settinPage.getContent().stream().map(DtoResListSub::getInstance)
    .collect(Collectors.toList());
    return new PageImpl<>(response, pageable, settinPage.getTotalElements());
  }
}
