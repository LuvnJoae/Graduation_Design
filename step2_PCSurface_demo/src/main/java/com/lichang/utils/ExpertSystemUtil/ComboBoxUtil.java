package com.lichang.utils.ExpertSystemUtil;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;

import java.util.List;
import java.util.Map;

/**
 * 用于下拉框内容与数据库的连接
 */
public class ComboBoxUtil {
    public static List<Map<String, Object>> getData(String table) {
        String sqlStr = SqlStrUtil.generateSql5(table);
        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sqlStr);
        return mapsList;
    }
}
