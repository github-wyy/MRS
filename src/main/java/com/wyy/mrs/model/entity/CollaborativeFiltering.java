package com.wyy.mrs.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 关于协同过滤算法的表
 */
@Data
@TableName("t_collaborative_filtering")
@NoArgsConstructor
@AllArgsConstructor
public class CollaborativeFiltering implements Serializable {
    private String id;
    private String cname;
    //序列化的评分矩阵
    private String value;
}
