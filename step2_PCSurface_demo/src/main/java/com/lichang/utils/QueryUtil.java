package com.lichang.utils;

import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 用于 查询 操作，统一处理
 */
public class QueryUtil {

    private static Logger log = LoggerUtil.getLogger();

    /**
     * 自定义语句查找
     * @param sqlStr  自定义的sql语句
     * @param params  sql语句中的参数
     * @param cla     bean类对象
     * @return
     */
    public static Object query(String sqlStr, List<Object> params, Class cla) {
        return JdbcTemplateUtil.queryMultForBean(sqlStr, cla, params);
    }

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
