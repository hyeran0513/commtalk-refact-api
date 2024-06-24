package com.commtalk.domain.auth.service;

import com.commtalk.domain.auth.dto.JoinDTO;

public interface AuthService {

    Long join(JoinDTO joinDto);

}
