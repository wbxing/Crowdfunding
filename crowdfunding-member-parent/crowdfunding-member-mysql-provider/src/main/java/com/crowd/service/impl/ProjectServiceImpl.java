package com.crowd.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowd.service.api.ProjectService;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {

}
