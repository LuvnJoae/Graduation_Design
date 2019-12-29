package com.lichang.utils.ExpertSystemUtils;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KnowledgeBaseUtil {
    /**
     * 用于获取资料库 表格内容
     */
    public static List<Map<String, Object>> getData(String table) {
        String sqlStr = SqlStrUtil.query_sql1(table);
        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sqlStr);
        return mapsList;
    }

    /**
     *  用于向 专家系统各表中 添加一条记录（全部列）
     */
    public static boolean addData(String table, int colCount, List<Object> params) {
        String sqlStr = SqlStrUtil.insert_sql2(table, colCount);
        boolean result = JdbcTemplateUtil.update(sqlStr, params);

        return result;
    }

    /**
     * 用于向 专家系统各表中 删除一条记录（全部列）
     */
    public static boolean deleteData(String table, Object id) {
        String sqlStr = "delete from "
                + table
                + " where id = ?";
        ArrayList<Object> params = new ArrayList<>();
        params.add(id);

        boolean result = JdbcTemplateUtil.update(sqlStr, params);

        //恢复自增ID正常序列
        if (result) {
            JdbcTemplateUtil.update(SqlStrUtil.recoverId(table)[0]);
            JdbcTemplateUtil.update(SqlStrUtil.recoverId(table)[1]);
        }

        return result;
    }

    /**
     * 用于向 专家系统各表中 修改一条记录（全部列） 以id为条件
     *      colsName为：包括id在内的所有列名
     *      colsData为：包括id在内的所有列值
     */
    public static boolean changeData(String table, Object[] colsName, Object[] colsData) {
        String sqlStr = SqlStrUtil.update_sql3(table, colsName);
        ArrayList<Object> params = new ArrayList<>();
        Object id = colsData[0]; //id为第一个列值
        //更改一下 参数的位置，将id放在最后（配合sql语句）
        for (int i = 1; i < colsName.length; i++) {
            params.add(colsData[i]);
        }
        params.add(id);
        boolean result = JdbcTemplateUtil.update(sqlStr, params);
        return result;
    }
}
