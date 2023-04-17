package com.wyy.mrs.model.vo;

import com.wyy.mrs.model.entity.Arrangement;
import com.wyy.mrs.model.entity.Film;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ArrangementVO {

    private List<Arrangement> arrangements;

    private Film film;

}
