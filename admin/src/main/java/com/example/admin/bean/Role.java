package com.example.admin.bean;

import java.io.Serializable;

public class Role implements Serializable {
    private Integer id;
    private String name;
    private String namezh;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getnamezh() {
        return namezh;
    }

    public void setnamezh(String namezh) {
        this.namezh = namezh;
    }
}
