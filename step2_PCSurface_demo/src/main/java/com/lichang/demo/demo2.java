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
        String sqlStr = "select count(id) from machine_data_brief WHERE production_name = ?";
        Object[] objects = {"production_default"};
        ArrayList<Object> list = new ArrayList<>();
        list.add("prodution_default");
        JdbcTemplate template = new JdbcTemplate(JdbcUtil.getDataSource()); // 获取JdbcTemplate对象
        Long count = template.queryForObject(sqlStr, Long.class,objects);
        System.out.println(count);

    }
}

