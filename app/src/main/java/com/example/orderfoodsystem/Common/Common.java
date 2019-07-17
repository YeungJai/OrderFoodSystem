package com.example.orderfoodsystem.Common;


import com.example.orderfoodsystem.Model.Users;

public class Common {
    public static Users currentUser;

    public static final String DELETE = "Delete";

    public static final String USER_KEY = "Delete";

    public static final String PWD_KEY = "Delete";

    public static String convertCodeToStatus(String status) {
        if (status.equals("0")){
            return "Doing Now";
        }
        else if(status.equals("1")){
            return "On my way";
        }
        else {
            return "Shipped";
        }
    }





}
