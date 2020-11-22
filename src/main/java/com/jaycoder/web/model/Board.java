package com.jaycoder.web.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@Lob
		private String content;
		
		@NotNull
		@Size(min = 2, max = 30, message = "제목은 2자 이상 30자 이하입니다.")
		private String title;
		
    
		
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
		
  

		@OneToMany(mappedBy = "board")
		@OrderBy("id ASC")
		@JsonIgnore
		private List<Reply> replys;
		
}
