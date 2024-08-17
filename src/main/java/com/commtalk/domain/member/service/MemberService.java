package com.commtalk.domain.member.service;

import com.commtalk.domain.member.dto.request.*;
import com.commtalk.domain.member.dto.MemberDTO;

public interface MemberService {

    String login(MemberLoginRequest loginReq);

    Long join(MemberJoinRequest joinReq);

    void withdraw(Long memberId);

    MemberDTO getInfoById(Long memberId);

    void updateInfo(Long memberId, MemberUpdateRequest updateReq);

    void updatePassword(Long memberId, MemberPasswordUpdateRequest updateReq);

    void confirmPassword(Long memberId, String currentPassword);

}
