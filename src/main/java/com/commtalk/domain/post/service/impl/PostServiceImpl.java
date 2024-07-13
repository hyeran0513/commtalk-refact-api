package com.commtalk.domain.post.service.impl;

import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.domain.post.dto.PostDTO;
import com.commtalk.domain.post.dto.PostPreviewDTO;
import com.commtalk.domain.post.dto.request.PostCreateRequest;
import com.commtalk.domain.post.dto.PostPageDTO;
import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.post.entity.Post;
import com.commtalk.domain.post.entity.PostHashtag;
import com.commtalk.domain.post.exception.PostIdNullException;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepo;
    private final PostHashtagRepository hashtagRepo;

    @Override
    public PostPageDTO getPosts(Pageable pageable) {
        // 페이지에 해당하는 게시글 목록 조회
        Page<Post> postPage = postRepo.findAllOrderByUpdatedAt(pageable);
        return PostPageDTO.of(postPage);
    }

    @Override
    public PostPageDTO getPostsByBoard(Long boardId, Pageable pageable) {
        // 페이지에 해당하는 게시판 게시글 목록 조회
        Page<Post> postPage = postRepo.findByBoardIdOrderByUpdatedAt(boardId, pageable);
        return PostPageDTO.of(postPage);
    }

    @Override
    public PostPageDTO getPostsByKeyword(String keyword, Pageable pageable) {
        // 제목 또는 내용에 키워드가 포함되는 게시글 목록 조회
        Page<Post> postPage = postRepo.findByKeywordOrderByUpdateAt(keyword, pageable);
        return PostPageDTO.of(postPage);
    }

    @Override
    public PostPageDTO getPostsByBoardAndKeyword(Long boardId, String keyword, Pageable pageable) {
        // 제목 또는 내용에 키워드가 포함되는 게시판 게시글 목록 조회
        Page<Post> postPage = postRepo.findByBoardAndKeywordOrderByUpdateAt(boardId, keyword, pageable);
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
        return PostDTO.from(post, hashtags);
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
        Page<Post> postPage = postRepo.findByBoardIdOrderByViewCount(boardId, PageRequest.of(0, size));
        List<Post> postList = postPage.getContent();

        return postList.stream()
                .map(PostPreviewDTO::of)
                .toList();
    }

    @Override
    public List<PostPreviewDTO> getPostPreviewsByViews() {
        Page<Post> postPage = postRepo.findAllByOrderByViewCountDesc(PageRequest.of(0, 3));
        List<Post> postList = postPage.getContent();

        return postList.stream()
                .map(PostPreviewDTO::of)
                .toList();
    }

    @Override
    @Transactional
    public void createPost(Long memberId, Long boardId, PostCreateRequest createReq) {
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
    }

}
