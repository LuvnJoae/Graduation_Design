package com.lichang.utils.ExpertSystemUtil;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
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

    /**
     *  用于向 专家系统各表中 添加一条记录（全部列）
     */
    public static boolean addData(String table, int colCount, List<Object> params) {
        String sqlStr = SqlStrUtil.generateSql8(table, colCount);
        boolean result = JdbcTemplateUtil.update(sqlStr, params);

        System.out.println("******************");
        System.out.println(sqlStr);
        System.out.println("******************");
        System.out.println(params);
        return result;
    }

    public static boolean deleteData(String table, Object id) {
        String sqlStr = "delete from "
                + table
                + " where id = ?";
        ArrayList<Object> params = new ArrayList<>();
        params.add(id);

        boolean result = JdbcTemplateUtil.update(sqlStr, params);
        return result;
    }
}
