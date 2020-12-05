package com.jaycoder.web.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.Reply;
import com.jaycoder.web.model.Result;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.BoardRepository;
import com.jaycoder.web.repository.ReplyRepository;
import com.jaycoder.web.repository.UserRepository;
import com.jaycoder.web.service.ReplyService;

@RestController  
@RequestMapping("/api/board/{boardId}/replys")
public class ReplyApiController {
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyService replyService ;
	
	@PostMapping("")
	public String create(@PathVariable Long boardId, String content, Authentication authentication, Principal principal) {
			String username = authentication.getName();
			User user = userRepository.findByUsername(username);
			Board board = boardRepository.findById(boardId).get(); 
			Reply  reply = new Reply(content, user, board);
			board.addReply();
		  replyRepository.save(reply);
			
      JsonObject obj =new JsonObject();      
      obj.addProperty("count_of_reply", board.getCount_of_reply());
      obj.addProperty("createdate", reply.getFormattedCreateDate());
      obj.addProperty("username", username);
      obj.addProperty("boardid", boardId);
      obj.addProperty("id", reply.getId());
      obj.addProperty("content", content);
      return obj.toString();
			
	}
					
	@GetMapping("/{id}")
	public String updateForm(@PathVariable Long boardId, @PathVariable Long id, Authentication auth) {	
		 	User principalUser = userRepository.findByUsername(auth.getName());
		 	Reply reply = replyRepository.findById(id).get();
			if(!reply.isSameWriter(principalUser)) {
					return null;
			}
			
      JsonObject obj =new JsonObject();      
      obj.addProperty("boardid", boardId);
      obj.addProperty("id", id);
      obj.addProperty("content", reply.getContent());
      return obj.toString();			
	}
	
	@PutMapping("/{id}")
	public Reply update(@PathVariable Long boardId, @PathVariable Long id, String content,  Authentication auth) {
		 	User principalUser = userRepository.findByUsername(auth.getName());
		 	Reply reply = replyRepository.findById(id).get();
			if(!reply.isSameWriter(principalUser)) {
					return null;
			}				
			return replyService.update(boardId, id, content); 		
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
