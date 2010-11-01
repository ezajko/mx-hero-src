package org.mxhero.console.backend.vo;

import java.util.Collection;

public class PageVO<T> {

	private Collection<T> elements;
	
	private Integer number;
	
	private Integer numberOfElements;
	
	private Integer size;
	
	private Long totalElements;
	
	private Integer totalPages;
	
	private Boolean hasNextPage;
	
	private Boolean hasPreviousPage;

	public Collection<T> getElements() {
		return elements;
	}

	public void setElements(Collection<T> elements) {
		this.elements = elements;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(Integer numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalEmelents) {
		this.totalElements = totalEmelents;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Boolean getHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(Boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public Boolean getHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(Boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

}
