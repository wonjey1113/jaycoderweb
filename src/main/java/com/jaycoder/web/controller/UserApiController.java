package com.jaycoder.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.UserRepository;

@RestController
@RequestMapping("/api")
class UserApiController {

	@Autowired
  private UserRepository repository;

  @GetMapping("/users")
  List<User> all() {
 				return repository.findAll();			
  }

  @PostMapping("/users")
  User newUser(@RequestBody User newUser) {
    return repository.save(newUser);
  }

  // Single item

  @GetMapping("/users/{id}")
  User one(@PathVariable Long id) {

    	return repository.findById(id).orElseThrow(null);
//  		return repository.findById(id).orElseThrow(()-> new IllegalArgumentException());
  }

  @PutMapping("/users/{id}")
  User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

    return repository.findById(id)
      .map(user -> {
   		
      	// user 객체에 boards 필드에 추가
      	//  orphanRemoval = false 이거나 이 옵션을 않줬다면
      	//  user.setBoards(newUser.getBoards());  을 사용해서 새 boards를 추가하고
      	//  (user 정보로 게시글 추가)
      	//  orphanRemoval = true 을 주면  user 객체에서 boards 를 클리어 하고 
      	//  새 boards 를 등록해주면 에러가 없다.  아래 코드 57~58라인과 같다.
      	//  기존 user 게시글은 삭제되고 새 boards만 등록된다.
      		user.getBoards().clear();
      		user.getBoards().addAll(newUser.getBoards());
      		// 새 boards에 user 정보 추가 
      	  for(Board board : user.getBoards()) {
      	  		board.setUser(user);
      	  }
      		return repository.save(user);
      })
      .orElseGet(() -> {
        newUser.setId(id);
        return repository.save(newUser);
      });
  }

  @DeleteMapping("/users/{id}")
  void deleteUser(@PathVariable Long id) {
    repository.deleteById(id);
  }
}
