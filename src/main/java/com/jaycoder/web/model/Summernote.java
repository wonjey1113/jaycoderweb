package com.jaycoder.web.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Summernote extends AbstractEntity {
		/* 파일 번호 PK - AbstractEntity 상속으로 제외 */
	
		/* 게시글 번호  FK */
	  private Long board_idx;
	   
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
		
		public String getUploadImagePath() {
				if(save_name == null || modifieddate == null) return null;		
				return "/upload/summernote_image/"+ save_name;
		}		
		

		
}
