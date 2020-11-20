package com.jaycoder.web.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Board {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@NotNull
		@Size(min = 2, max = 30, message = "제목은 2자 이상 30자 이하입니다.")
		private String title;
		
		private String content;
		
		@ManyToOne // 게시글은 ManyToOne
		@JoinColumn(name = "user_id")
		@JsonIgnore // 재귀적 호출 방지 - Board 호출시  이 게시글이 가지는 사용자 정보는 skip 한다.
		private User user;
		
		@CreationTimestamp
		private Timestamp createdate;
		private int hit;
		public boolean isSameWriter(User loginUser) {
				return this.user.equals(loginUser);
		}
		
		
		
}
