package com.gt.common.view;

import java.util.Objects;

public class UserView {

    private String userID;
    private String password;

    public UserView() {}

    public UserView(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserView userView = (UserView) o;
        return Objects.equals(userID, userView.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }

}
