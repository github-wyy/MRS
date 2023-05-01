package com.wyy.mrs.service;

import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.model.entity.FilmEvaluate;
import com.wyy.mrs.model.vo.FilmEvaluateVO;

import java.util.List;

public interface FilmEvaluateService {

    void save(FilmEvaluate filmEvaluate) throws Exception;

    List<FilmEvaluateVO> findAllByFilmId(String fid);

    void deleteAllByFilmId(String fid);

    void deleteById(String id);

    List<FilmEvaluate> findAllFilm();

    void ModifyAverageFilmRating(String fid);

    //void ModifyAllAverageFilmRating(List<Film> films, List<FilmEvaluate> evaluateList);

}
