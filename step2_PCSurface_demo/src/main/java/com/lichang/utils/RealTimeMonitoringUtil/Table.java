package com.lichang.utils.RealTimeMonitoringUtil;

import com.lichang.DBbeans.Machine_data_all;
import com.lichang.DBbeans.Machine_data_now;
import com.lichang.DBbeans.Machine_fault_data;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 用于对JTable的数据处理、添加等
 */
public class Table {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 获取 机器表 指定 num 的 机器数据
     * @return
     */
    public static List<Machine_data_all> getDataBeans(int num) {
        log.debug("获取 机器表 指定 num 的 机器数据");

        String sqlStr = SqlStrUtil.generateSql2("Machine_data_all"); //table 表名
        List<Object> params = SqlStrUtil.generateList2(num); // num为 工件编号
        List<Machine_data_all> machine_data_all_BeansList = (List<Machine_data_all>)
                JdbcTemplateUtil.queryMultForBean(sqlStr, Machine_data_all.class, params);

        return machine_data_all_BeansList;
    }

    /**
     * 获取 当前工件 的数据
     * @return
     */
    public static List<Machine_data_now> getDataBeans_now() {
        log.debug("获取 当前工件 的数据");

        String sqlStr = "select * from machine_data_now";

        List<Machine_data_now> machine_data_now_BeanList = (List<Machine_data_now>)
                JdbcTemplateUtil.queryMultForBean(sqlStr, Machine_data_now.class);

        return machine_data_now_BeanList;
    }


    /**
     * 获取 故障表 指定 num 的 故障数据
     * @return
     */
    public static List<Machine_fault_data> getFaultDataBeans(int num) {
        log.debug("获取 故障表 指定 num 的 故障数据");

        String sqlStr = SqlStrUtil.generateSql2("Machine_fault_data"); //table 表名
        List<Object> params = SqlStrUtil.generateList2(num); // num为 工件编号
        List<Machine_fault_data> machine_fault_data_BeansList = (List<Machine_fault_data>)
                JdbcTemplateUtil.queryMultForBean(sqlStr, Machine_fault_data.class, params);

        return machine_fault_data_BeansList;
    }

    /**
     * 获取 故障表的 最新一条记录
     * @return
     */
    public static List<Machine_fault_data> getLastFaultDataBeans() {
        log.debug("获取 故障表的 最新一条记录");
        String sqlStr = "SELECT * FROM machine_fault_data ORDER BY id DESC LIMIT 1";
        List<Machine_fault_data> machine_fault_data_BeansList = (List<Machine_fault_data>)
                JdbcTemplateUtil.queryMultForBean(sqlStr, Machine_fault_data.class);

        return machine_fault_data_BeansList;
    }
}
