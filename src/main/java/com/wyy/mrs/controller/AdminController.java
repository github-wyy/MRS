package com.wyy.mrs.controller;

import com.wyy.mrs.model.dto.LoginDto;
import com.wyy.mrs.service.AdminService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
//管理员接口
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping("/login")
    //管理员登陆
    public Map<String, String> login(@RequestBody LoginDto loginDto) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", adminService.login(loginDto));
        return map;
    }

}
