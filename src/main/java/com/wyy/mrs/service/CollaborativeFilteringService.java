package com.wyy.mrs.service;

import com.wyy.mrs.model.entity.CollaborativeFiltering;
import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.model.entity.FilmEvaluate;

import java.util.List;

public interface CollaborativeFilteringService {

    //正常登录页面之后，返回推荐的电影
    List<Film> findFilmBaseUserFiler(String uid);

    //评分之后，修改map，插入数据
    void updateValue(List<FilmEvaluate> list);
}
