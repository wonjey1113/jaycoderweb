package com.jaycoder.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jaycoder.web.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
		//	repository 함수만 정의 해놓으면 된다.
//		List<Board> findByTitle(String title); 
//		List<Board> findByTitleOrContent(String title, String content); 
//		
//		Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}
