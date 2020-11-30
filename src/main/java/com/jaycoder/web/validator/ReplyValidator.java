package com.jaycoder.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.Reply;

@Component
public class ReplyValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
			return Board.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
			Reply r = (Reply) obj;
			if(StringUtils.isEmpty(r.getContent())) {
					errors.rejectValue("content", "key", "내용을 입력해주세요.");
			}
	}

}
