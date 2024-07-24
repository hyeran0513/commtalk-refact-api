package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {

    Optional<ActivityType> findByTypeName(ActivityType.Activity typeName);

}
