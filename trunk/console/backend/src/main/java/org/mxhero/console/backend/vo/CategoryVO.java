package org.mxhero.console.backend.vo;

import java.util.Collection;

public class CategoryVO {

	private Integer id;
	
	private String label;
	
	private String iconsrc;
	
	private Collection<FeatureVO> childs;
	
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

	public Collection<FeatureVO> getChilds() {
		return childs;
	}

	public void setChilds(Collection<FeatureVO> childs) {
		this.childs = childs;
	}

}
