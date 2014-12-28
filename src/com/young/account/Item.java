package com.young.account;

import cn.bmob.v3.BmobObject;

public class Item extends BmobObject {
    private int id;
    private int sync;
    private float price;
    private String date;
    private String time;
    private String type;
    private String method;
    private String remark;

    // id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // sync
    public int getSync() {
        return sync;
    }
    public void setSync(int sync) {
        this.sync = sync;
    }

    // price
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    // date
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    // time
    public String gettime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    // type
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    // method
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    // remark
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
