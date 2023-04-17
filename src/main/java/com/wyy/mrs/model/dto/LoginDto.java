package com.wyy.mrs.model.dto;

import lombok.Data;

@Data
public class LoginDto {

    private String username;

    private String password;

    private boolean remember;

}
