package com.wyy.mrs.controller;

import com.wyy.mrs.model.entity.Activity;
import com.wyy.mrs.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Resource
    private ActivityService activityService;

    @PostMapping("")
    //新增活动
    public void create(@RequestBody Activity activity) {
        activityService.create(activity);
    }

    @GetMapping("")
    //获取全部活动
    public List<Activity> findAll() {
        return activityService.findAll();
    }

    @GetMapping("{id}")
    //根据id查找活动
    public Activity findById(@PathVariable String id) {
        return activityService.findById(id);
    }

    @DeleteMapping("{id}")
    //删除活动
    public void delete(@PathVariable String id) {
        activityService.deleteById(id);
    }

}
