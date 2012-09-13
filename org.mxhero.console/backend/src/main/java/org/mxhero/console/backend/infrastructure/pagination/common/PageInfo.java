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

package org.mxhero.console.backend.infrastructure.pagination.common;

import java.util.ArrayList;
import java.util.List;

abstract public class PageInfo < T_Criterial, T_OrderBy>{
    protected T_Criterial expression;
    protected List<T_OrderBy> orderByList = new ArrayList<T_OrderBy>();
    protected int pageSize;
    protected int pageNo;
    protected Boolean end;

    public PageInfo(T_Criterial expression,
                    List<T_OrderBy> orderByList, int pageNo, int pageSize, Boolean end) {
            this.expression = expression;
            this.orderByList = orderByList;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.end = end;
    }

    public PageInfo(T_Criterial expression,
                    List<T_OrderBy> orderBy, int pageNo, int pageSize) {

            this.expression = expression;
            this.orderByList = orderBy;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.end = null;
    }

    public PageInfo() {
    }

    public T_Criterial getExpression() {
            return expression;
    }

    public void setExpression(T_Criterial expression) {
            this.expression = expression;
    }

    public int getPageSize() {
            return pageSize;
    }

    public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
    }

    public int getPageNo() {
            return pageNo;
    }

    public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
    }

    public Boolean getEnd() {
            return end;
    }

    public void setEnd(Boolean end) {
            this.end = end;
    }

    public List<T_OrderBy> getOrderByList() {
            return orderByList;
    }

    public void setOrderByList(List<T_OrderBy> orderBy) {
            this.orderByList = orderBy;
    }

    abstract public void addOrderByAsc(String orderBy);

    abstract public void addOrderByDesc(String orderBy);
}
