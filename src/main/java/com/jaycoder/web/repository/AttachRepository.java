package com.jaycoder.web.repository;

//import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jaycoder.web.model.Attach;

public interface AttachRepository extends JpaRepository<Attach, Long> {
		//	repository 함수만 정의 해놓으면 된다.

		
}
