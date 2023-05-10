package com.wyy.mrs.service;

import com.wyy.mrs.model.entity.Film;

import java.util.List;

public interface FilmService {

    void save(Film film);

    void deleteById(String id);

    List<Film> findAll();

    List<Film> findByRegionAndType(String region, String type);

    //获取热门电影
    List<Film> findHots(Integer limit);

    //获取乱序热门电影
    List<Film> findRListHots(Integer limit);

    //根据电影名模糊查询
    List<Film> findLikeName(String name);

    Film findById(String id);

    Film update(Film film);

}
