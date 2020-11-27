package com.jaycoder.web.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Role {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private String name;

		@ManyToMany(mappedBy = "role")
		@JsonIgnore // 재귀적 호출 방지 - Roles 호출시  이 Roles를 가지는 사용자 정보는 skip 한다.
		private List<User> users;
		
}
