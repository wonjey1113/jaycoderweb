package com.jaycoder.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import com.google.gson.JsonObject;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.BoardRepository;
import com.jaycoder.web.repository.UserRepository;

@RestController
@RequestMapping("/api")
class BoardApiController {

	@Autowired
  private BoardRepository repository;
	
	@Autowired
	private UserRepository userRepository;

  @GetMapping("/boards")
  List<Board> all(@RequestParam(required = false, defaultValue = "") String title, 
  		@RequestParam(required = false, defaultValue = "") String content) {
  		if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
  				return repository.findAll();			
  		}else {
  				return repository.findByTitleOrContent(title, content);
  		}
  }

  @PostMapping("/boards")
  Board newBoard(@RequestBody Board newBoard) {
    return repository.save(newBoard);
  }

  // Single item

  @GetMapping("/boards/{id}")
  Board one(@PathVariable Long id) {

    	return repository.findById(id).orElseThrow(null);
//  		return repository.findById(id).orElseThrow(()-> new IllegalArgumentException());
  }

  @PutMapping("/boards/{id}")
  Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {

    return repository.findById(id)
      .map(board -> {
        board.setTitle(newBoard.getTitle());
        board.setContent(newBoard.getContent());
        return repository.save(board);
      })
      .orElseGet(() -> {
        newBoard.setId(id);
        return repository.save(newBoard);
      });
  }

  @DeleteMapping("/boards/{id}")
  void deleteBoard(@PathVariable Long id) {
    repository.deleteById(id);
  }
  
  @GetMapping("/boards/{id}/security")
  public String securityChking(@PathVariable Long id) {

	 	String  getname = SecurityContextHolder.getContext().getAuthentication().getName();	 						 				  
	 	User principalUser = userRepository.findByUsername(getname);			  	
  	Board board = repository.findById(id).get();
  	
    JsonObject obj =new JsonObject();
		obj.addProperty("security", board.getSecret_yn()); 
		if(board.getSecret_yn().equals("N")) {
			obj.addProperty("auth","allow");
		}else {
			if(!board.isSameWriter(principalUser)) {
				obj.addProperty("auth","not_allow");
			}	else {
				obj.addProperty("auth","allow");
			}			
		}

    return obj.toString();
  }
  
  
}
