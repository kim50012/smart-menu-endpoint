package com.basoft.service.entity.user;

import com.basoft.service.dto.PaginationDTO;

public class UserListDTO extends PaginationDTO{
	private static final long serialVersionUID = 1L;
	
	private String keyword;
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getKeyword() {
		return keyword;
	}
}