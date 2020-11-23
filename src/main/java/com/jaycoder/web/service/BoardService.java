package com.jaycoder.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.BoardRepository;
import com.jaycoder.web.repository.UserRepository;

@Service
public class BoardService {
		
		@Autowired
		private BoardRepository boardRepository;
		
		@Autowired(required = true)
		private UserRepository userRepository;
		
		public Board save(String username, Board board) {
			  //System.err.println("username:"+username);
				User user = userRepository.findByUsername(username);
				board.setUser(user);
//				board.setCount_of_reply(0);
//			  Timestamp createdate = new Timestamp(System.currentTimeMillis());
//				board.setCreatedate(createdate);
				return boardRepository.save(board);
		}
}
