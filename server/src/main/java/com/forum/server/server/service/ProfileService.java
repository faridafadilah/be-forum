package com.forum.server.server.service;

import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCodeApi;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.models.User;
import com.forum.server.server.payload.request.ProfileRequest;
import com.forum.server.server.payload.response.DtoResProfile;
import com.forum.server.server.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import com.forum.server.server.payload.response.DtoUserRole;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Service
public class ProfileService {
  private final Path root = Paths.get("./imageUser");

  private ModelMapper objectMapper = new ModelMapper();

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private UserRepository userRepository;

  public void updateRole(Long roleId, Long userId) {
    String sql = "UPDATE public.user_roles SET role_id = ? WHERE user_id = ?";
    jdbcTemplate.update(sql, roleId, userId);
  }

  public boolean getUserById(ResponAPI<DtoResProfile> responAPI, Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (!optionalUser.isPresent()) {
      responAPI.setErrorMessage("Data tidak ditemukan!");
      return false;
    }
    try {
      DtoResProfile response = DtoResProfile.getInstance(optionalUser.get());
      responAPI.setData(response);
    } catch (Exception e) {
      responAPI.setErrorMessage(e.getMessage());
    }
    return true;
  }

  public boolean updateProfileById(Long id, ProfileRequest body, MultipartFile file,
      ResponAPI<DtoResProfile> responAPI) {
    Optional<User> uOptional = userRepository.findById(id);
    if (!uOptional.isPresent()) {
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      responAPI.setErrorMessage(MessageApi.BODY_NOT_VALID);
      return false;
    }

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

      if (file != null && !file.isEmpty()) {
        if (userImage != null) {
          Path oldFile = root.resolve(userImage);
          Files.deleteIfExists(oldFile);
        }
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String uniqueFilename = UUID.randomUUID().toString() + ext;
        Path filePath = this.root.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/imageUser/").path(uniqueFilename)
            .toUriString();

        user.setUrlImage(url);
        user.setImage(uniqueFilename);
      }

      userRepository.save(user);
      responAPI.setData(mapToProfileResponse(user));
      responAPI.setErrorCode(ErrorCode.SUCCESS);
      responAPI.setErrorMessage(MessageApi.SUCCESS);
      return true;

    } catch (Exception e) {
      responAPI.setErrorCode(ErrorCodeApi.FAILED);
      responAPI.setErrorMessage(e.getMessage());
      return false;
    }
  }

  private DtoResProfile mapToProfileResponse(User user) {
    return objectMapper.map(user, DtoResProfile.class);
  }

  public DtoUserRole getUserRoleById(Long userId) {
    String sql = "SELECT a.username as usernameUser, a.id as idUser, b.role_id as roleIdUser, c.name as roleUser "
        + "FROM users a "
        + "INNER JOIN user_roles b ON a.id = b.user_id "
        + "INNER JOIN roles c ON b.role_id = c.id "
        + "WHERE a.id = ? "
        + "ORDER BY a.id";

    RowMapper<DtoUserRole> rowMapper = new BeanPropertyRowMapper<>(DtoUserRole.class);
    DtoUserRole userDto = jdbcTemplate.queryForObject(sql, rowMapper, userId);

    return userDto;
  }

}
