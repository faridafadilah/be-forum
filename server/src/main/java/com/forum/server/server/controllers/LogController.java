package com.forum.server.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.forum.server.server.base.ResponAPI;
import com.forum.server.server.constant.ErrorCode;
import com.forum.server.server.constant.MessageApi;
import com.forum.server.server.service.LogServices;
import com.forum.server.server.payload.response.*;

@RestController
// @CrossOrigin(origins = "http://10.10.102.90:8081")
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api")
public class LogController {

  @Autowired
  private LogServices logServices;

  @GetMapping("/log")
  public ResponseEntity<ResponAPI<Page<DtoResListLog>>> getAllMainForum(
      @RequestParam(value = "search_query", required = false) String search,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "limit", required = false) Integer limit,
      @RequestParam(value = "sortBy", required = false) List<String>sortBy,
      @RequestParam(value = "descending", required = false) Boolean desc
  ) {
    Page<DtoResListLog> mainNotPending = logServices.getAllLog(search, page, limit, sortBy, desc);
    ResponAPI<Page<DtoResListLog>> responAPI = new ResponAPI<>();
    responAPI.setErrorMessage(MessageApi.SUCCESS);
    responAPI.setErrorCode(ErrorCode.SUCCESS);
    responAPI.setData(mainNotPending);
    return ResponseEntity.status(HttpStatus.OK).body(responAPI);
  }
}
