package com.lorettax.entity;

/**
 * Created by li on 2018/12/11.
 */
public class AppIninfo {
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "AppIninfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
