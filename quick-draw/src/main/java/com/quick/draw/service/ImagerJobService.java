package com.quick.draw.service;


import com.quick.draw.domain.po.ImageJobPO;

import java.util.List;

public interface ImagerJobService {
    /**
     * 根据用户id查询该用户下所有job
     * @param userId
     * @return
     */
    List<ImageJobPO> getJobByUserId(Long userId, Integer taskStatus);
}
