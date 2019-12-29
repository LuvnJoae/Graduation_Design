package com.lichang.utils;

import com.lichang.DBbeans.Admin;
import com.lichang.DBbeans.Employee;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 密码修改的工具类
 */
public class ChangePasswordUtil {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 判断密码 是否 正确
     * @param table
     * @param username
     * @param password
     * @return
     */
    public static boolean validate(String table, String username, String password) {
        String sqlStr = SqlStrUtil.query_sql1_1(table); // sql语句
        List<Object> params = SqlStrUtil.query_list1_1(username, password); // 参数列表

        Object obj = null;
        if (table.equals("admin")) {
            obj = new Admin();
        } else if (table.equals("emp")) {
            obj = new Employee();
        } else {
            log.error("表名出错");
        }

        List<?> bean = JdbcTemplateUtil.queryMultForBean(sqlStr, obj.getClass(), params);

        if (bean == null || bean.size() <= 0 || bean.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 数据库录入新密码
     * @param table
     * @param username
     * @param password
     * @return
     */
    public static void newPassword(String table, String username, String password) {
        String sqlStr = SqlStrUtil.update_sql1(table);
        List<Object> params = SqlStrUtil.update_list1(username, password);

        JdbcTemplateUtil.update(sqlStr, params);


    }
}
