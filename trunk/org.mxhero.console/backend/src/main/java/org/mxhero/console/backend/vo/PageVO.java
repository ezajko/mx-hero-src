package org.mxhero.console.backend.vo;

import java.util.List;

public class PageVO {

	private List elements;
	
	private Integer totalElements;

	private Integer totalPages;

	private Integer actualPage;

	public List getElements() {
		return elements;
	}

	public void setElements(List elements) {
		this.elements = elements;
	}

	public Integer getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getActualPage() {
		return actualPage;
	}

	public void setActualPage(Integer actualPage) {
		this.actualPage = actualPage;
	}
	
}
