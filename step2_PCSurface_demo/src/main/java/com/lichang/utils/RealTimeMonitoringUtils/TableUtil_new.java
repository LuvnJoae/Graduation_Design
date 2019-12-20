package com.lichang.utils.RealTimeMonitoringUtils;

import com.lichang.DBbeans.Machine_data_all;
import com.lichang.DBbeans.Machine_data_now;
import com.lichang.DBbeans.Machine_fault_data;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * 用于对JTable的数据处理、添加等
 */
public class TableUtil_new {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 故障表： 获取 表的 最新一条记录
     * @return
     */
    public static Map<String, Object> getLastRecord(String table, String production_name) {
        String sqlStr = SqlStrUtil.query_sql6(table);
        List<Object> params = SqlStrUtil.query_list6(production_name);

        Map<String, Object> map = JdbcTemplateUtil.querySingle(sqlStr, params);

        return map;
    }

    /**
     * 故障表：获取表中的全部数据
     *
     * @param table
     * @return
     */
    public static List<Map<String, Object>> getData(String table) {
        String sqlStr = SqlStrUtil.query_sql4(table);
        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sqlStr);
        return mapsList;
    }

    /**
     * 当前参数表： 获取表中 条件为production_name的全部数据
     */
    public static List<Map<String, Object>> getData(String table, String production_name) {
        String sqlStr = SqlStrUtil.query_sql7(table);
        List<Object> params = SqlStrUtil.query_list7(production_name);

        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sqlStr, params);
        return mapsList;
    }

}
