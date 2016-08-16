package com.pintn.www.myschedule.entity;

import java.io.Serializable;

/**
 * Created by appledev070 on 8/16/16.
 */
public class City implements Serializable{

    private String id;
    private String nameCN;
    private String nameEn;
    private String provinceCN;
    private String districtCN;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getProvinceCN() {
        return provinceCN;
    }

    public void setProvinceCN(String provinceCN) {
        this.provinceCN = provinceCN;
    }

    public String getDistrictCN() {
        return districtCN;
    }

    public void setDistrictCN(String districtCN) {
        this.districtCN = districtCN;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", nameCN='" + nameCN + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", provinceCN='" + provinceCN + '\'' +
                ", districtCN='" + districtCN + '\'' +
                '}';
    }

}
