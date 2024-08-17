package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {

    List<PostHashtag> findAllByPostId(Long postId);

    void deleteAllByPostId(Long postId);

}
