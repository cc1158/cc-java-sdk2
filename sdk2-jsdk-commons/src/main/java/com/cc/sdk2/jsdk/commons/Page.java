package com.cc.sdk2.jsdk.commons;

/**
 * 分页类
 * @author sen.hu
 * @date 2019/8/12 17:32
 **/
public class Page {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_START = 0;

    /**
     * 页码
     */
    private Integer pageNumber = DEFAULT_PAGE_NUMBER;
    /**
     *一页数量
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 数据起始位置
     */
    private Integer start = DEFAULT_START;
    /**
     * 数量
     */
    private Integer total = 0;
    /**
     * 总页数
     */
    private Integer pages = 0;

    public Page() {
        this.start = (this.pageNumber - 1) * pageSize;
    }

    public Page(Integer pageNumber, Integer pageSize) {
        if (pageSize != null && pageSize > 0) {
            this.setPageSize(pageSize);
        }
        if (pageNumber != null && pageNumber > 0) {
            this.setPageNumber(pageNumber);
        }
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        this.pageNumber = pageNumber >= DEFAULT_PAGE_NUMBER ? pageNumber : DEFAULT_PAGE_NUMBER;
        this.start = (this.pageNumber - 1) * pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize >= DEFAULT_PAGE_SIZE ? pageSize : DEFAULT_PAGE_SIZE;
        this.setPageNumber(this.pageNumber);
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start >= DEFAULT_START ? start : DEFAULT_START;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        if (total > 0) {
            this.total = total;
        }
        this.pages = total % this.pageSize == 0 ? this.total / this.pageSize : this.total / this.pageSize + 1;

    }

    public Integer getPages() {
        return pages;
    }

}
