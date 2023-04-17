package com.wyy.mrs.controller;

import com.wyy.mrs.constant.Roles;
import com.wyy.mrs.model.dto.LoginDto;
import com.wyy.mrs.model.entity.User;
import com.wyy.mrs.service.UserService;
import com.wyy.mrs.utils.JwtTokenUtil;


import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//用户接口
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    //用户登录
    public Map<String, Object> login(@RequestBody LoginDto dto) throws Exception {
        User user = userService.login(dto);
        Map<String, Object> map = new HashMap<>();
        //是否选择记住我
        long exp = dto.isRemember() ? JwtTokenUtil.REMEMBER_EXPIRATION_TIME : JwtTokenUtil.EXPIRATION_TIME;
        List<String> roles = new ArrayList<>();
        roles.add(Roles.ROLE_USER);
        map.put("token", JwtTokenUtil.createToken(dto.getUsername(), roles, exp));
        map.put("user", user);
        return map;
    }

    @GetMapping("")
    //查找所有用户
    public List<User> findAll() {
        return userService.findAll();
    }

    @PutMapping("")
    //更新用户
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    //查找用户
    public User findById(@PathVariable String id) {
        return userService.findById(id);
    }

    @PostMapping("/register")
    //用户注册
    public User save(@RequestBody User user) throws Exception {
        return userService.save(user);
    }

}
