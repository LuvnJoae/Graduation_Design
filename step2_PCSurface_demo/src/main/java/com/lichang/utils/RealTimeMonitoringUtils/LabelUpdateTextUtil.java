package com.lichang.utils.RealTimeMonitoringUtils;

import com.lichang.DBbeans.Machine_data_now;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 用于对Label进行数据更新
 */
public class LabelUpdateTextUtil {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * faultNumberLabel1 ： 查询故障记录总数
     * @return
     */
    public static Long faultNumberLabel1() {
        String sql = SqlStrUtil.query_sql3("machine_fault_data");
        Long count = JdbcTemplateUtil.queryCount(sql);
        return count;
    }

    /**
     * completedNumberLabel1: 查询已完成工件总记录数
     */
    public static Long CompletedNumberLabel1() {
        String sql = SqlStrUtil.query_sql3("machine_data_brief");
        Long count = JdbcTemplateUtil.queryCount(sql);
        return count;
    }

    /**
     * NumberLabel： 展示 当前的工件编号
     */
    public static int numberLabel() {
        String sqlStr = "select * from machine_data_now";
        List<Machine_data_now> machine_data_now_BeansList = (List<Machine_data_now>)
                JdbcTemplateUtil.queryMultForBean(sqlStr, Machine_data_now.class);

        int num = 0;
        try {
            num = machine_data_now_BeansList.get(0).getNum();
        } catch (Exception e) {
            log.warn("产品编号 数据不存在！");
            return -1;
        }

        return num;
    }

    /**
     * ResultLabel: 展示 当前工件的检测结果
     */
    public static String resultLabel() {
        String sqlStr = "select * from machine_data_now";
        List<Machine_data_now> machine_data_now_BeansList = (List<Machine_data_now>)
                JdbcTemplateUtil.queryMultForBean(sqlStr, Machine_data_now.class);

        String result;
        try {
            result = machine_data_now_BeansList.get(0).getResult();
        } catch (Exception e) {
            log.warn("产品编号 数据不存在！");
            return "工件不存在";
        }

        return result;
    }
}
