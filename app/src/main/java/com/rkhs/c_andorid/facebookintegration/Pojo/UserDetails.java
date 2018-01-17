package com.rkhs.c_andorid.facebookintegration.Pojo;

/**
 * Created by Admin on 17-01-2018.
 */

public class UserDetails {

    public String kId;
    public String kFname;
    public String kLname;
    public String kEmailId;
    public String kGender;

    public UserDetails() {

    }

    public UserDetails(String kId, String kFname, String kLname, String kEmailId, String kGender) {
        this.kId = kId;
        this.kFname = kFname;
        this.kLname = kLname;
        this.kEmailId = kEmailId;
        this.kGender = kGender;
    }

    public String getkId() {
        return kId;
    }

    public void setkId(String kId) {
        this.kId = kId;
    }

    public String getkFname() {
        return kFname;
    }

    public void setkFname(String kFname) {
        this.kFname = kFname;
    }

    public String getkLname() {
        return kLname;
    }

    public void setkLname(String kLname) {
        this.kLname = kLname;
    }

    public String getkEmailId() {
        return kEmailId;
    }

    public void setkEmailId(String kEmailId) {
        this.kEmailId = kEmailId;
    }

    public String getkGender() {
        return kGender;
    }

    public void setkGender(String kGender) {
        this.kGender = kGender;
    }
}
