package com.recruitsystem.myapplication.data.model;

public class UserLoginStatus {
        private static boolean loginStatus = false;

    public static boolean isLoginStatus() {
        return loginStatus;
    }

    public static void setLoginStatus(boolean loginStatus) {
        UserLoginStatus.loginStatus = loginStatus;
    }

}
