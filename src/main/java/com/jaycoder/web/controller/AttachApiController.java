package com.jaycoder.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jaycoder.web.model.Attach;
import com.jaycoder.web.model.Result;
import com.jaycoder.web.repository.AttachRepository;
import com.jaycoder.web.util.FileUtils;

@RestController  
@RequestMapping("/api/board/attach")  // /api/board/attach/47/delete
public class AttachApiController {
	
	@Autowired
	private AttachRepository attachRepository;

	@DeleteMapping("/{id}/delete")
	public Result delete(@PathVariable Long id) {
			Attach attach = attachRepository.findById(id).get();
			// 파일 삭제
			FileUtils.boardFileDelete(attach.getSave_name(), attach.getFormattedYMD());
			// 디비 삭제
			attachRepository.deleteById(id);				
			return Result.ok(0);				
	}
		
}
