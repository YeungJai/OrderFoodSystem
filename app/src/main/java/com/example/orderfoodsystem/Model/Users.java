package com.example.orderfoodsystem.Model;

public class Users {
    private String Email;
    private String Password;
    private String Phone;

    public Users() {

    }

    public Users(String email, String password, String phone){
        Email = email;
        Password = password;
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}