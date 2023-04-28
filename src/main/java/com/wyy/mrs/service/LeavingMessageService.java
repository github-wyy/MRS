package com.wyy.mrs.service;

import com.wyy.mrs.model.entity.LeavingMessage;
import com.wyy.mrs.model.vo.ActiveUserVO;
import com.wyy.mrs.model.vo.LeavingMessageVO;

import java.util.List;

public interface LeavingMessageService {

    void save(LeavingMessage leavingMessage);

    void reply(LeavingMessage leavingMessage);

    List<LeavingMessageVO> findAll();

    //获取活跃留言的用户
    List<ActiveUserVO> findActiveUsers();

}
