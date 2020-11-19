package com.jaycoder.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class User {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private String username;
		private String password;
		private Boolean enabled;
		@ManyToMany
		@JoinTable(
		  name = "user_role", 
		  joinColumns = @JoinColumn(name="user_id"), 
		  inverseJoinColumns = @JoinColumn(name = "role_id"))			
		private List<Role> Roles = new ArrayList<>();
		
		// 사용자 조회할때 게시글도 나오게 할수 있다.
		// 사용자 입장에서는 OneToMany
		// 기본적으로 외부 엔티티는 저장되지 않지만, 
		//  cascade = CascadeType.ALL 을 주면 외부 엔티티도 저장된다. 
		@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // 매핑할 필드명을 설정,게시글과 연결하니깐 Board 객체에 user(join column) 필드와 매핑한다.
		private List<Board> boards = new ArrayList<>();  // 게시글은 여러개일수 있으니 배열리스트로 설정한다.

}
