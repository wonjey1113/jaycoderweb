package com.jaycoder.web.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(value = "roles")
public class User extends AbstractEntity {

		private String username;
		@JsonIgnore
		private String password;
		@JsonIgnore
		private Boolean enabled;
		
		@ManyToMany
		@JoinTable(
		  name = "user_role", 
		  joinColumns = @JoinColumn(name="user_id"), 
		  inverseJoinColumns = @JoinColumn(name = "role_id"))
		@JsonIgnore
		private List<Role> Roles = new ArrayList<>();
		
		// 사용자 조회할때 게시글도 나오게 할수 있다.
		// 사용자 입장에서는 OneToMany
		// 기본적으로 외부 엔티티는 저장되지 않지만, 
		//  cascade = CascadeType.ALL 을 주면 외부 엔티티도 저장된다. 
		// @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // 매핑할 필드명을 설정,게시글과 연결하니깐 Board 객체에 user(join column) 필드와 매핑한다.
		@OneToMany(mappedBy = "user") 
    @JsonIgnore
		private List<Board> boards = new ArrayList<>();  // 게시글은 여러개일수 있으니 배열리스트로 설정한다.

		@Override
		public String toString() {
			return "User [" + super.toString() + ", username=" + username + ", password=" + password + ", enabled=" + enabled + ", Roles=" + Roles
					+ ", boards=" + boards + "]";
		}

		
}
