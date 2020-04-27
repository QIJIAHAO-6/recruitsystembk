package com.recruitsystem.myapplication.data.Bean;

public class UserBean {
    private Integer userId;
    private String userName;
    private String userNickName;
    private Integer userAge;
    private String userBornDate;
    private String userSex;
    private String userAddress;
    private String userPhone;
    private String userDescription;
    private String userloginStatus;
    private String userPassword;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public String getUserBornDate() {
        return userBornDate;
    }

    public void setUserBornDate(String userBornDate) {
        this.userBornDate = userBornDate;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserloginStatus() {
        return userloginStatus;
    }

    public void setUserloginStatus(String userloginStatus) {
        this.userloginStatus = userloginStatus;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", userAge=" + userAge +
                ", userBornDate='" + userBornDate + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userDescription='" + userDescription + '\'' +
                ", userloginStatus='" + userloginStatus + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
