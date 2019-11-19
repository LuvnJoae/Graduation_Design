package com.lichang.utils;

import com.lichang.DBbeans.Machine_data;
import com.lichang.ui.chart.LineChart;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 用于获取 数据库中的数据，给Table
 */
public class TableUtil {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 用于 实时监测界面的 JTable（机器数据）
     *      直接复用这一界面 LineChartUtil的代码（功能相同）
     * @return
     */
    public static List<Machine_data> getDataBeans() {
       log.debug("实时监测界面 JTable：获取机器数据");
        return LineChartUtil.getDataBeans(); // 返回焊机数据（List）
    }
}
