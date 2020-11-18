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
		@ManyToOne
		@JoinColumn(name = "user_id")
		@JsonIgnore
		private User user;
		@CreationTimestamp
		private Timestamp createdate;
		private int hit;
}
