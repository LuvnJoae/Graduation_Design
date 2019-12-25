package com.lichang.utils.SettingUtils;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingUtil {
    /**
     * 用于 查询指定表 的全部内容
     */
    public static List<Map<String, Object>> getData(String table) {
        String sqlStr = SqlStrUtil.query_sql1(table);
        List<Map<String, Object>> maps = JdbcTemplateUtil.queryMult(sqlStr);
        return maps;
    }

    /**
     * 获取当前焊机的 名称与状态
     */
    public static String[] getNowMachine() {

        String machineName = "-1"; //一旦出错，则默认为-1
        String machineStatus = "-1"; //一旦出错，则默认为-1

        try {
            String ip = String.valueOf(InetAddress.getLocalHost()); //查询当前 主机名 + ip地址

            //查询是否存在该主机名+ip
            String sqlStr = "Select * from machine_now where ip = ?";
            ArrayList<Object> params = new ArrayList<>();
            params.add(ip);

            Map<String, Object> map = JdbcTemplateUtil.querySingle(sqlStr, params);

            //不存在则返回默认值-1，存在则返回相应值
            if (map == null) {
                return new String[]{machineName, machineStatus};
            } else {
                machineName = (String) map.get("machine_name");
                machineStatus = (String) map.get("machine_status");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] machineStrs = {machineName, machineStatus};

        return machineStrs;
    }

    /**
     * 更换 当前焊机
     */
    public static boolean changeNowMachine(String machine_name, String machine_status) {
        boolean updateResult = false;
        try {
            String ip = String.valueOf(InetAddress.getLocalHost()); //查询当前 ip地址

            //查询是否存在该主机名+ip
            String sqlStr = "select * from machine_now where ip = ?";
            ArrayList<Object> params = new ArrayList<>();
            params.add(ip);

            Map<String, Object> map = JdbcTemplateUtil.querySingle(sqlStr, params);

            //判断数据库是否已有此主机名+ip信息
            String sqlStr2;
            params = new ArrayList<>();
            //无，则新添加
            if (map == null) {
                sqlStr2 = "insert into machine_now (ip, machine_name, machine_status) values (?, ?, ?)";
                params.add(ip);
                params.add(machine_name);
                params.add(machine_status);
            } else {
                //有，则对原数据进行更改
                sqlStr2 = "update machine_now set machine_name = ?, machine_status = ? where ip = ?";
                params.add(machine_name);
                params.add(machine_status);
                params.add(ip);
            }

            updateResult = JdbcTemplateUtil.update(sqlStr2, params);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return updateResult;
    }

    /**
     * 修改 焊机信息
     */
    public static boolean updateMachine(String machine_name, String machine_status) {
        String sqlStr = "update machine_setting set machine_status = ? where machine_name = ?";
        ArrayList<Object> params = new ArrayList<>();
        params.add(machine_status);
        params.add(machine_name);

        boolean updateResult = JdbcTemplateUtil.update(sqlStr, params);

        return updateResult;
    }

    /**
     * 更新 当前焊机表的 状态信息
     */
    public static boolean updateMachineStatus(String machine_name, String machine_status) {
        try {
            String ip = String.valueOf(InetAddress.getLocalHost()); //查询当前 ip地址

            String sqlStr = "update machine_now set machine_status = ? where ip = ? and machine_name = ?";
            ArrayList<Object> params = new ArrayList<>();
            params.add(machine_status);
            params.add(ip);
            params.add(machine_name);

            boolean updateResult = JdbcTemplateUtil.update(sqlStr, params);

            return updateResult;

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }


    }

    /**
     * 添加焊机 插入新记录
     */
    public static boolean insertNewMachine(String machine_name, String machine_status) {
        String sqlStr = "insert into machine_setting (machine_name, machine_status) values(?, ?)";
        ArrayList<Object> params = new ArrayList<>();
        params.add(machine_name);
        params.add(machine_status);

        boolean result = JdbcTemplateUtil.update(sqlStr, params);
        return result;
    }

    /**
     *  用户信息管理 更新用户信息
     */
    public static boolean updateUser(String table, int id, String username, String password) {
        String sqlStr = "update " + table + " set username = ? , password = ? where id = ?";
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        params.add(id);

        boolean result = JdbcTemplateUtil.update(sqlStr, params);

        return result;
    }

    /**
     * 用户信息管理 添加新用户
     */
    public static boolean insertNewUser(String table, String username, String password) {
        String sqlStr = "insert into " + table + " (username, password) values(?, ?)";
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(password);

        boolean result = JdbcTemplateUtil.update(sqlStr, params);
        return result;
    }
}
