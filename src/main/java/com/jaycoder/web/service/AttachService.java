package com.jaycoder.web.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaycoder.web.model.Attach;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.Summernote;
import com.jaycoder.web.repository.AttachRepository;
import com.jaycoder.web.repository.BoardRepository;
import com.jaycoder.web.repository.SummernoteRepository;
import com.jaycoder.web.util.FileUtils;

@Service
public class AttachService {
		
		@Autowired
		private AttachRepository attachRepository;
		
		@Autowired
		private BoardRepository boardRepository;
		
		@Autowired
    private SummernoteRepository summernoteRepository;

		public Integer insertAttach(List<Attach> fileList) {
		  int rows = 0;
			for (Attach attachDTO : fileList) {
				 Attach attachDto = 	attachRepository.save(attachDTO);
				 if(attachDto != null) {
					 	rows++;
				 }					 
			}
			return rows;
		}

		public String getModifydate(Long id) {
				Attach attachDto = attachRepository.getOne(id);
				
				return attachDto.getFormattedYMD();
		}

		public String getFilename(Long id) {
				Attach attachDto = attachRepository.getOne(id);
				
				return attachDto.getSave_name();
		}

		public Attach fileDetailService(Long id) {

			return attachRepository.findById(id).get();
		}

		public void deleteFiles(Long boardid) {		
			  Board board = boardRepository.findById(boardid).get();
				List<Attach> attachs = attachRepository.findByBoard2(board);
				for (Attach attach : attachs) {
						// 파일 삭제
						FileUtils.boardFileDelete(attach.getSave_name(), attach.getFormattedYMD());				 
				}
				// 파일 디비 삭제
				attachRepository.deleteFiles(boardid);
			
		}

		public void insertImage(Summernote attach) {
			 	
			summernoteRepository.save(attach);
		}

		public void updateSummernoteid(Long summernote_id) {
			summernoteRepository.updateSummernoteIdx(summernote_id);
			
		}

		public List<Summernote> summernoteFindByid(Long id) {

			return summernoteRepository.findByBoard_idx(id);
		}
		
		
		
}
