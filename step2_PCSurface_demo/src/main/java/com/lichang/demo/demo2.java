package com.lichang.demo;

import com.lichang.DBbeans.Machine_data_now;
import com.lichang.utils.dao.JdbcTemplateUtil;

import java.util.List;

public class demo2 {

    public static void main(String[] args) {
        String sqlStr = "select * from machine_data_now where id = 100";
        List<Machine_data_now> machine_data_now_BeansList = (List<Machine_data_now>)
                JdbcTemplateUtil.queryMultForBean(sqlStr, Machine_data_now.class);
        int num = machine_data_now_BeansList.get(0).getNum();
        System.out.println(num);

    }
}

