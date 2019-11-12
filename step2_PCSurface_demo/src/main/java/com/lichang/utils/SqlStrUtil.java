package com.lichang.utils;

import org.apache.log4j.Logger;

/**
 * 该类用于 合成 SQL语句，简化
 *      方法可自行定义添加
 */
public class SqlStrUtil {

    private static Logger log = LoggerUtil.getLogger();
    /**
     * 查询
     * @param username
     * @param password
     * @param table
     * @return
     */
    public static String generateSql(String username, String password, String table) {
        log.debug("查询Sql合成 ： username、password、table");
        String sqlStr = "select * from "
                + table
                + " where username = "
                + "'" + username + "'"
                + " and password = "
                + "'" + password + "'";
        return sqlStr;
    }
}
