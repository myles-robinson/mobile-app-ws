package com.myles.app.ws.repository;

import com.myles.app.ws.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

// extend crudRepository instead of creating dao to persist data to db

@Repository         // Used for pagination, not return possible thousands of records at once
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
}
