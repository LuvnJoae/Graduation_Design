package com.lichang.utils.HistoricalStatisticsUtils;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;

import java.util.List;
import java.util.Map;

/**
 * 用于 查询指定表 的全部内容
 */
public class TableUtil {
    public static List<Map<String, Object>> getData(String table) {
        String sqlStr = SqlStrUtil.query_sql1(table);
        List<Map<String, Object>> maps = JdbcTemplateUtil.queryMult(sqlStr);
        return maps;
    }
}
