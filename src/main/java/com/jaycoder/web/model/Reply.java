package com.jaycoder.web.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;

@Entity
@Data
public class Reply {
			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			private Long id;
			
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
			
			@CreationTimestamp
			private Timestamp createdate;
			
			public Reply() {	}
		
			public Reply(String content, User user, Board board) {
				this.content = content;
				this.user = user;
				this.board = board;
			} 
		
			@Override
			public String toString() {
				return "Reply [id=" + id + ", content=" + content + ", user=" + user + ", createdate=" + createdate + "]";
			}
	
			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				Reply other = (Reply) obj;
				if (id == null) {
					if (other.id != null)
						return false;
				} else if (!id.equals(other.id))
					return false;
				return true;
			}
	
			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((id == null) ? 0 : id.hashCode());
				return result;
			}


		
	
}
