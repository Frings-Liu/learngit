package com.lxy.model;

import java.util.Date;

public class Student  {
    private int stuId;
    private String stuNo;
    private String stuName;
    private String sex;
    private Date birthday;
    private int gradeId=-1;

    public Student() {
        super();
    }
    //构造方法不要stuNo,因为我们这个构造方法是给添加的servlet用的,添加到数据库的时候,不需要stuId,这个是在数据库里面自增长的
    public Student(int stuId, String stuName, String sex, Date birthday, int gradeId, String stuDesc, String email) {
        this.stuId = stuId;
        this.stuName = stuName;
        this.sex = sex;
        this.birthday = birthday;
        this.gradeId = gradeId;
        this.stuDesc = stuDesc;
        this.email = email;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    private String stuDesc;
    private String email;

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }




    public String getStuDesc() {
        return stuDesc;
    }

    public void setStuDesc(String stuDesc) {
        this.stuDesc = stuDesc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
