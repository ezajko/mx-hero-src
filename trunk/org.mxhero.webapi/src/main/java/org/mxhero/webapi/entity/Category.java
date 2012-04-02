package org.mxhero.webapi.entity;

import java.util.Collection;

public class Category {

	private Integer id;
	
	private String label;
	
	private String iconsrc;
	
	private Collection<Feature> childs;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIconsrc() {
		return iconsrc;
	}

	public void setIconsrc(String iconsrc) {
		this.iconsrc = iconsrc;
	}

	public Collection<Feature> getChilds() {
		return childs;
	}

	public void setChilds(Collection<Feature> childs) {
		this.childs = childs;
	}

}
