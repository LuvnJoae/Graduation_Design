package com.lichang.utils.RealTimeMonitoringUtil;

import com.lichang.DBbeans.Admin;
import com.lichang.DBbeans.Employee;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 密码修改的工具类
 */
public class ChangePassword {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 判断密码 是否 正确
     * @param table
     * @param username
     * @param password
     * @return
     */
    public static boolean validate(String table, String username, String password) {
        log.debug("判断密码 是否 正确");

        String sqlStr = SqlStrUtil.generateSql1(table); // sql语句
        List<Object> params = SqlStrUtil.generateList1(username, password); // 参数列表

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
        log.debug("数据库录入新密码");

        String sqlStr = SqlStrUtil.generateSql3(table);
        List<Object> params = SqlStrUtil.generateList3(username, password);

        JdbcTemplateUtil.update(sqlStr, params);


    }
}
