package com.lichang.utils.dao;

import com.lichang.utils.LoggerUtil;
import javafx.scene.chart.StackedBarChart;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类用于 合成 SQL语句，简化
 *      1. 方法可自行定义添加
 *      2. 通过List转Array，进驻参数，防止SQL注入
 */
public class SqlStrUtil {

    private static Logger log = LoggerUtil.getLogger();

    /**
     * 合成Sql语句 1
     *      查询表  table
     *      查询参数 username，password
     * @param table
     * @return
     */
    public static String generateSql1(String table) {
        log.debug("查询Sql合成 ：table");
        String sqlStr = "select * from "
                + table
                + " where username= ? and password= ?";

        return sqlStr; // 注意，表名不能通过占位符，需要拼接字符串
    }

    public static List<Object> generateList1(String username, String password) {
        log.debug("查询Sql的params合成 ：username，password");
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(username);
        paramsList.add(password);
        return paramsList; // 占位符参数列表
    }
}