package org.mxhero.console.backend.infrastructure.pagination.common;

import java.util.List;

import org.mxhero.console.backend.vo.PageVO;

public class PageResult<T> {
	
    private List<T> pageData;
    private int totalRecordsNumber;
    private int pageNumber;
    private int pageAmount;

    public PageResult(List<T> data, int recordsNumber, int pageNumber, int pageAmount) {
            this.pageData = data;
            this.totalRecordsNumber = recordsNumber;
            this.pageAmount = pageAmount;
            this.pageNumber = pageNumber;
    }
    
    public PageResult() {
    }

    public List<T> getPageData() {
            return pageData;
    }

    public void setPageData(List<T> data) {
            this.pageData = data;
    }

    public int getTotalRecordsNumber() {
            return totalRecordsNumber;
    }

    public void setTotalRecordsNumber(int recordsNumber) {
            this.totalRecordsNumber = recordsNumber;
    }

    public int getPageAmount() {
            return pageAmount;
    }

    public void setPageAmount(int pageAmount) {
            this.pageAmount = pageAmount;
    }

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public PageVO createVO() {
		PageVO pageVO = new PageVO();
		pageVO.setActualPage(this.getPageNumber());
		pageVO.setElements(this.getPageData());
		pageVO.setTotalElements(this.getTotalRecordsNumber());
		pageVO.setTotalPages(this.getPageAmount());
		return pageVO;
	}

}
