package com.lichang.utils;

import com.lichang.DBbeans.Admin;

public class Login_LogoutUtils {
    public Login_LogoutUtils() {

    }

    /**
     * 登录
     * @param obj Bean类对象
     * @return
     */
    public String login(Object obj) {
        /*
            预留信息
         */
        String resultStr = "登录失败";
        String username = null;
        String password = null;
        String table = null;

        String sqlStr = null;

        if (Admin.class == obj.getClass()) {
            Admin admin = (Admin) obj;
            username = admin.getUsername();
            password = admin.getPassword();
            table = "admin";
            sqlStr = SqlStrUtil.generateSql("admin", "admin", "admin");


        }
        return resultStr;
    }
}
