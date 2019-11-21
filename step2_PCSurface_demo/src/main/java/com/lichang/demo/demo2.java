package com.lichang.demo;

public class demo2 {

    public static void main(String[] args) {
        String newPassword = "a";
        if ((newPassword.charAt(0) < 'A' || newPassword.charAt(0) > 'z')
                || (newPassword.charAt(0) > 'Z' && newPassword.charAt(0) < 'a')) {
            System.out.println("密码格式错误！");

        }else {
            System.out.println("密码格式正确！");

        }
    }
}

