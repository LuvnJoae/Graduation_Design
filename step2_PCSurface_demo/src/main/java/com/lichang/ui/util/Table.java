package com.lichang.ui.util;

import com.lichang.DBbeans.Machine_data;
import com.lichang.DBbeans.Machine_fault_data;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.QueryUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 用于对JTable的数据处理、添加等
 */
public class Table {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 用于 实时监测界面的 JTable2（机器数据）
     * @return
     */
    public static List<Machine_data> getDataBeans(int num) {
        log.debug("实时监测界面 JTable2：获取机器数据");
        return (List<Machine_data>)
                QueryUtil.queryForNum("Machine_data", num, Machine_data.class); // 返回焊机数据（List）
    }

    /**
     * 用于 实时监测界面的 JTable1（故障数据）
     * @return
     */
    public static List<Machine_fault_data> getFaultDataBeans(int num) {
        log.debug("实时监测界面 JTable1：获取故障数据");

        return (List<Machine_fault_data>)
                QueryUtil.queryForNum("Machine_fault_data", num, Machine_fault_data.class);
    }
}
