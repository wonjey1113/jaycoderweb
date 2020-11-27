package com.jaycoder.web.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Reply extends AbstractEntity {
			
			@Lob
			private String content;
				
			@ManyToOne // 댓글은 ManyToOne
			@JoinColumn(name = "user_id")
			//@JoinColumn(foreignKey = @ForeignKey(name = "FK_user"))
			//@JsonIgnore
			private User user;
			
			@ManyToOne
			@JoinColumn(name = "board_id")		
			//@JoinColumn(foreignKey = @ForeignKey(name = "FK_reply_board"))
			//@JsonIgnore
 			private Board board ;
			
			public Reply() {	}
		
			public Reply(String content, User user, Board board) {
					this.content = content;
					this.user = user;
					this.board = board;
			} 

			public boolean isSameWriter(User principalUser) {
					return principalUser.equals(this.user);
			}			
			
			
			
			@Override
			public String toString() {
					return "Reply [" + super.toString() + ", content=" + content + ", user=" + user + "]";
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = super.hashCode();
				result = prime * result + ((user == null) ? 0 : user.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (!super.equals(obj))
					return false;
				if (getClass() != obj.getClass())
					return false;
				Reply other = (Reply) obj;
				if (user == null) {
					if (other.user != null)
						return false;
				} else if (!user.equals(other.user))
					return false;
				return true;
			}
	

	
}
