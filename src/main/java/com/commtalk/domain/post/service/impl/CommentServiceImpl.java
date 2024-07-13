package com.commtalk.domain.post.service.impl;

import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.post.dto.ChildCommentDTO;
import com.commtalk.domain.post.dto.request.CommentCreateRequest;
import com.commtalk.domain.post.dto.ParentCommentDTO;
import com.commtalk.domain.post.entity.Comment;
import com.commtalk.domain.post.entity.Post;
import com.commtalk.domain.post.exception.CommentIdNullException;
import com.commtalk.domain.post.repository.CommentRepository;
import com.commtalk.domain.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;

    @Override
    public List<ParentCommentDTO> getCommentsByPost(Long postId) {
        List<Comment> commentList = commentRepo.findByPostId(postId);
        Map<Long, ParentCommentDTO> commentDtoMap = new HashMap<>();

        for (Comment comment : commentList) {
            if (comment.getParent() == null) {
                commentDtoMap.put(comment.getId(), ParentCommentDTO.from(comment));
            } else {
                ParentCommentDTO parentCommentDto = commentDtoMap.get(comment.getParent().getId());
                if (parentCommentDto != null) {
                    parentCommentDto.addChildComment(ChildCommentDTO.from(comment));
                    commentDtoMap.put(comment.getParent().getId(), parentCommentDto);
                }
            }
        }

        return commentDtoMap.values().stream()
                .peek(commentDto -> commentDto.setChildCount((commentDto.getChildren() != null) ? commentDto.getChildren().size() : 0))
                .toList();
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

}
