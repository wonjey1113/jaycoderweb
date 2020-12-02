package com.jaycoder.web.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaycoder.web.model.Attach;
import com.jaycoder.web.repository.AttachRepository;

@Service
public class AttachService {
		
		@Autowired
		private AttachRepository attachRepository;

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
		
		
		
}
