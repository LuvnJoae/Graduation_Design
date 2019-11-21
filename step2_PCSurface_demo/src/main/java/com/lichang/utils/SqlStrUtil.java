package com.lichang.utils;

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
     * 合成Sql语句 1 ： 查询
     *      表  table
     *      查询参数 username，password
     * @param table
     * @return
     */
    public static String generateSql1(String table) {
        log.debug("查询Sql合成1：" + table);
        String sqlStr = "select * from "
                + table
                + " where username= ? and password= ?";

        return sqlStr; // 注意，表名不能通过占位符，需要拼接字符串
    }

    public static List<Object> generateList1(String username, String password) {
        log.debug("查询Sql的params合成 ：" + username + "   " + password);
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(username);
        paramsList.add(password);
        return paramsList; // 占位符参数列表
    }

    /**
     * 合成Sql语句 2 ： 查询
     *      表  table
     *      查询参数 num
     * @param table
     * @return
     */
    public static String generateSql2(String table) {
        log.debug("查询Sql合成2：" + table);
        String sqlStr = "select * from "
                + table
                + " where num = ?";
        return sqlStr;
    }

    public static List<Object> generateList2(int num) {
        log.debug("查询Sql的params合成 ：" + num);
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(num);
        return paramsList; // 占位符参数列表
    }

    /**
     * 合成Sql语句 3 ： 修改
     *      修改表 table
     *      查询参数 username
     *      修改参数 password
     * @param table
     * @return
     */
    public static String generateSql3(String table) {
        log.debug("修改Sql合成3：" + table);
        String sqlStr = "update "
                + table
                + " set password = ?"
                + " where username = ?";
        return sqlStr;
    }

    public static List<Object> generateList3(String username, String password) {
        log.debug("修改Sql的params合成 ：" + username + "   " + password);
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(password);
        paramsList.add(username);
        return paramsList; // 占位符参数列表
    }
}
