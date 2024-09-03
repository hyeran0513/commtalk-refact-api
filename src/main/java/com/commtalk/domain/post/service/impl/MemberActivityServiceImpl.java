package com.commtalk.domain.post.service.impl;

import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.post.dto.ParentCommentDTO;
import com.commtalk.domain.post.dto.PostDTO;
import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.entity.Comment;
import com.commtalk.domain.post.entity.MemberActivity;
import com.commtalk.domain.post.entity.Post;
import com.commtalk.domain.post.repository.ActivityTypeRepository;
import com.commtalk.domain.post.repository.CommentRepository;
import com.commtalk.domain.post.repository.MemberActivityRepository;
import com.commtalk.domain.post.repository.PostRepository;
import com.commtalk.domain.post.service.MemberActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberActivityServiceImpl implements MemberActivityService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final ActivityTypeRepository activityTypeRepo;
    private final MemberActivityRepository activityRepo;

    @Override
    public boolean isLikeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName) {
        return activityRepo.existsByMemberIdAndRefIdAndTypeName(memberId, postId, typeName);
    }

    @Override
    @Transactional
    public PostDTO likeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName) {
        // 회원 활동 유형 조회
        ActivityType activityType = activityTypeRepo.findByName(typeName)
                .orElseThrow(() -> new EntityNotFoundException("회원 활동 유형을 찾을 수 없습니다."));

        // 회원 활동 저장
        Member member = Member.builder().id(memberId).build();
        MemberActivity activity = MemberActivity.create(activityType, member, postId);
        activityRepo.save(activity);

        // 게시글 좋아요(스크랩) 수 업데이트
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (typeName == ActivityType.TypeName.POST_LIKE) {
            post.setLikeCount(post.getLikeCount() + 1);
        } else if (typeName == ActivityType.TypeName.POST_SCRAP) {
            post.setScrapCount(post.getScrapCount() + 1);
        }

        return PostDTO.from(postRepo.save(post), new ArrayList<>(), true, true);
    }

    @Override
    @Transactional
    public PostDTO unlikeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName) {
        // 회원 활동 삭제
        activityRepo.deleteByMemberIdAndRefIdAndTypeName(memberId, postId, typeName);

        // 게시글 좋아요(스크랩) 수 업데이트
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (typeName == ActivityType.TypeName.POST_LIKE) {
            post.setLikeCount(post.getLikeCount() - 1);
        } else if (typeName == ActivityType.TypeName.POST_SCRAP) {
            post.setScrapCount(post.getScrapCount() - 1);
        }

        return PostDTO.from(postRepo.save(post), new ArrayList<>(), false, false);
    }

    @Override
    public boolean isLikeComment(Long memberId, Long commentId) {
        return activityRepo.existsByMemberIdAndRefIdAndTypeName(memberId, commentId, ActivityType.TypeName.COMMENT_LIKE);
    }

    @Override
    @Transactional
    public ParentCommentDTO likeComment(Long memberId, Long commentId) {
        // 회원 활동 유형 조회
        ActivityType activityType = activityTypeRepo.findByName(ActivityType.TypeName.COMMENT_LIKE)
                .orElseThrow(() -> new EntityNotFoundException("회원 활동 유형을 찾을 수 없습니다."));

        // 회원 활동 저장
        Member member = Member.builder().id(memberId).build();
        MemberActivity activity = MemberActivity.create(activityType, member, commentId);
        activityRepo.save(activity);

        // 댓글 좋아요 수 업데이트
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        comment.setLikeCount(comment.getLikeCount() + 1);
        return ParentCommentDTO.from(commentRepo.save(comment), true);
    }

    @Override
    @Transactional
    public ParentCommentDTO unlikeComment(Long memberId, Long commentId) {
        // 회원 활동 삭제
        activityRepo.deleteByMemberIdAndRefIdAndTypeName(memberId, commentId, ActivityType.TypeName.COMMENT_LIKE);

        // 댓글 좋아요 수 업데이트
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        comment.setLikeCount(comment.getLikeCount() - 1);
        return ParentCommentDTO.from(commentRepo.save(comment), false);
    }

    @Override
    public List<Long> getPostIdsByLikeOrScrap(Long memberId, ActivityType.TypeName typeName) {
        return activityRepo.getRefIdsByMemberIdAndTypeName(memberId, typeName);
    }

}
