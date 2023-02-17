package com.forum.server.server.service;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.models.User;
import com.forum.server.server.payload.request.ProfileRequest;
import com.forum.server.server.payload.response.DtoResProfile;
import com.forum.server.server.repository.UserRepository;


@Service
public class ProfileService {
  private final Path root = Paths.get("./imageUser");
  private String url = "http://10.10.102.48:8080/imageUser/";

  private ModelMapper objectMapper = new ModelMapper();

  @Autowired
  private UserRepository userRepository;

  public boolean getUserById(ResponAPI<DtoResProfile> responAPI, Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if(!optionalUser.isPresent()) {
      responAPI.setErrorMessage("Data tidak ditemukan!");
      return false;
    }
    try {
      DtoResProfile response =DtoResProfile.getInstance(optionalUser.get());
      responAPI.setData(response);
    } catch (Exception e) {
      responAPI.setErrorMessage(e.getMessage());
    }
    return true;
  }

  public boolean updateProfileById(Long id, ProfileRequest body, MultipartFile file, ResponAPI<DtoResProfile> responAPI) {
    Optional<User> uOptional = userRepository.findById(id);
    if(!uOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }
    System.out.println(file.isEmpty());
    try {
      User user = uOptional.get();
      String userImage = user.getImage();
      user.setId(id);
      user.setUsername(body.getUsername());
      user.setEmail(body.getEmail());
      user.setBio(body.getBio());
      user.setGithub(body.getGithub());
      user.setWhatsapp(body.getWhatsapp());
      user.setLinkedin(body.getLinkedin());
      user.setGender(body.getGender());
      user.setAddress(body.getAddress());
      user.setHobies(body.getHobies());
      user.setBirth(body.getBirth());

        if(userImage == null) {
          try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
            String fileImage = StringUtils.cleanPath(file.getOriginalFilename());
            user.setUrlImage(url+fileImage);
            user.setImage(fileImage);
          } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
              throw new RuntimeException("A file of that name already exists.");
            }
          }
        } else {
          try {
            String nameImage = user.getImage();
            Path oldFile = root.resolve(nameImage);
            Files.deleteIfExists(oldFile);
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
            String newFile = StringUtils.cleanPath(file.getOriginalFilename());
            user.setUrlImage(url+newFile);
            user.setImage(newFile);   
          } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
              throw new RuntimeException("A file of that name already exists.");
            }
          }
        }
      userRepository.save(user);
      responAPI.setData(mapToProfileResponse(user));
      System.out.println(mapToProfileResponse(user));
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

  private DtoResProfile mapToProfileResponse(User user) {
    return objectMapper.map(user, DtoResProfile.class);
  }
  
}
