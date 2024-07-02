package com.commtalk.domain.member.service;

import com.commtalk.domain.member.dto.JoinDTO;
import com.commtalk.domain.member.dto.LoginDTO;
import com.commtalk.domain.member.dto.MemberDTO;
import com.commtalk.domain.member.dto.UpdateMemberDTO;

public interface MemberService {

    String login(LoginDTO loginDto);

    Long join(JoinDTO joinDto);

    MemberDTO getInfoById(Long memberId);

    void updateInfo(Long memberId, UpdateMemberDTO memberDto);

}
