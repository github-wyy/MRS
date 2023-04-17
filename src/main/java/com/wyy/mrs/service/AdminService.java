package com.wyy.mrs.service;

import com.wyy.mrs.model.dto.LoginDto;

public interface AdminService {

    String login(LoginDto loginDto) throws Exception;

}
