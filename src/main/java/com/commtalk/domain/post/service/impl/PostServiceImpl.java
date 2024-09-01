package com.commtalk.domain.post.service.impl;

import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.common.exception.PermissionException;
import com.commtalk.domain.post.dto.PostDTO;
import com.commtalk.domain.post.dto.PostPreviewDTO;
import com.commtalk.domain.post.dto.request.PostCreateRequest;
import com.commtalk.domain.post.dto.PostPageDTO;
import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.post.dto.request.PostUpdateRequest;
import com.commtalk.domain.post.entity.*;
import com.commtalk.domain.post.exception.PostIdNullException;
import com.commtalk.domain.post.repository.ActivityTypeRepository;
import com.commtalk.domain.post.repository.MemberActivityRepository;
import com.commtalk.domain.post.repository.PostHashtagRepository;
import com.commtalk.domain.post.repository.PostRepository;
import com.commtalk.domain.post.service.PostService;
import com.commtalk.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepo;
    private final PostHashtagRepository hashtagRepo;
    private final ActivityTypeRepository activityTypeRepo;
    private final MemberActivityRepository activityRepo;
    private final PostHashtagRepository postHashtagRepo;

    @Override
    public PostPageDTO getPosts(Pageable pageable) {
        // 페이지에 해당하는 게시글 목록 조회
        Page<Post> postPage = postRepo.findAllOrderByUpdatedAt(pageable, false);
        return PostPageDTO.of(postPage);
    }

    @Override
    public PostPageDTO getPostsByBoard(Long boardId, Pageable pageable) {
        // 페이지에 해당하는 게시판 게시글 목록 조회
        Page<Post> postPage = postRepo.findByBoardIdOrderByUpdatedAt(boardId, pageable, false);
        return PostPageDTO.of(postPage);
    }

    @Override
    public PostPageDTO getPostsByKeyword(String keyword, Pageable pageable) {
        // 제목 또는 내용에 키워드가 포함되는 게시글 목록 조회
        Page<Post> postPage = postRepo.findByKeywordOrderByUpdateAt(keyword, pageable, false);
        return PostPageDTO.of(postPage);
    }

    @Override
    public PostPageDTO getPostsByBoardAndKeyword(Long boardId, String keyword, Pageable pageable) {
        // 제목 또는 내용에 키워드가 포함되는 게시판 게시글 목록 조회
        Page<Post> postPage = postRepo.findByBoardAndKeywordOrderByUpdateAt(boardId, keyword, pageable, false);
        return PostPageDTO.of(postPage);
    }

    @Override
    @Transactional
    public PostDTO getPost(Long postId) {
        // 게시글 조회
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        List<PostHashtag> hashtags = hashtagRepo.findAllByPostId(postId);

        // 조회수 증가
        post.setViewCount(post.getViewCount() + 1);
        postRepo.save(post);
        return PostDTO.from(post, hashtags, false, false);
    }

    @Override
    @Transactional
    public PostDTO getPost(Long postId, Long memberId) {
        // 게시글 조회
        Object[] postObj = postRepo.findById(postId, memberId, ActivityType.TypeName.POST_LIKE,
                        ActivityType.TypeName.POST_SCRAP, false)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        postObj = (Object[]) postObj[0];

        List<PostHashtag> hashtags = hashtagRepo.findAllByPostId(postId);
        Post post = (Post) postObj[0];
        boolean likeYN = postObj[1] != null;
        boolean scrapYN = postObj[2] != null;

        // 조회수 증가
        post.setViewCount(post.getViewCount() + 1);
        postRepo.save(post);

        return PostDTO.from(post, hashtags, likeYN, scrapYN);
    }

    @Override
    public void isExistsPost(Long postId) {
        if (!postRepo.existsById(postId)) {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다.");
        }
    }

    @Override
    public List<PostPreviewDTO> getPostPreviewsByBoard(Long boardId, int size) {
        // size 만큼 게시글 미리보기 목록 조회
        Page<Post> postPage = postRepo.findByBoardIdOrderByViewCount(boardId, PageRequest.of(0, size), false);
        List<Post> postList = postPage.getContent();

        return postList.stream()
                .map(PostPreviewDTO::of)
                .toList();
    }

    @Override
    public List<PostPreviewDTO> getPostPreviewsByViews() {
        Page<Post> postPage = postRepo.findAllByDeletedYNOrderByViewCountDesc(PageRequest.of(0, 3), false);
        List<Post> postList = postPage.getContent();

        return postList.stream()
                .map(PostPreviewDTO::of)
                .toList();
    }

    @Override
    @Transactional
    public Long createPost(Long memberId, Long boardId, PostCreateRequest createReq) {
        // 게시글 생성
        Member member = Member.builder().id(memberId).build();
        Board board = Board.builder().id(boardId).build();

        Post post = Post.create(member, board, createReq);
        Post newPost = postRepo.save(post);
        if (newPost.getId() == null) {
            throw new PostIdNullException("게시글 생성에 실패했습니다.");
        }

        // 게시글 해시태그 생성
        PostHashtag postHashtag;
        for (String hashtag : createReq.getHashtags()) {
            postHashtag = PostHashtag.create(newPost, hashtag);
            hashtagRepo.save(postHashtag);
        }

        return newPost.getId();
    }

    @Override
    @Transactional
    public void updatePost(Long memberId, Long postId, PostUpdateRequest updateReq) {
        // 게시글 조회
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!memberId.equals(post.getAuthor().getId())) {
            throw new PermissionException("작성자만 게시글 수정이 가능합니다.");
        }

        // 게시글 수정
        post.setTitle(updateReq.getTitle());
        post.setContent(updateReq.getContent());
        post.setAnonymousYN(updateReq.isAnonymousYN());
        post.setCommentableYN(updateReq.isCommentableYN());

        // 수정된 게시글 저장
        postRepo.save(post);

        // 이전 게시글 해시태그 삭제
        postHashtagRepo.deleteAllByPostId(postId);

        // 게시글 해시태그 생성
        PostHashtag postHashtag;
        for (String hashtag : updateReq.getHashtags()) {
            postHashtag = PostHashtag.create(post, hashtag);
            hashtagRepo.save(postHashtag);
        }
    }

    @Override
    @Transactional
    public void deletePost(Long memberId, Long postId) {
        // 게시글 조회
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!memberId.equals(post.getAuthor().getId())) {
            throw new PermissionException("작성자만 게시글 삭제가 가능합니다.");
        }

        // 게시글의 deletedYN 컬럼 값을 true로 변경
        post.setDeletedYN(true);

        // 수정된 게시글 저장
        postRepo.save(post);
    }

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

}
