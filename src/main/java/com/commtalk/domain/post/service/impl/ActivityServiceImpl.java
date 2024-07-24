package com.commtalk.domain.post.service.impl;

import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.entity.Comment;
import com.commtalk.domain.post.entity.MemberActivity;
import com.commtalk.domain.post.entity.Post;
import com.commtalk.domain.post.exception.MemberActivitySetException;
import com.commtalk.domain.post.repository.ActivityTypeRepository;
import com.commtalk.domain.post.repository.MemberActivityRepository;
import com.commtalk.domain.post.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    
    private final ActivityTypeRepository activityTypeRepo;
    private final MemberActivityRepository memberActivityRepo;

    @Override
    public void likePost(Long postId, Long memberId) {
        // 게시글 좋아요 생성
        MemberActivity activity = setMemberActivity(ActivityType.Activity.POST_LIKE, postId, memberId);
        memberActivityRepo.save(activity);
    }

    @Override
    public void cancelLikePost(Long postId, Long memberId) {
        // 게시글 좋아요 삭제

    }

    @Override
    public void scrapPost(Long postId, Long memberId) {
        // 게시글 스크랩 생성
        MemberActivity activity = setMemberActivity(ActivityType.Activity.POST_SCRAP, postId, memberId);
        memberActivityRepo.save(activity);
    }

    @Override
    public void cancelScrapPost(Long postId, Long memberId) {

    }

    @Override
    public void likeComment(Long commentId, Long memberId) {
        // 댓글 좋아요 생성
        MemberActivity activity = setMemberActivity(ActivityType.Activity.COMMENT_LIKE, commentId, memberId);
        memberActivityRepo.save(activity);
    }

    @Override
    public void cancelLikeComment(Long commentId, Long memberId) {

    }

    private MemberActivity setMemberActivity(ActivityType.Activity type, Long refId, Long memberId) {
        // 회원 활동 유형 조회
        ActivityType activityType = activityTypeRepo.findByTypeName(type)
                .orElseThrow(() -> new EntityNotFoundException("회원 활동 유형을 찾을 수 없습니다."));

        Member member = Member.builder().id(memberId).build();
        if (type.name().contains("POST")) {
            Post post = Post.builder().id(refId).build();
            return MemberActivity.create(activityType, member, post);
        }
        else if (type.name().contains("COMMENT")) {
            Comment comment = Comment.builder().id(refId).build();
            return MemberActivity.create(activityType, member, comment);
        }
        throw new MemberActivitySetException("회원 활동 정보를 구성하는 도중 에러가 발생했습니다.");
    }

}
