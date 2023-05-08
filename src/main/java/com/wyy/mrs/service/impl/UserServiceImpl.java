package com.wyy.mrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.mapper.UserMapper;
import com.wyy.mrs.model.dto.LoginDto;
import com.wyy.mrs.model.entity.User;
import com.wyy.mrs.service.UserService;
import com.wyy.mrs.utils.DataTimeUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User login(LoginDto dto) throws Exception {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.in("username", dto.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user == null) throw new Exception("用户名或密码错误");
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) throw new Exception("用户名或密码错误");
        return user;
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    @Override
    public User findById(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public User update(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.updateById(user);
        return user;
    }

    @Override
    public User save(User user) throws Exception {
        if (findByUsername(user.getUsername()) != null) {
            throw new Exception("用户名已注册");
        }
        String now = DataTimeUtil.getNowTimeString();
        user.setId(UUID.randomUUID().toString());
        user.setCreateAt(now);
        user.setUpdateAt(now);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setAvatar("https://tse2-mm.cn.bing.net/th/id/OIP-C.YvlSxd61mVhdY9T8MYpjFgHaHa?w=183&h=183&c=7&r=0&o=5&dpr=1.3&pid=1.7");
        userMapper.insert(user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void deleteById(String id) {
        userMapper.deleteById(id);
    }

}
