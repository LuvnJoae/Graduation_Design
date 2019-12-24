package com.lichang.utils;

import com.lichang.utils.dao.JdbcTemplateUtil;

import java.util.List;
import java.util.Map;

/**
 * 用于 查看详情 Jialog工具
 */
public class DetailsUtil {
    /**
     *  根据time查询 machine_fault_data表，获得相应记录
     */
    public static Map<String, Object> getFaultRecordForTime(String time) {
        String sqlStr = SqlStrUtil.query_sql1_4("machine_fault_data");
        List<Object> params = SqlStrUtil.query_list1_4(time);
        Map<String, Object> map = JdbcTemplateUtil.querySingle(sqlStr, params);
        return map;
    }

    /**
     *  根据time查询 machine_data_brief表，获得相应记录
     */
    public static Map<String, Object> getBriefRecordForTime(String time) {
        String sqlStr = SqlStrUtil.query_sql1_4("machine_data_brief");
        List<Object> params = SqlStrUtil.query_list1_4(time);
        Map<String, Object> map = JdbcTemplateUtil.querySingle(sqlStr, params);
        return map;
    }

    /**
     * 根据production_name 和 production_num 查询 machine_data_all表，获得相应参数记录
     */
    public static List<Map<String, Object>> getDetailData(String production_name, int production_num) {
        String sqlStr = SqlStrUtil.query_sql1_5("machine_data_all");
        List<Object> params = SqlStrUtil.query_list1_5(production_name, production_num);
        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sqlStr, params);
        return mapsList;
    }

    /**
     * 获取指定表中的所有数据
     * @param table
     * @return
     */
    public static List<Map<String, Object>> getData(String table) {
        String sqlStr = SqlStrUtil.query_sql1(table);
        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sqlStr);
        return mapsList;
    }
}
