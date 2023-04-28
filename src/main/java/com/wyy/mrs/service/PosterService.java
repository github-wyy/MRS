package com.wyy.mrs.service;

import com.wyy.mrs.model.entity.Poster;

import java.util.List;

public interface PosterService {

    void save(Poster poster);

    void update(Poster poster);

    List<Poster> findAll();

    List<Poster> findByStatus(boolean status);

    void deleteById(String id);

    void deleteAll();

}
