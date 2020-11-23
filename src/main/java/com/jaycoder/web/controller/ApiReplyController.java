package com.jaycoder.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.Reply;
import com.jaycoder.web.model.Result;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.BoardRepository;
import com.jaycoder.web.repository.ReplyRepository;
import com.jaycoder.web.repository.UserRepository;

@RestController  
@RequestMapping("/api/board/{boardId}/replys")
public class ApiReplyController {
	
		@Autowired
		private ReplyRepository replyRepository;
		
		@Autowired
		private UserRepository userRepository;
		
		@Autowired
		private BoardRepository boardRepository;
		
		@PostMapping("")
		public Reply create(@PathVariable Long boardId, String content, Authentication authentication) {
				String username = authentication.getName();
				User user = userRepository.findByUsername(username);
				Board board = boardRepository.findById(boardId).get(); 
				Reply  reply = new Reply(content, user, board);
				board.addReply();
				return replyRepository.save(reply);
		}

		@DeleteMapping("/{id}")
		public Result delete(@PathVariable Long boardId, @PathVariable Long id, Authentication authentication) {
				String username = authentication.getName();				
				Reply reply = replyRepository.findById(id).get();
			 	User principalUser = userRepository.findByUsername(username);
			 	//principalUser.setUsername(username);
			 	//principalUser.setId(id);
				if(!reply.isSameWriter(principalUser)) {
						return Result.fail(-1,"자신의 글만 삭제 할 수 있습니다.");
				}											
				replyRepository.deleteById(id);				
				Board board = boardRepository.getOne(boardId);
				board.deleteReply();
				boardRepository.save(board);
				return Result.ok(board.getCount_of_reply());				
		}
		
}
