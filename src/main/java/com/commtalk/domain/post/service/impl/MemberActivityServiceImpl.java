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
import com.commtalk.domain.post.service.MemberActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberActivityServiceImpl implements MemberActivityService {
    
    private final ActivityTypeRepository activityTypeRepo;
    private final MemberActivityRepository memberActivityRepo;

    @Override
    public Map<String, Boolean> getMemberActivitiesByPost(Long memberId, Long postId) {
        Map<String, Boolean> activities = new HashMap<>();
        activities.put("likeYN", false);
        activities.put("scrapYN", false);

        List<MemberActivity> memberActivities = memberActivityRepo.findByMemberIdAndPostId(memberId, postId);
        for (MemberActivity activity : memberActivities) {
            switch (activity.getType().getTypeName()) {
                case POST_LIKE:
                    activities.put("likeYN", true);
                    break;
                case POST_SCRAP:
                    activities.put("scrapYN", true);
                    break;
                default:
                    break;
            }
        }
        return activities;
    }

    @Override
    public Map<String, Boolean> getMemberActivitiesByComment(Long memberId, Long commentId) {
        Map<String, Boolean> activities = new HashMap<>();
        activities.put("likeYN", false);

        List<MemberActivity> memberActivities = memberActivityRepo.findByMemberIdAndCommentId(memberId, commentId);
        if (!memberActivities.isEmpty()
            && memberActivities.get(0).getType().getTypeName() == ActivityType.TypeName.COMMENT_LIKE) {
            activities.put("likeYN", true);
        }
        return activities;
    }

    @Override
    public void doActivity(ActivityType.TypeName typeName, Long memberId, Long refId) {
        // 회원 활동 유형 조회
        ActivityType activityType = activityTypeRepo.findByTypeName(typeName)
                .orElseThrow(() -> new EntityNotFoundException("회원 활동 유형을 찾을 수 없습니다."));

        // 회원 활동(좋아요, 스크랩) 저장
        Member member = Member.builder().id(memberId).build();
        MemberActivity activity = null;
        if (typeName.name().contains("POST")) {
            Post post = Post.builder().id(refId).build();
            activity = MemberActivity.create(activityType, member, post);
        }
        else if (typeName.name().contains("COMMENT")) {
            Comment comment = Comment.builder().id(refId).build();
            activity =  MemberActivity.create(activityType, member, comment);
        }

        if (activity == null) {
            throw new MemberActivitySetException("회원 활동 정보를 구성하는 도중 에러가 발생했습니다.");
        }
        memberActivityRepo.save(activity);
    }

    @Override
    public void undoActivity(ActivityType.TypeName typeName, Long memberId, Long refId) {
        // 회원 활동 유형 조회
        ActivityType activityType = activityTypeRepo.findByTypeName(typeName)
                .orElseThrow(() -> new EntityNotFoundException("회원 활동 유형을 찾을 수 없습니다."));

        // 회원 활동(좋아요, 스크랩) 삭제
        switch (activityType.getTypeName()) {
            case POST_LIKE:
            case POST_SCRAP:
                memberActivityRepo.deleteByMemberIdAndPostIdAndType(memberId, refId, activityType);
                break;
            case COMMENT_LIKE:
                memberActivityRepo.deleteByMemberIdAndCommentIdAndType(memberId, refId, activityType);
                break;
            default:
                break;
        }
    }
}
