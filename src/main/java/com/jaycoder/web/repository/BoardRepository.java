package com.jaycoder.web.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jaycoder.web.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
		//	repository 함수만 정의 해놓으면 된다.
		List<Board> findByTitle(String title); 
		List<Board> findByTitleOrContent(String title, String content); 
		
		// 제;목+내용 검색
		Page<Board> findByTitleContainingOrContentContainingOrderByCreatedateDesc(String title, String content, Pageable pageable  );
		
		// 제목 검색
		Page<Board> findByTitleContainingOrderByCreatedateDesc(String title, Pageable pageable);
		
		// 내용 검색
		Page<Board> findByContentContainingOrderByCreatedateDesc(String content, Pageable pageable);
		
		// 작성자 검색?
		//Page<Board> findByUser_idContainingOrderByCreatedateDesc(Long user_id,  Pageable pageable);
    //@Query("SELECT * FROM Board b WHERE b.user_id = :uid")
		@Query(value = "SELECT * FROM Board WHERE user_id = :uid", nativeQuery = true)
    Page<Board> findByUid(@Param("uid") Long uid, Pageable pageable);
		
		@Query(value = "SELECT * FROM board WHERE user_id = (SELECT id FROM user WHERE username = :username) order by createdate desc", nativeQuery = true)
		Page<Board> findByUsername(@Param("username") String username, Pageable pageable);
		
		// 일반글 기본(제목) 검색 
		@Query(value = "SELECT * FROM board WHERE notice_yn = 'N' and title like %:keyword% order by createdate desc ", nativeQuery = true)
		Page<Board> findByGeneralList(@Param("keyword") String keyword, Pageable pageable);
		
		// 일반글 내용 검색 
		@Query(value = "SELECT * FROM board WHERE notice_yn = 'N' and content like %:keyword% order by createdate desc ", nativeQuery = true)
		Page<Board> findByGeneralContentList(@Param("keyword") String keyword, Pageable pageable);		
		
		// 일반글 제목+내용 감색
		@Query(value = "SELECT * FROM board WHERE notice_yn = 'N' and title like %:keyword% or content like %:keyword% order by createdate desc ", nativeQuery = true)
		Page<Board> findByGeneralTitleOrContentList(@Param("keywrod") String keyword, Pageable pageable);
		
		// 일반글 작성자 검색
		@Query(value = "SELECT * FROM board WHERE notice_yn = 'N' and  user_id = (SELECT id FROM user WHERE username = :username) order by createdate desc", nativeQuery = true)
		Page<Board> findByGeneralUsername(@Param("username") String username, Pageable pageable);
		
}
