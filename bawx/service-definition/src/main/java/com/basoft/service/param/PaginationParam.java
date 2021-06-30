package com.basoft.service.param;

import javax.validation.constraints.DecimalMin;

/**
 * 封装分页相关的参数.
 * @author DongXifu
 */
public class PaginationParam {
    private final int MAX_ROWS_PER_PAGE = 200; // 每页最大数量
    @DecimalMin(value = "1", message = "必须大于等于1")
    private int page = 1; // 当前页码

    @DecimalMin(value = "1", message = "必须大于等于1")
    private int rows = 20; // 每页大小

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return Math.min(rows, MAX_ROWS_PER_PAGE);
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
