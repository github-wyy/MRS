package com.wyy.mrs.model.vo;

import com.wyy.mrs.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveUserVO {

    private User user;

    private Integer Number;

}
