package com.forum.server.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.forum.server.server.payload.request.MainRequest;
import com.forum.server.server.payload.response.DtoResListMain;
import com.forum.server.server.payload.response.MainResponse;
import com.forum.server.server.service.MainService;


@RestController
@RequestMapping("/api/main")
public class MainForumController {
  @Autowired
  private MainService mainService;

  //Get All Main Forum
  @GetMapping("/")
  public ResponseEntity<ResponAPI<Page<DtoResListMain>>> getAllMainForum(
    @RequestParam(value = "search", required = false) String search,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "limit", required = false) Integer limit,
    @RequestParam(value = "sortBy", required = false) List<String>sortBy,
    @RequestParam(value = "descending", required = false) Boolean desc
  ) {
    Page<DtoResListMain> mainNotPending = mainService.getAllMainForum(search, page, limit, sortBy, desc);
    ResponAPI<Page<DtoResListMain>> responAPI = new ResponAPI<>();
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setData(mainNotPending);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }


  //Get Main Forum By Id
  @GetMapping("/{id}")
  public ResponseEntity<ResponAPI<DtoResListMain>> getDetailMainForum(@PathVariable("id") Long id) {
    ResponAPI<DtoResListMain> responAPI = new ResponAPI<>();
    if(!mainService.getMainForumById(responAPI, id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }

  //Create Main Forum
  @PostMapping("/create")
  @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
  public ResponseEntity<ResponAPI<MainResponse>> createMainForum(@ModelAttribute MainRequest body, @RequestParam("file") MultipartFile file) {
    ResponAPI<MainResponse> responAPI = new ResponAPI<>();
    if(!mainService.createMainForum(body, file, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Edit Main Forum
  @PostMapping("/{id}")
  @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
  public ResponseEntity<ResponAPI<MainResponse>> updateMainForum(@PathVariable Long id, @ModelAttribute MainRequest body, @RequestParam("file") MultipartFile file) {
    ResponAPI<MainResponse> responAPI = new ResponAPI<>();
    if(!mainService.updateMainForum(body,file, id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }

  //Delete Main Forum
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
  public ResponseEntity<ResponAPI<MainResponse>> deleteMainForum(@PathVariable Long id) {
    ResponAPI<MainResponse> responAPI = new ResponAPI<>();
    if(!mainService.deleteMainForum(id, responAPI)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responAPI);
    }
    return ResponseEntity.ok(responAPI);
  }
}
