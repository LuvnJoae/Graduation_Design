package com.lichang.demo;

import com.lichang.DBbeans.Machine_data_now;
import com.lichang.utils.dao.JdbcTemplateUtil;
import com.lichang.utils.dao.JdbcUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class demo2 {

    public static void main(String[] args) throws UnknownHostException {
        String sqlStr = "insert into expert_base_metal (id) values(?)";
        ArrayList<Object> list = new ArrayList<>();
        list.add("6");
        boolean update = JdbcTemplateUtil.update(sqlStr, list);
        System.out.println(update);
    }
}

