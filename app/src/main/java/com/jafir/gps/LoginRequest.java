package com.jafir.gps;

/**
 * created by jafir on 2020-06-24
 */
public class LoginRequest {

    /**
     * username : admin
     * password : 123456
     */

    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
