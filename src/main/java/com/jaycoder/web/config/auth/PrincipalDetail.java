package com.jaycoder.web.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jaycoder.web.model.RoleType;
import com.jaycoder.web.model.User;

@SuppressWarnings("serial")
public class PrincipalDetail implements UserDetails {
		private User user;  // 컴포지션 (객체를 품고 있다.)

		public PrincipalDetail(User principal) {
				this.user = principal;
		}

		@Override
		public String getPassword() {			
				return user.getPassword();
		}

		@Override
		public String getUsername() {
				return user.getUsername();
		}
		
		public Long getId() {
				return user.getId();
		}
		
		public String getEmail() {
				return user.getEmail();
		}
		
		public RoleType getRole() {
				return user.getRole();
		}

		// 계정 만료 여부 -  true : 만료 않됨
		@Override
		public boolean isAccountNonExpired() {
				return true;
		}

		// 계정 잠김 여부 - true : 잠기지 않음
		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		// 비밀번호 만료 여부 - true : 만료되지 않음
		@Override
		public boolean isCredentialsNonExpired() {
				return true;
		}

		// 계정 활성화 여부 - true : 활성화됨
		@Override
		public boolean isEnabled() {
				return true;
		}
		
		// 계정 권한 여부 - 컬렉션 타입 리턴 
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
				Collection<GrantedAuthority> collectors = new ArrayList<GrantedAuthority>();	
				collectors.add(()->{ return "ROLE_"+user.getRole(); });				
				return collectors;
		}				
		
}
