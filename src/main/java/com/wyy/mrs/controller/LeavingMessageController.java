package com.wyy.mrs.controller;

import com.wyy.mrs.model.entity.LeavingMessage;
import com.wyy.mrs.model.vo.ActiveUserVO;
import com.wyy.mrs.model.vo.LeavingMessageVO;
import com.wyy.mrs.service.LeavingMessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/lm")
public class LeavingMessageController {

    @Resource
    private LeavingMessageService leavingMessageService;

    @PostMapping("")
    public void save(@RequestBody LeavingMessage leavingMessage) {
        leavingMessageService.save(leavingMessage);
    }

    @PutMapping("")
    public void reply(@RequestBody LeavingMessage leavingMessage) {
        leavingMessageService.reply(leavingMessage);
    }

    @GetMapping("")
    public List<LeavingMessageVO> list() {
        return leavingMessageService.findAll();
    }

    @GetMapping("/active")
    public List<ActiveUserVO> findActiveUsers() {
        return leavingMessageService.findActiveUsers();
    }

}
