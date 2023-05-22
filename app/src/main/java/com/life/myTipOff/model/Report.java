package com.life.myTipOff.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "report")
public class Report {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String name;
    private String nameType; // 실명 혹은 가명
    private String phone;
    private String content;
    private String date;

    public Report(String title, String name, String nameType, String phone, String content, String date) {
        this.title = title;
        this.name = name;
        this.nameType = nameType;
        this.phone = phone;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
