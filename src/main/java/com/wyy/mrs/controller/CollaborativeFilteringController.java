package com.wyy.mrs.controller;

import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.service.CollaborativeFilteringService;
import com.wyy.mrs.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
//推荐算法接口
@RequestMapping("/api/collaborativeFiltering")
public class CollaborativeFilteringController {

    @Resource
    CollaborativeFilteringService collaborativeFilteringService;

    @GetMapping("/{uid}")
    public List<Film> list(@PathVariable String uid) {
        List<Film> films = collaborativeFilteringService.findFilmBaseUserFiler(uid);
        return films;
    }
}
