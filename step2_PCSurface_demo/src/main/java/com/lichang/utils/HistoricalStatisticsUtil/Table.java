package com.lichang.utils.HistoricalStatisticsUtil;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * 用于 历史统计与查询页面 的表格
 */
public class Table {
    public static List<Map<String, Object>> getData_table4(String table) {
        String sqlStr = SqlStrUtil.generateSql5(table);
        List<Map<String, Object>> maps = JdbcTemplateUtil.queryMult(sqlStr);
        return maps;
    }
}
