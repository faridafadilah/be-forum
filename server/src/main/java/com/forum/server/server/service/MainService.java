package com.forum.server.server.service;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.forum.server.server.base.BasePageInterface;
import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.models.MainForum;
import com.forum.server.server.payload.request.MainRequest;
import com.forum.server.server.payload.response.DtoResListMain;
import com.forum.server.server.payload.response.MainResponse;
import com.forum.server.server.repository.MainForumRepository;
import com.forum.server.server.specification.MainSpecification;

import javax.validation.ValidationException;

@Service
public class MainService implements BasePageInterface<MainForum, MainSpecification, MainResponse, Long> {
  private final Path root = Paths.get("./imageMain");

  @Autowired
  private MainSpecification specification;

  @Autowired
  private MainForumRepository mainForumRepository;
  private ModelMapper objectMapper = new ModelMapper();

  public boolean createMainForum(MainRequest body, MultipartFile file, ResponAPI<MainResponse> responAPI) {

    try {
      MainForum mainForum = objectMapper.map(body, MainForum.class);
      String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
      String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
      String uniqueFilename = UUID.randomUUID().toString() + ext;
      Path filePath = this.root.resolve(uniqueFilename);
      Files.copy(file.getInputStream(), filePath);
      String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/imageMain/").path(uniqueFilename).toUriString();
      mainForum.setNameImage(uniqueFilename);
      mainForum.setUrlImage(url);
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

  public boolean updateMainForum(MainRequest body, MultipartFile file, Long id, ResponAPI<MainResponse> responAPI) {
    // findById
    Optional<MainForum> mOptional = mainForumRepository.findById(id);
    if (!mOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    try {
      MainForum mainForum = mOptional.get();
      String nameImage = mainForum.getNameImage();
      if(file != null) {
          Path oldFile = root.resolve(nameImage);
          Files.deleteIfExists(oldFile);
          
          String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
          String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
          String uniqueFilename = UUID.randomUUID().toString() + ext;
          Path filePath = this.root.resolve(uniqueFilename);
          Files.copy(file.getInputStream(), filePath);
          String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/imageMain/").path(uniqueFilename).toUriString();
          mainForum.setNameImage(uniqueFilename);
          mainForum.setUrlImage(url);
      }
      mainForum.setId(id);
      mainForum.setTitle(body.getTitle());
      mainForum.setDescription(body.getDescription());

      mainForumRepository.save(mainForum);
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean deleteMainForum(Long id, ResponAPI<MainResponse> responAPI) {
    Optional<MainForum> mOptional = mainForumRepository.findById(id);
    if (!mOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    try {
      MainForum mainForum = mOptional.get();
      if (!mainForum.getSubForums().isEmpty()) {
        responAPI.setData(null);
        responAPI.setErrorCode(ErrorCodeApi.FAILED);
        responAPI.setErrorMessage("Maaf didalam main forum tidak kosong");
      } else {
        String nameImage = mainForum.getNameImage();
        Path file = root.resolve(nameImage);
        try {
          Files.delete(file);
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
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

  
  private MainResponse mapToMainResponse(MainForum mainForum) {
    return objectMapper.map(mainForum, MainResponse.class);
  }

  public boolean getMainForumById(ResponAPI<DtoResListMain> responAPI, Long id) {
    Optional<MainForum> optionalMain = mainForumRepository.findById(id);
    if (!optionalMain.isPresent()) {
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


  // GetAll + Pagination
  public Page<DtoResListMain> getAllMainForum(String search, Integer page, Integer limit, List<String> sortBy,
      Boolean desc) {
    sortBy = (sortBy != null) ? sortBy : Arrays.asList("id");
    desc = (desc != null) ? desc : true;
    Pageable pageableRequest = this.defaultPage(search, page, limit, sortBy, desc);
    Page<MainForum> settingPage = mainForumRepository.findAll(this.defaultSpec(search, specification), pageableRequest);
    List<MainForum> mains = settingPage.getContent();
    List<DtoResListMain> responseList = new ArrayList<>();
    mains.stream().forEach(a -> {
      responseList.add(DtoResListMain.getInstance(a));
    });
    Page<DtoResListMain> response = new PageImpl<>(responseList, pageableRequest, settingPage.getTotalElements());
    return response;
  }

}
