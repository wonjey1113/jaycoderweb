package com.jaycoder.web.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass  // 부모 클래스로 사용할때 사용
@EntityListeners(value = AuditingEntityListener.class) // 변경 사항이 있으면 데이터를 타입에 맞게 자동으로 등록해줌.
public class AbstractEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@CreatedDate
		@Column(updatable=false)
		public LocalDateTime  createdate;  // createdate
		
		@LastModifiedDate
		public LocalDateTime  modifieddate;

		public Long getId() {
				return id;
		}		
	
		public void setId(Long id) {
				this.id = id;
		}

		public String getFormattedCreateDate() {
				return getFormattedDate(createdate, "yyyy.MM.dd HH:mm:ss");
		}
		
		public String getFormattedModifiedDate() {
				return getFormattedDate(modifieddate, "yyyy.MM.dd HH:mm:ss");
		}		
		
		private String getFormattedDate(LocalDateTime dateTime, String format) {
				if (dateTime == null) {
						return "";
				}
				return dateTime.format(DateTimeFormatter.ofPattern(format));
		}		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AbstractEntity other = (AbstractEntity) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "AbstractEntity [id=" + id + ", createdate=" + createdate + ", modifieddate=" + modifieddate + "]";
		}
		
		
}
