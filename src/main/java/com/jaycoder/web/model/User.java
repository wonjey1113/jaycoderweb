package com.jaycoder.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

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
		
}
