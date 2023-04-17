package com.wyy.mrs.service.impl;

import com.wyy.mrs.mapper.ActivityMapper;
import com.wyy.mrs.model.entity.Activity;
import com.wyy.mrs.service.ActivityService;
import com.wyy.mrs.utils.DataTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityMapper activityMapper;

    @Override
    public void create(Activity activity) {
        activity.setId(UUID.randomUUID().toString());
        activity.setCreateAt(DataTimeUtil.getNowTimeString());
        activityMapper.insert(activity);
    }

    @Override
    public Activity findById(String id) {
        return activityMapper.selectById(id);
    }

    @Override
    public List<Activity> findAll() {
        return activityMapper.selectList(null);
    }

    @Override
    public void deleteById(String id) {
        activityMapper.deleteById(id);
    }

}
