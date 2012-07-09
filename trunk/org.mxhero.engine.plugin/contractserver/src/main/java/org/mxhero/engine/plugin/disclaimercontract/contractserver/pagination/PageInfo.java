package org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination;

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
