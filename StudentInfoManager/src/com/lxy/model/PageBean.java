package com.lxy.model;

public class PageBean {
    private int rows;//每页的行数
    private int page;//第几页
    private int start;//在数据库中查询是所对应的start

    public PageBean(int page, int rows) {
        this.rows = rows;
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getStart() {
        return (page-1)*rows;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
