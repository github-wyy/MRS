package com.wyy.mrs.model.vo;

import com.wyy.mrs.model.entity.LeavingMessage;
import com.wyy.mrs.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LeavingMessageVO implements Serializable {

    private String id;

    private LeavingMessage leavingMessage;

    private User user;

}
