package com.example.demotree;

import lombok.Data;

/**
 * 前端表格的展示数据
 */
@Data
public class ProveData {
    int index;
    String hash;
    String Chash;

    ProveData(){}

    public ProveData(int index, String hash, String chash) {
        this.index = index;
        this.hash = hash;
        Chash = chash;
    }
}
