package com.lichang.demo;

import com.lichang.DBbeans.Admin;
import com.lichang.ui.Login;
import com.lichang.utils.JdbcTemplateUtil;
import com.lichang.utils.SqlStrUtil;

import java.util.List;
import java.util.Map;

public class Demo1 {


    public static void main(String[] args) {
        String sqlStr = SqlStrUtil.generateSql("admin", "admin", "admin");
        List<?> objects = JdbcTemplateUtil.queryMultForBean(sqlStr, new Admin());
        System.out.println(objects);
    }


}
