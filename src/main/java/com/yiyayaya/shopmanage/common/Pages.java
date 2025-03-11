package com.yiyayaya.shopmanage.common;

import lombok.Data;

import java.util.List;

@Data
public class Pages<T> {
    private List<T> records; // 当前页的数据
    private long total; // 总记录数
    private long current; // 当前页码
    private long pageSize; // 每页大小

    public Pages(List<T> records, long total, long current, long pageSize) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.pageSize = pageSize;
    }
} 