package com.jaycoder.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jaycoder.web.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
		//	repository 함수만 정의 해놓으면 된다.
		List<Board> findByTitle(String title); 
		List<Board> findByTitleOrContent(String title, String content); 
		
		Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}
