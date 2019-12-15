package com.lichang.utils.ExpertSystemUtil;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;

import java.util.List;
import java.util.Map;

public class KnowledgeBase {
    /**
     * 用于获取资料库 表格内容
     */
    public static List<Map<String, Object>> getData(String table) {
        String sqlStr = SqlStrUtil.generateSql5(table);
        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sqlStr);
        return mapsList;
    }
}
