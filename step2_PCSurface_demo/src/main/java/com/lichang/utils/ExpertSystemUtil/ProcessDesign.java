package com.lichang.utils.ExpertSystemUtil;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * 用于工艺流程设计 内容与数据库的连接
 */
public class ProcessDesign {
    /**
     * 获取专家系统相应表中的全部数据 给 下拉框
     * @param table
     * @return
     */
    public static List<Map<String, Object>> getData(String table) {
        String sqlStr = SqlStrUtil.generateSql5(table);
        List<Map<String, Object>> mapsList = JdbcTemplateUtil.queryMult(sqlStr);
        return mapsList;
    }

    /**
     * 添加产品： 将下拉框内容 添加到数据库
     */
    public static boolean setData(String name,
                               String base_metal_a,
                               String base_metal_b,
                               String weld_method,
                               String weld_metal,
                               String auxiliary_materials,
                               String workpiece_thickness,
                               String weld_joint_joint,
                               String weld_joint_groove,
                               String weld_joint_weldposition,
                               String thermal_parameters,
                               String extra_1,
                               String current_advice,
                               String voltage_arc_advice,
                               String speed_advice,
                               String extension_advice,
                               String extra_2,
                               String current_practical,
                               String voltage_arc_practical,
                               String speed_practical,
                               String extension_practical
                               ) {
        String sqlStr = SqlStrUtil.generateSql6("expert_production");
        List<Object> params = SqlStrUtil.generateList6(
                name,
                base_metal_a,
                base_metal_b,
                weld_method,
                weld_metal,
                auxiliary_materials,
                workpiece_thickness,
                weld_joint_joint,
                weld_joint_groove,
                weld_joint_weldposition,
                thermal_parameters,
                extra_1,
                current_advice,
                voltage_arc_advice,
                speed_advice,
                extension_advice,
                extra_2,
                current_practical,
                voltage_arc_practical,
                speed_practical,
                extension_practical
                );
        boolean result = JdbcTemplateUtil.update(sqlStr, params); //插入结果
        return result;
    }
}
