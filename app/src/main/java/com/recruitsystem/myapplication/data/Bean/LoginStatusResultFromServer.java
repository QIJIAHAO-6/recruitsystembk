package com.recruitsystem.myapplication.data.Bean;

public class LoginStatusResultFromServer {
    private static int LoginReturnResult;

    public static int getLoginReturnResult() {
        return LoginReturnResult;
    }

    public static void setLoginReturnResult(int loginReturnResult) {
        LoginReturnResult = loginReturnResult;
    }
}
