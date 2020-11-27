package com.example.appchat.Models;

import java.io.Serializable;

public class NhanTin implements Serializable {
    private String tinGui;
    private String tinNhan;

    public String getTinGui() {
        return tinGui;
    }

    public void setTinGui(String tinGui) {
        this.tinGui = tinGui;
    }

    public String getTinNhan() {
        return tinNhan;
    }

    public void setTinNhan(String tinNhan) {
        this.tinNhan = tinNhan;
    }

    public NhanTin() {
    }

    public NhanTin(String tinGui, String tinNhan) {
        this.tinGui = tinGui;
        this.tinNhan = tinNhan;
    }
}