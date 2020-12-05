package com.jaycoder.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
//import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jaycoder.web.model.Attach;
import com.jaycoder.web.model.Board;

public interface AttachRepository extends JpaRepository<Attach, Long> {

	//	repository 함수만 정의 해놓으면 된다.
	List<Attach> findByBoard2(Board board);

	@Query(value = "DELETE  FROM attach WHERE board_idx = :uid", nativeQuery = true)
  void deleteFiles(@Param("uid") Long uid);
		
}
