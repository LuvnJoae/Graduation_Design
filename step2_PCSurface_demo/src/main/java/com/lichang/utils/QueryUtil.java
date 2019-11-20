package com.lichang.utils;

import com.lichang.utils.dao.JdbcTemplateUtil;
import com.lichang.utils.dao.SqlStrUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 用于 具有同样查询需求的 查询 操作
 */
public class QueryUtil {

    private static Logger log = LoggerUtil.getLogger();
    /**
     * 针对 以 num 为检索信息的SQl查询
     * @param table 表名
     * @param num  工件编号
     * @param cla  bean类
     * @return
     */
    public static List<?> queryForNum(String table, int num, Class cla) {
        log.debug("针对 以 num 为检索信息的SQl查询");
        //生成sql语句，与参数列表
        String sqlStr = SqlStrUtil.generateSql2(table); //table 表名
        List<Object> params = SqlStrUtil.generateList2(num); // num为 工件编号

        //以num 为查询条件， 查询 table 表的数据，并以Bean形式返回List
        List<?> beansList = JdbcTemplateUtil.queryMultForBean(sqlStr, cla, params);
        return beansList;
    }
}
