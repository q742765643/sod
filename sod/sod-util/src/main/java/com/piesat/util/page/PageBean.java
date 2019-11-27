package com.piesat.util.page;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-11-18 22:09
 **/

public class PageBean {
    /**
     * 当前页
     */
    private int currentPage = 1;

    /**
     * 分页大小
     */
    private int pageSize = 10;

    private List<?> pageData;

    private long totalCount;

    private Integer totalPage;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<?> getPageData() {
        return pageData;
    }

    public void setPageData(List<?> pageData) {
        this.pageData = pageData;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}

