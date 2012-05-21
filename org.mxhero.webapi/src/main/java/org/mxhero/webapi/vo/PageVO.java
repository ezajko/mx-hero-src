package org.mxhero.webapi.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "page")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value={UserVO.class})
public class PageVO <T>{

	@XmlAnyElement(lax=true)
	@XmlElementWrapper(name="elements")
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
