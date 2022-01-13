package com.guestbook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.guestbook.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	Optional<User> findByEmailId(String emailId);

	List<User> findAllByRole(String role);
}
