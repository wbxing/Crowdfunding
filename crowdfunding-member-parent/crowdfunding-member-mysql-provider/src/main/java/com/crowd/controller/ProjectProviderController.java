package com.crowd.controller;

import com.crowd.service.api.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectProviderController {

    @Autowired
    private ProjectService projectService;
}
