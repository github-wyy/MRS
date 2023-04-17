package com.wyy.mrs.service;

import com.wyy.mrs.model.entity.Arrangement;
import com.wyy.mrs.model.vo.ArrangementVO;

import java.util.List;

public interface ArrangementService {

    void save(Arrangement arrangement);

    List<Arrangement> findAll();

    ArrangementVO findByFilmId(String fid);

    List<Integer> getSeatsHaveSelected(String id);

    Arrangement findById(String id);

    void deleteById(String id);

    Arrangement Update(Arrangement arrangement);

}
