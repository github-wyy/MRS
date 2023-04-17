package com.wyy.mrs.model.vo;

import com.wyy.mrs.model.entity.FilmEvaluate;
import com.wyy.mrs.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 电影评分前端展示
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmEvaluateVO implements Serializable {

    private String id;

    private FilmEvaluate filmEvaluate;

    private User user;

}
