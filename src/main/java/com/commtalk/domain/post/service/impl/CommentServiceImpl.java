package com.commtalk.domain.post.service.impl;

import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.post.dto.ChildCommentDTO;
import com.commtalk.domain.post.dto.request.CommentCreateRequest;
import com.commtalk.domain.post.dto.ParentCommentDTO;
import com.commtalk.domain.post.dto.request.CommentUpdateRequest;
import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.entity.Comment;
import com.commtalk.domain.post.entity.MemberActivity;
import com.commtalk.domain.post.entity.Post;
import com.commtalk.domain.post.exception.CommentIdNullException;
import com.commtalk.common.exception.PermissionException;
import com.commtalk.domain.post.repository.ActivityTypeRepository;
import com.commtalk.domain.post.repository.CommentRepository;
import com.commtalk.domain.post.repository.MemberActivityRepository;
import com.commtalk.domain.post.service.CommentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;
    private final ActivityTypeRepository activityTypeRepo;
    private final MemberActivityRepository activityRepo;

    @Override
    public List<ParentCommentDTO> getCommentsByPost(Long postId) {
        List<Comment> commentList = commentRepo.findByPostIdAndDeletedYN(postId, false);
        Map<Long, ParentCommentDTO> commentDtoMap = new HashMap<>();

        for (Comment comment : commentList) {
            if (comment.getParent() == null) {
                commentDtoMap.put(comment.getId(), ParentCommentDTO.from(comment, false));
            } else {
                ParentCommentDTO parentCommentDto = commentDtoMap.get(comment.getParent().getId());
                if (parentCommentDto != null) {
                    parentCommentDto.addChildComment(ChildCommentDTO.from(comment, false));
                    commentDtoMap.put(comment.getParent().getId(), parentCommentDto);
                }
            }
        }

        return commentDtoMap.values().stream()
                .peek(commentDto -> commentDto.setChildCount((commentDto.getChildren() != null) ? commentDto.getChildren().size() : 0))
                .toList();
    }

    @Override
    public List<ParentCommentDTO> getCommentsByPost(Long postId, Long memberId) {
        List<Object[]> commentList = commentRepo.findByPostIdAndDeletedYN(postId, false, memberId, ActivityType.TypeName.COMMENT_LIKE);
        Map<Long, ParentCommentDTO> commentDtoMap = new HashMap<>();

        for (Object[] commentObj : commentList) {
            Comment comment = (Comment) commentObj[0];
            boolean likeYN = commentObj[1] != null;

            if (comment.getParent() == null) {
                commentDtoMap.put(comment.getId(), ParentCommentDTO.from(comment, likeYN));
            } else {
                ParentCommentDTO parentCommentDto = commentDtoMap.get(comment.getParent().getId());
                if (parentCommentDto != null) {
                    parentCommentDto.addChildComment(ChildCommentDTO.from(comment, likeYN));
                    commentDtoMap.put(comment.getParent().getId(), parentCommentDto);
                }
            }
        }

        return commentDtoMap.values().stream()
                .peek(commentDto -> commentDto.setChildCount((commentDto.getChildren() != null) ? commentDto.getChildren().size() : 0))
                .toList();
    }

    @Override
    public long getCommentCountByPost(Long postId) {
        return commentRepo.countByPostIdAndDeletedYN(postId, false);
    }

    @Override
    public void createComment(Long memberId, Long postId, CommentCreateRequest createReq) {
        // 댓글 생성
        Member member = Member.builder().id(memberId).build();
        Post post = Post.builder().id(postId).build();

        Comment comment = Comment.create(member, post, createReq);
        if (createReq.getParentId() > 0) {
            Comment parent = commentRepo.findById(createReq.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("상위 댓글을 찾을 수 없습니다."));
            comment.setParent(parent);
        }

        Comment newComment = commentRepo.save(comment);
        if (newComment.getId() == null) {
            throw new CommentIdNullException("게시글 댓글 생성에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public void updateComment(Long memberId, Long commentId, CommentUpdateRequest updateReq) {
        // 댓글 조회
        Comment comment = commentRepo.findByIdWithWriter(commentId, false)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        if (!memberId.equals(comment.getWriter().getId())) {
            throw new PermissionException("작성자만 댓글 수정이 가능합니다.");
        }
        
        // 댓글 수정
        comment.setContent(updateReq.getContent());
        comment.setAnonymousYN(updateReq.isAnonymousYN());

        // 수정된 댓글 저장
        commentRepo.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        // 댓글 조회
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        if (!memberId.equals(comment.getWriter().getId())) {
            throw new PermissionException("작성자만 댓글 삭제가 가능합니다.");
        }
        
        // 댓글의 deletedYN 컬럼 값을 true로 변경
        comment.setDeletedYN(true);

        // 수정된 댓글 저장
        commentRepo.save(comment);
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

}
