package martexcorp.com.swiftblood.EntryPoint;

import martexcorp.com.swiftblood.DonorList.DonorList;

public class UserDetails  {

    private String username;
    private String town;
    private String tel;
    private String bloodGrp;
    private String sex;

    public UserDetails(){ }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodgrp) {
        this.bloodGrp = bloodgrp;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public UserDetails(String username, String town, String tel, String bloodGrp, String sex){

        this.username = username;
        this.town = town;
        this.tel = tel;
        this.bloodGrp = bloodGrp;
        this.sex=sex;

    }
}
