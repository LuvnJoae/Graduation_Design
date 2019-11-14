package com.lichang.utils;

import com.lichang.DBbeans.Machine_data;
import com.lichang.utils.dao.JdbcTemplateUtil;
import com.lichang.utils.dao.SqlStrUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 用于获取 生成折线图 所需的数据。
 */
public class LineChartUtil {
    private static Logger log = LoggerUtil.getLogger();
    public static List<Machine_data> getDataBeans() {
        log.debug("获取生成折线图所需的 machine_data表的数据");

        String table = "Machine_data"; // 表名

        //生成sql语句，与参数列表
        String sqlStr = SqlStrUtil.generateSql2(table);
        List<Object> params = SqlStrUtil.generateList2(1);

        //查询machine_data数据表中的数据，并以Bean形式返回成List
        List<Machine_data> machine_data_BeansList =
                (List<Machine_data>) JdbcTemplateUtil.queryMultForBean(sqlStr, Machine_data.class, params);
        return machine_data_BeansList;
    }

}
