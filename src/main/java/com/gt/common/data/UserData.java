package com.gt.common.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
public class UserData {

    @Id
    @NotNull
    @Column(name = "USER_ID")
    private String userID;

    @NotNull
    @Column(name = "PASSWORD")
    private String password;

    public UserData() {}

    public UserData(@NotNull String userID, @NotNull String password) {
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

}
