package com.example.detouradmin.Models;

public class UserModel {

    private String Email;
    private String Phone;
    private String Username;

    private UserModel() {}

    private UserModel(String email, String phone, String username){
        this.Email = email;
        this.Phone = phone;
        this.Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
