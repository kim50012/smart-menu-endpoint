package com.basoft.eorder.interfaces.query;

import java.util.List;

public class PaginationDTO<T> {
    private int total;
    private List<T> dataList;

    public PaginationDTO() {
    }

    public PaginationDTO(int total, List<T> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
