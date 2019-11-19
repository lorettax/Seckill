package com.lorettax.entity;

import java.util.List;
import java.util.Map;

public class UsageAnalydata {
        private List<Long> xlist;
        private List<Map<String,Object>> datalist;

    public List<Long> getXlist() {
        return xlist;
    }

    public void setXlist(List<Long> xlist) {
        this.xlist = xlist;
    }

    public List<Map<String, Object>> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<Map<String, Object>> datalist) {
        this.datalist = datalist;
    }
}