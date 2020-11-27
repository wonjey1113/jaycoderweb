package com.jaycoder.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jaycoder.web.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
		//	repository 함수만 정의 해놓으면 된다.
		User findByUsername(String username);

}
