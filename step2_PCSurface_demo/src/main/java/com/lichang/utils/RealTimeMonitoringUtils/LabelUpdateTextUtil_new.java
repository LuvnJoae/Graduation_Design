package com.lichang.utils.RealTimeMonitoringUtils;

import com.lichang.DBbeans.Machine_data_now;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于对Label进行数据更新
 */
public class LabelUpdateTextUtil_new {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 查询 已完成工件 总记录数
     */
    public static Long getCompletedCount(String production_name) {
        String sql = SqlStrUtil.query_sql2_1("machine_data_brief");
        Object[] params = {production_name};
        Long count = JdbcTemplateUtil.queryCount(sql, params);
        return count;
    }

    /**
     * 查询 故障工件 总记录数
     */
    public static Long getFaultCount(String production_name) {
        String sql = SqlStrUtil.query_sql2_1("machine_fault_data");
        Object[] params = {production_name};
        Long count = JdbcTemplateUtil.queryCount(sql, params);
        return count;
    }

    /**
     * 查询 当前工件 编号
     */
    public static int getProdutionNum(String production_name) {
        String sql = SqlStrUtil.query_sql1_3("machine_data_now");
        List<Object> params = SqlStrUtil.query_list1_3(production_name);
        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sql, params);

        if (mapsList == null || mapsList.size() == 0) {
            return 0;
        }
        int num = (int) mapsList.get(0).get("production_num");
        return num;
    }

    /**
     * 查询 当前工件 检测结果
     */
    public static String getResult(String production_name) {
        String sql = SqlStrUtil.query_sql1_3("machine_data_now");
        List<Object> params = SqlStrUtil.query_list1_3(production_name);
        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sql, params);

        if (mapsList == null || mapsList.size() == 0) {
            return "-1";
        }
        String result = (String) mapsList.get(0).get("result");
        return result;
    }
}
