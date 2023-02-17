package com.forum.server.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.payload.request.ProfileRequest;
import com.forum.server.server.payload.request.RoleRequest;
import com.forum.server.server.payload.response.DtoResProfile;
import com.forum.server.server.payload.response.DtoUserRole;
import com.forum.server.server.repository.UserRepository;
import com.forum.server.server.service.ProfileService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@CrossOrigin(origins = "http://10.10.102.48:8081")
@RestController
@RequestMapping("/api/")
public class ProfileController {
  @Autowired
  private ProfileService profileService;

  @Autowired
  private UserRepository userRepository;

  @GetMapping("profile/{id}")
  public ResponseEntity<ResponAPI<DtoResProfile>> getUserById(@PathVariable("id") Long id) {
    ResponAPI<DtoResProfile> responAPI = new ResponAPI<>();
    if (!profileService.getUserById(responAPI, id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

  @PostMapping("profile/{id}")
  public ResponseEntity<ResponAPI<DtoResProfile>> updateProfile(@PathVariable("id") Long id,
      @ModelAttribute ProfileRequest body, @RequestParam("file") MultipartFile file) {
    ResponAPI<DtoResProfile> responAPI = new ResponAPI<>();
    if (!profileService.updateProfileById(id, body, file, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Get All User Role
  @GetMapping("userRole/")
  public ResponseEntity<ResponAPI<List<DtoUserRole>>> getUserRole() {
    ResponAPI<List<DtoUserRole>> responAPI = new ResponAPI<>();
    List<DtoUserRole> data = userRepository.getAllUserRole();
    responAPI.setData(data);
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.ok(responAPI);
  }

  //Update Role User
  // @PostMapping("userRole/{userId}")
  // public ResponseEntity<ResponAPI<UserRole>> updateRoleUser(@RequestBody RoleRequest body, @PathVariable("userId") Long userId) {
  //   ResponAPI<UserRole> responAPI = new ResponAPI<>();
  //   System.out.println(body.getRole_id());
  //   // UserRole user = userRoleRepository.updateRole(userId, body.getRole_id());
  //   // responAPI.setData(user);
  //   responAPI.setErrorCode(ErrorCode.SUCCESS);
  //   responAPI.setErrorMessage(MessageApi.SUCCESS);
  //   return ResponseEntity.ok(responAPI);
  // }
  @GetMapping("userRole/user/{id}")
  public ResponseEntity<ResponAPI<DtoResProfile>> getUser(@PathVariable("id") Long id) {
    ResponAPI<DtoResProfile> responAPI = new ResponAPI<>();
    if (!profileService.getUserById(responAPI, id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

}
