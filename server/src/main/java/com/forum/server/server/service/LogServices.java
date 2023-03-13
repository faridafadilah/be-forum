package com.forum.server.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.forum.server.server.payload.response.DtoResListLog;
import com.forum.server.server.repository.LogRepository;
import com.forum.server.server.models.*;
import com.forum.server.server.base.BasePageInterface;
import com.forum.server.server.specification.*;

@Service
public class LogServices implements BasePageInterface<Logs, LogSpecification, DtoResListLog, Long> {
  @Autowired
  private LogRepository repository;

  @Autowired
  private LogSpecification specification;

  public Page<DtoResListLog> getAllLog(String search, Integer page, Integer limit, List<String> sortBy,
      Boolean desc) {
    sortBy = (sortBy != null) ? sortBy : Arrays.asList("id");
    desc = (desc != null) ? desc : true;
    Pageable pageableRequest = this.defaultPage(search, page, limit, sortBy, desc);
    Page<Logs> settingPage = repository.findAll(this.defaultSpec(search, specification), pageableRequest);
    List<Logs> mains = settingPage.getContent();
    List<DtoResListLog> responseList = new ArrayList<>();
    mains.stream().forEach(a -> {
      responseList.add(DtoResListLog.getInstance(a));
    });
    Page<DtoResListLog> response = new PageImpl<>(responseList, pageableRequest, settingPage.getTotalElements());
    return response;
  }
}
