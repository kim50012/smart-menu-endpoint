package com.basoft.service.dto;

import java.io.Serializable;

public class PaginationDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int pageNum;
	private int pageSize;

	public PaginationDTO() {
	}

	public PaginationDTO(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}