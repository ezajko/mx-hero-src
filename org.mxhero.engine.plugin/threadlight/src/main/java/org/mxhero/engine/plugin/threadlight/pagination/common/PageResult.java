/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.plugin.threadlight.pagination.common;

import java.util.List;

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

}
