package com.commtalk.domain.auth.service;

import com.commtalk.domain.auth.dto.JoinDTO;
import com.commtalk.domain.auth.dto.LoginDTO;

public interface AuthService {

    Long join(JoinDTO joinDto);

    String login(LoginDTO loginDto);

}
