package com.example.myzipzap;

public class ReadWriteUserDetails {
    public String userCredit;

    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String userCredit){
        this.userCredit = userCredit;
    }

    public String getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(String userCredit) {
        this.userCredit = userCredit;
    }
}
