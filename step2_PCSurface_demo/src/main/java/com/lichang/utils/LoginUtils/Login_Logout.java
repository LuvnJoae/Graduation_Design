package com.lichang.utils.LoginUtils;

import com.lichang.DBbeans.Admin;
import com.lichang.DBbeans.Employee;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class Login_Logout {
    public Login_Logout() {

    }
    private static Logger log = LoggerUtil.getLogger();
    /**
     * 登录
     * @param obj Bean类对象
     * @return
     */
    public static String login(Object obj) {
        /*
            预留信息
         */
        String resultStr = "登录失败";

        String username = null;
        String password = null;
        String table = null;

        String sqlStr = null;
        /*
            身份验证
         */
        if (Admin.class == obj.getClass()) {
            Admin admin = (Admin) obj;
            username = admin.getUsername();
            password = admin.getPassword();
            table = "admin";

            sqlStr = SqlStrUtil.query_sql1_1(table); // sql语句
            List<Object> params = SqlStrUtil.query_list1_1(username, password); // 参数列表
            Map<String, Object> DB_userInfoMap = JdbcTemplateUtil.querySingle(sqlStr, params); // 以map形式返回结果

            if (DB_userInfoMap != null) {
                resultStr = "登录成功！";
            }else {
                resultStr = "用户名或密码错误，请重新输入！";
            }
        } else if (Employee.class == obj.getClass()) {
            Employee emp = (Employee) obj;
            username = emp.getUsername();
            password = emp.getPassword();
            table = "employee";

            sqlStr = SqlStrUtil.query_sql1_1(table);
            List<Object> params = SqlStrUtil.query_list1_1(username, password);
            Map<String, Object> DB_userInfoMap = JdbcTemplateUtil.querySingle(sqlStr, params);

            if (DB_userInfoMap != null) {
                resultStr = "登录成功！";
            }else {
                resultStr = "用户名或密码错误，请重新输入！";
            }
        }
        return resultStr;
    }
}
