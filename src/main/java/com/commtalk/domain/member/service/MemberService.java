package com.commtalk.domain.member.service;

import com.commtalk.domain.member.dto.request.MemberJoinRequest;
import com.commtalk.domain.member.dto.request.MemberLoginRequest;
import com.commtalk.domain.member.dto.MemberDTO;
import com.commtalk.domain.member.dto.request.MemberUpdateRequest;

public interface MemberService {

    String login(MemberLoginRequest loginReq);

    Long join(MemberJoinRequest joinReq);

    MemberDTO getInfoById(Long memberId);

    void updateInfo(Long memberId, MemberUpdateRequest updateReq);

}
