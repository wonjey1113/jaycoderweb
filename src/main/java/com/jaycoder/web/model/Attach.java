package com.jaycoder.web.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Attach extends AbstractEntity {
		/* 파일 번호 PK - AbstractEntity 상속으로 제외 */
	
		/* 게시글 번호  FK */
	  //private Long board_idx;
	  
	  @ManyToOne   //다대일(회원과 팀은 N:1 관계)
	  @JoinColumn(name = "board_idx")  //조인컬럼은 외래키를 매핑할때 사용 (연관관계주인)
	  private Board board2;  //연관관계 주인 필드	  
	  
		/** 원본 파일명 */
		private String original_name;

		/** 저장 파일명 */
		private String save_name;

		/** 파일 크기 */
		private long size;
		
		/** 삭제 여부 */
		private String delete_yn = "N";
		
		/** 삭제일 */
		private LocalDateTime deletetime;
		
		/* 생성 날짜  createdate  - AbstractEntity 상속으로 제외 */
		
		/* 갱신 날짜 modifieddate; - AbstractEntity 상속으로 제외 */
		
}
