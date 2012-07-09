package org.mxhero.engine.plugin.disclaimercontract.contractserver.vo;

import java.util.List;

public class PageVO <T>{

	private List<T> elements;
	
	private Integer totalElements;

	private Integer totalPages;

	private Integer actualPage;
	
	
	public List<T> getElements() {
		return elements;
	}

	public void setElements(List<T> elements) {
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
