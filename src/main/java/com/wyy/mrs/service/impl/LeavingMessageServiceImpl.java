package com.wyy.mrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.mapper.LeavingMessageMapper;
import com.wyy.mrs.mapper.UserMapper;
import com.wyy.mrs.model.entity.LeavingMessage;
import com.wyy.mrs.model.entity.User;
import com.wyy.mrs.model.vo.ActiveUserVO;
import com.wyy.mrs.model.vo.LeavingMessageVO;
import com.wyy.mrs.service.LeavingMessageService;
import com.wyy.mrs.utils.DataTimeUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "leavingMessage")
public class LeavingMessageServiceImpl implements LeavingMessageService {

    @Resource
    private LeavingMessageMapper leavingMessageMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public void save(LeavingMessage leavingMessage) {
        leavingMessage.setCreateAt(DataTimeUtil.getNowTimeString());
        leavingMessageMapper.insert(leavingMessage);
    }

    @Override
    public void reply(LeavingMessage leavingMessage) {
        leavingMessageMapper.updateById(leavingMessage);
    }

    @Override
    public List<LeavingMessageVO> findAll() {
        List<LeavingMessage> list = leavingMessageMapper.selectList(null);
        List<LeavingMessageVO> result = new ArrayList<>();
        for (LeavingMessage m : list) {
            User user = userMapper.selectById(m.getUid());
            result.add(new LeavingMessageVO(m.getId(), m, user));
        }
        return result;
    }

    @Override
    public List<ActiveUserVO> findActiveUsers() {
        List<ActiveUserVO> result = new ArrayList<>();
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            ActiveUserVO activeUserVO = new ActiveUserVO();
            activeUserVO.setUser(user);
            activeUserVO.setNumber(leavingMessageMapper.selectList(
                    new QueryWrapper<LeavingMessage>().in("uid", user.getId())).size());
            result.add(activeUserVO);
        }
        //按留言数量排序
        result.sort((v1, v2) -> v2.getNumber().compareTo(v1.getNumber()));
        return result;
    }

}
