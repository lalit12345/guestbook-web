package com.guestbook.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.guestbook.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	Optional<User> findByEmailId(String emailId);

	Page<User> findAllByRoleAndDeleteFlag(String role, boolean deleteFlag, Pageable pageable);
}
