package com.crowd.service.api;

import com.crowd.entity.vo.ProjectVO;

public interface ProjectService {

    void saveProject(ProjectVO projectVO, Integer memberId);
}
