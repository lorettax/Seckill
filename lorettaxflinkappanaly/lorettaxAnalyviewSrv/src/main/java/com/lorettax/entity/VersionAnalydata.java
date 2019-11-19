package com.lorettax.entity;

import java.util.List;
import java.util.Map;

public class VersionAnalydata {
        private List<String> xlist;
        private List<Map<String,Object>> datalist;

        public List<String> getXlist() {
            return xlist;
        }

        public void setXlist(List<String> xlist) {
            this.xlist = xlist;
        }

        public List<Map<String, Object>> getDatalist() {
            return datalist;
        }

    public void setDatalist(List<Map<String, Object>> datalist) {
        this.datalist = datalist;
    }
}