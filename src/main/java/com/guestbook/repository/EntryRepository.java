package com.guestbook.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.guestbook.entity.Entry;

@Repository
public interface EntryRepository extends PagingAndSortingRepository<Entry, Integer> {
}
