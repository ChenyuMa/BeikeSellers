package com.example.andy.beikesellers.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;



public class SellerInfo extends BmobObject {
    private String idType;
    private String name;
    private String idNum;
    private String image1;

    public String getIdType() {
        return idType;
    }

    public String getName() {
        return name;
    }

    public String getIdNum() {
        return idNum;
    }

    public String getImage1() {
        return image1;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }
}