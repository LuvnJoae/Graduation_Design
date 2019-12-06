package com.lichang.demo;

import com.lichang.DBbeans.Machine_data_now;
import com.lichang.utils.dao.JdbcTemplateUtil;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class demo2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        while (true) {
            String newPassword = sc.next();
            if (Pattern.matches(".*(as|fg).*", newPassword)) {
                System.out.println(true);
            } else {
                System.out.println(false);
            }
        }


    }
}

