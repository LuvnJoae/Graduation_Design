package com.lichang.demo;

import com.lichang.DBbeans.Machine_data_now;
import com.lichang.utils.dao.JdbcTemplateUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class demo2 {

    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost.getHostAddress());
    }
}

