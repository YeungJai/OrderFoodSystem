package com.example.orderfoodsystem.Model;

public class Users {
    private String Name;
    private String Email;
    private String Password;
    private String Phone;
    private String IsStaff;

    public Users() {

    }

    public Users(String email, String password, String phone, String name){
        Name  = name;
        Email = email;
        Password = password;
        Phone = phone;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
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

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}