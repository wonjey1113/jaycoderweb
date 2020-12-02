package com.jaycoder.web.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Board extends AbstractEntity {		
		@Lob
		private String content;
		
		private Integer count_of_reply = 0;		
		
		@NotNull
		@Size(min = 2, max = 30, message = "제목은 2자 이상 30자 이하입니다.")
		private String title;
		    		
		@ManyToOne // 게시글은 ManyToOne
		@JoinColumn(name = "user_id")
		@JsonIgnore // 재귀적 호출 방지 - Board 호출시  이 게시글이 가지는 사용자 정보는 skip 한다.
		private User user;

		@OneToMany(mappedBy = "board2")  // Member 객체와 양방향 관계를 만들기위해 추가한다. (대상테이블)
		private List<Attach> attachs = new ArrayList<Attach>();		
		
		private int hit;
				
		@OneToMany(mappedBy = "board")
		@OrderBy("id ASC")
		@JsonIgnore
		private List<Reply> replys;

		public void addReply() {
				this.count_of_reply += 1;
		}
		
		public void deleteReply() {
				this.count_of_reply -= 1;
		}

		public boolean isSameWriter(User loginUser) {
			return this.user.equals(loginUser);
		}		
		
}
