package com.jaycoder.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaycoder.web.model.Board;

public interface BoardRespository extends JpaRepository<Board, Long> {

}
