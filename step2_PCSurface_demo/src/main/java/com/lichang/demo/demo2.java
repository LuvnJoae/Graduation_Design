package com.lichang.demo;

import com.lichang.DBbeans.Machine_data_now;
import com.lichang.utils.RealTimeMonitoringUtils.LabelUpdateTextUtil;
import com.lichang.utils.RealTimeMonitoringUtils.LabelUpdateTextUtil_new;
import com.lichang.utils.dao.JdbcTemplateUtil;
import com.lichang.utils.dao.JdbcUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class demo2 {

    public static void main(String[] args) throws UnknownHostException {
        String limitRule = "s-2, 0.9-1.1;3-e,1.0-1.0;&0&0";
//        for (String s : limitRule.split("&")) {
//            System.out.println(s);
//        }
        String currentLimitRule = limitRule.split("&")[0]; //电压 value_limit字符串
        String voltageLimitRule = limitRule.split("&")[1]; //电流 value_limit字符串
        String speedLimitRule = limitRule.split("&")[2]; //电流 value_limit字符串
    }
}

