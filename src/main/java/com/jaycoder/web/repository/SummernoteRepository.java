package com.jaycoder.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
//import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jaycoder.web.model.Summernote;

public interface SummernoteRepository extends JpaRepository<Summernote, Long> {

	//	repository 함수만 정의 해놓으면 된다.

	@Query(value = "DELETE  FROM summernote WHERE board_idx = :uid", nativeQuery = true)
  void deleteFiles(@Param("uid") Long uid);
	
	@Query(value = "UPDATE  summernote SET board_idx = :bid  WHERE  board_idx = 0", nativeQuery = true)
	void updateSummernoteIdx(@Param("bid") Long bid);

	@Query(value = "SELECT * FROM summernote WHERE board_idx = :bid", nativeQuery = true)
	List<Summernote>	 findByBoard_idx(@Param("bid") Long bid);
		
}
