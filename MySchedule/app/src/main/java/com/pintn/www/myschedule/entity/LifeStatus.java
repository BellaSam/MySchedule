package com.pintn.www.myschedule.entity;

import java.io.Serializable;

/**
 * Created by appledev070 on 8/16/16.
 */
public class LifeStatus implements Serializable{

    private String index;
    private String name;
    private String code;
    private String detail;
    private String otherName;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    @Override
    public String toString() {
        return "LifeStatus{" +
                "index='" + index + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", detail='" + detail + '\'' +
                ", otherName='" + otherName + '\'' +
                '}';
    }
}
