package com.forum.server.server.service;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forum.server.server.base.BasePageInterface;
import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.models.SubForum;
import com.forum.server.server.models.Thread;
import com.forum.server.server.models.User;
import com.forum.server.server.models.UserLike;
import com.forum.server.server.payload.request.ThreadRequest;
import com.forum.server.server.payload.response.DtoResListThread;
import com.forum.server.server.payload.response.LikeResponse;
import com.forum.server.server.payload.response.ThreadResponse;
import com.forum.server.server.repository.*;
import com.forum.server.server.specification.ThreadSpecification;

import javax.validation.ValidationException;

@Service
public class ThreadService {
  private final Path root = Paths.get("./imageThread");

  @Autowired
  private ThreadSpecification specification;

  @Autowired
  private SubForumRepository subForumRepository;

  @Autowired
  private ThreadRepository threadRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserLikeRepository likeRepository;

  private ModelMapper objectMapper = new ModelMapper();

  private String url = "http://10.10.102.97:8080/imageThread/";

  public boolean getThreadById(ResponAPI<DtoResListThread> responAPI, Long id) {
    addView(id);
    Optional<Thread> optionalThread = threadRepository.findById(id);
    if (!optionalThread.isPresent()) {
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

  public boolean getLikeByUser(ResponAPI<LikeResponse> responAPI, Long id) {
    Optional<User> uOptional = userRepository.findById(id);
    if (!uOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }
    User user = uOptional.get();

    Optional<UserLike> optionalLike = likeRepository.findByUser(user);
    if (!optionalLike.isPresent()) {
      responAPI.setErrorMessage("Data tidak ditemukan!");
      return false;
    }

    try {
      LikeResponse response = LikeResponse.getInstance(optionalLike.get());
      responAPI.setData(response);
    } catch (Exception e) {
      responAPI.setErrorMessage(e.getMessage());
    }
    return true;
  }

  private SubForum findSubForum(Long subforumId) {
    Optional<SubForum> forum = subForumRepository.findById(subforumId);
    return forum.isPresent() ? forum.get() : null;
  }

  private User findUserId(Long userId) {
    Optional<User> user = userRepository.findById(userId);
    return user.isPresent() ? user.get() : null;
  }

  public boolean createThread(ThreadRequest body, MultipartFile file, ResponAPI<ThreadResponse> responAPI) {
    try {
      SubForum subForum = findSubForum(body.getSubforumId());
      if (subForum == null) {
        responAPI.setErrorCode(ErrorCodeApi.FAILED);
        responAPI.setErrorMessage("Sub Forum Not Found with Id= " + body.getSubforumId());
        return false;
      }

      User users = findUserId(body.getUserId());
      if (users == null) {
        responAPI.setErrorCode(ErrorCodeApi.FAILED);
        responAPI.setErrorMessage("User Not Found!");
        return false;
      }

      Thread thread = new Thread();
      if(file != null) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        thread.setNameImage(filename);
        thread.setUrlImage(url + filename);
        try {
          Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
          if (e instanceof FileAlreadyExistsException) {
            throw new RuntimeException("A file of that name already exists.");
          }
  
          throw new RuntimeException(e.getMessage());
        }
      }

      thread.setTitle(body.getTitle());
      thread.setContent(body.getContent());
      thread.setSubForum(subForum);
      thread.setUsers(users);
      thread.setLiked(thread.getLiked());
      threadRepository.save(thread);

      responAPI.setData(null);
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

  public boolean updateThread(ThreadRequest body, MultipartFile file, Long id, ResponAPI<ThreadResponse> responAPI) {
    Optional<Thread> tOptional = threadRepository.findById(id);
    if (!tOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }
    try {
      Thread thread = tOptional.get();
      if (file.isEmpty()) {
        responAPI.setErrorMessage("File tidak boleh kosong");
        responAPI.setErrorCode(ErrorCodeApi.FAILED);
        return false;
      }
      try {
        String nameImage = thread.getNameImage();
        Path oldFile = root.resolve(nameImage);
        Files.deleteIfExists(oldFile);
        Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        thread.setNameImage(filename);
        thread.setUrlImage(url + filename);
      } catch (Exception e) {
        if (e instanceof FileAlreadyExistsException) {
          throw new RuntimeException("A file of that name already exists.");
        }

        throw new RuntimeException(e.getMessage());
      }
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
    if (!tOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
    }

    try {
      Thread thread = tOptional.get();
      String nameImage = thread.getNameImage();
      Path file = root.resolve(nameImage);
      try {
        Files.delete(file);
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
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
    return objectMapper.map(thread, ThreadResponse.class);
  }

  public Page<DtoResListThread> getAll(int page, int limit, Long id) {
    Pageable pageable = PageRequest.of(page, limit);
    Page<Thread> settinPage = threadRepository.findAll(specification.subEqual(id), pageable);
    List<DtoResListThread> response = settinPage.getContent().stream().map(DtoResListThread::getInstance)
        .collect(Collectors.toList());
    return new PageImpl<>(response, pageable, settinPage.getTotalElements());
  }

  public Page<DtoResListThread> getByUserId(int page, int limit, Long id) {
    Pageable pageable = PageRequest.of(page, limit);
    Page<Thread> settinPage = threadRepository.findAll(specification.userEqual(id), pageable);
    List<DtoResListThread> response = settinPage.getContent().stream().map(DtoResListThread::getInstance)
        .collect(Collectors.toList());
    return new PageImpl<>(response, pageable, settinPage.getTotalElements());
  }

  // public Page<DtoResListThread> getAllThread(String search, Integer page,
  // Integer limit, List<String> sortBy,
  // Boolean desc) {
  // sortBy = (sortBy != null) ? sortBy : Arrays.asList("id");
  // desc = (desc != null) ? desc : true;
  // Pageable pageableRequest = this.defaultPage(search, page, limit, sortBy,
  // desc);
  // Page<Thread> setting = threadRepository.findAll(this.defaultSpec(search,
  // specification), pageableRequest);
  // List<Thread> thread = setting.getContent();
  // List<DtoResListThread> response = new ArrayList<>();
  // thread.stream().forEach(a -> {
  // response.add(DtoResListThread.getInstance(a));
  // });
  // Page<DtoResListThread> responseList = new PageImpl<>(response,
  // pageableRequest, setting.getTotalElements());
  // return responseList;
  // }

  // public Page<DtoResListThread> getAllByIdUser(int page, int limit, long id) {
  // Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC,
  // "id"));
  // Page<Thread> settingPage = threadRepository
  // .findAll(specification.userEqual(id), pageable);
  // List<DtoResListThread> response = settingPage.getContent().stream().map(a ->
  // DtoResListThread.getInstance(a))
  // .collect(Collectors.toList());
  // PageImpl<DtoResListThread> responsePage = new PageImpl<>(response, pageable,
  // settingPage.getTotalElements());
  // return responsePage;
  // }

  // Menambahkan jumlah view pada thread
  public void addView(Long id) {
    Optional<Thread> optionalThread = threadRepository.findById(id);
    if (optionalThread.isPresent()) {
      Thread thread = optionalThread.get();
      thread.setView(thread.getView() + 1);
      threadRepository.save(thread);
    }
  }

  public boolean addLikeThreadById(Long id, ResponAPI<ThreadResponse> responAPI, Long userId) {
    Optional<Thread> tOptional = threadRepository.findById(id);
    if (!tOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    Optional<User> uOptional = userRepository.findById(userId);
    if (!uOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    try {
      Thread thread = tOptional.get();
      User user = uOptional.get();
      Optional<UserLike> likeOptional = likeRepository.findByThreadAndUser(thread, user);
      if (likeOptional.isPresent()) {
        responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
        responAPI.setErrorMessage("You already liked this thread");
        return false;
      }

      UserLike like = new UserLike();
      like.setThread(thread);
      like.setUser(user);
      likeRepository.save(like);

      thread.setLiked(thread.getLiked() + 1);
      threadRepository.save(thread);

      responAPI.setData(null);
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean unLikeThreadById(Long id, ResponAPI<ThreadResponse> responAPI, Long userId) {
    Optional<Thread> tOptional = threadRepository.findById(id);
    if (!tOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    Optional<User> uOptional = userRepository.findById(userId);
    if (!uOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

    try {
      Thread thread = tOptional.get();
      User user = uOptional.get();
      Optional<UserLike> likeOptional = likeRepository.findByThreadAndUser(thread, user);
      if (!likeOptional.isPresent()) {
        responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
        responAPI.setErrorMessage("You not like");
        return false;
      }

      UserLike userLike = likeOptional.get();
      likeRepository.delete(userLike);
      thread.setLiked(thread.getLiked() - 1);
      threadRepository.save(thread);

      responAPI.setData(null);
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCode.BODY_NOT_VALID);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }

}
