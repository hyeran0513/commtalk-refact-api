package com.commtalk.domain.member.service;

import com.commtalk.domain.member.dto.JoinDTO;
import com.commtalk.domain.member.dto.LoginDTO;
import com.commtalk.domain.member.dto.MemberDTO;

public interface MemberService {

    String login(LoginDTO loginDto);

    Long join(JoinDTO joinDto);

    MemberDTO getInfoById(Long memberId);

    void updateInfo(MemberDTO memberDto);

}
