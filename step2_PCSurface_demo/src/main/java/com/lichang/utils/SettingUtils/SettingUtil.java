package com.lichang.utils.SettingUtils;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;

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
            String sqlStr = "Select * from machine_setting where ip = ?";
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
}
