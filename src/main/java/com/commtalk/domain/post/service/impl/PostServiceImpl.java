package com.commtalk.domain.post.service.impl;

import com.commtalk.domain.post.dto.CreatePostDTO;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepo;
    private final PostHashtagRepository hashtagRepo;

    @Override
    public PostPageDTO getPostsByBoard(Long boardId, Pageable pageable) {
        // 페이지에 해당하는 게시글 목록 조회
        Page<Post> postPage = postRepo.findByBoardIdOrderByUpdatedAt(boardId, pageable);
        return PostPageDTO.of(postPage);
    }

    @Override
    @Transactional
    public void createPost(Long memberId, Long boardId, CreatePostDTO postDto) {
        // 게시글 생성
        Member member = Member.builder().id(memberId).build();
        Board board = Board.builder().id(boardId).build();

        Post post = Post.create(member, board, postDto);
        Post newPost = postRepo.save(post);
        if (newPost.getId() == null) {
            throw new PostIdNullException("게시글 생성에 실패했습니다.");
        }

        // 게시글 해시태그 생성
        PostHashtag postHashtag;
        for (String hashtag : postDto.getHashtags()) {
            postHashtag = PostHashtag.create(newPost, hashtag);
            hashtagRepo.save(postHashtag);
        }
    }

}
