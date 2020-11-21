package com.jaycoder.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jaycoder.web.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
		//	repository 함수만 정의 해놓으면 된다.

}
