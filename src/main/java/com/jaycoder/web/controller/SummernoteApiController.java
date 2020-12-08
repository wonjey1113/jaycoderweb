package com.jaycoder.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaycoder.web.model.Result;
import com.jaycoder.web.model.Summernote;
import com.jaycoder.web.repository.SummernoteRepository;
import com.jaycoder.web.util.FileUtils;

@RestController  
@RequestMapping("/api/board/summernote")  // /api/board/attach/47/delete
public class SummernoteApiController {
	
	@Autowired
	private SummernoteRepository summernoteRepository;

	@DeleteMapping("/{id}/delete")
	public Result delete(@PathVariable Long id) {
			Summernote attach = summernoteRepository.findById(id).get();
			// 파일 삭제
			FileUtils.boardFileDelete(attach.getSave_name(), "summernote_image");
			// 디비 삭제
			summernoteRepository.deleteById(id);				
			return Result.ok(0);				
	}
		
}
