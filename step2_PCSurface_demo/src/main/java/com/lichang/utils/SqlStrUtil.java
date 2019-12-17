package com.lichang.utils;

import com.lichang.utils.LoggerUtil;
import javafx.scene.chart.StackedBarChart;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类用于 合成 SQL语句，简化
 *      1. 方法可自行定义添加
 *      2. 通过List转Array，进驻参数，防止SQL注入
 */
public class SqlStrUtil {

    private static Logger log = LoggerUtil.getLogger();

    /**
     * 语句 1 ： 查询  /条件：username、password
     */
    public static String generateSql1(String table) {
        log.debug("语句1：sql");
        String sqlStr = "select * from "
                + table
                + " where username= ? and password= ?";

        return sqlStr; // 注意，表名不能通过占位符，需要拼接字符串
    }

    public static List<Object> generateList1(String username, String password) {
        log.debug("语句1: params");
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(username);
        paramsList.add(password);
        return paramsList; // 占位符参数列表
    }

    /**
     * 语句 2 ： 查询  /条件：num
     */
    public static String generateSql2(String table) {
        log.debug("语句2：sql");
        String sqlStr = "select * from "
                + table
                + " where num = ?";
        return sqlStr;
    }

    public static List<Object> generateList2(int num) {
        log.debug("语句2：params");
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(num);
        return paramsList; // 占位符参数列表
    }

    /**
     * 语句 3 ： 更新 /条件：username 参数：password
     */
    public static String generateSql3(String table) {
        log.debug("语句3：sql");
        String sqlStr = "update "
                + table
                + " set password = ?"
                + " where username = ?";
        return sqlStr;
    }

    public static List<Object> generateList3(String username, String password) {
        log.debug("语句3：params");
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(password);
        paramsList.add(username);
        return paramsList; // 占位符参数列表
    }

    /**
     * 语句 4 ： 查询  总记录数  /参数： id
     */
    public static String generateSql4(String table) {
        log.debug("语句4：sql");
        String sqlStr = "select count(id) from "
                + table;
        return sqlStr;
    }

    /**
     * 语句 5 ： 查询  所有记录
     */
    public static String generateSql5(String table) {
        log.debug("查询：Sql合成5：" + table);
        String sqlStr = "select * from "
                + table;
        return sqlStr;
    }

    /**
     *  语句 6 ： 插入 单条记录  /参数：expert_production表 所有列（除了id）
     */
    public static String generateSql6(String table) {
        log.debug("语句6：sql");
        String sqlStr = "insert into "
                + table
                + " (name, " +
                "base_metal_a, " +
                "base_metal_b, " +
                "weld_method, " +
                "weld_metal, " +
                "auxiliary_materials, " +
                "workpiece_thickness, " +
                "weld_joint_joint, " +
                "weld_joint_groove, " +
                "weld_joint_weldposition, " +
                "thermal_parameters, " +
                "extra_1, " +
                "current_advice, " +
                "voltage_arc_advice, " +
                "speed_advice, " +
                "extension_advice, " +
                "extra_2, " +
                "current_practical, " +
                "voltage_arc_practical, " +
                "speed_practical, " +
                "extension_practical) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return sqlStr;
    }

    public static List<Object> generateList6(String name,
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
        log.debug("语句6：params");
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(name);
        paramsList.add(base_metal_a);
        paramsList.add(base_metal_b);
        paramsList.add(weld_method);
        paramsList.add(weld_metal);
        paramsList.add(auxiliary_materials);
        paramsList.add(workpiece_thickness);
        paramsList.add(weld_joint_joint);
        paramsList.add(weld_joint_groove);
        paramsList.add(weld_joint_weldposition);
        paramsList.add(thermal_parameters);
        paramsList.add(extra_1);
        paramsList.add(current_advice);
        paramsList.add(voltage_arc_advice);
        paramsList.add(speed_advice);
        paramsList.add(extension_advice);
        paramsList.add(extra_2);
        paramsList.add(current_practical);
        paramsList.add(voltage_arc_practical);
        paramsList.add(speed_practical);
        paramsList.add(extension_practical);

        return paramsList; // 占位符参数列表
    }

    /**
     * 语句7： 更新  /说明：针对 产品表 expert_production 除id外所有列
     */
    public static String generateSql7(String table) {
        log.debug("语句7：sql");
        String sqlStr = "update "
                + table
                + " set base_metal_a = ?, " +
                "base_metal_b = ?, " +
                "weld_method = ?, " +
                "weld_metal = ?, " +
                "auxiliary_materials = ?, " +
                "workpiece_thickness = ?, " +
                "weld_joint_joint = ?, " +
                "weld_joint_groove = ?, " +
                "weld_joint_weldposition = ?, " +
                "thermal_parameters = ?, " +
                "extra_1 = ?, " +
                "current_advice = ?, " +
                "voltage_arc_advice = ?, " +
                "speed_advice = ?, " +
                "extension_advice = ?, " +
                "extra_2 = ?, " +
                "current_practical = ?, " +
                "voltage_arc_practical = ?, " +
                "speed_practical = ?, " +
                "extension_practical = ? "
                + " where name = ?";
        return sqlStr;
    }

    public static List<Object> generateList7(String name,
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
        log.debug("语句7：params");
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(base_metal_a);
        paramsList.add(base_metal_b);
        paramsList.add(weld_method);
        paramsList.add(weld_metal);
        paramsList.add(auxiliary_materials);
        paramsList.add(workpiece_thickness);
        paramsList.add(weld_joint_joint);
        paramsList.add(weld_joint_groove);
        paramsList.add(weld_joint_weldposition);
        paramsList.add(thermal_parameters);
        paramsList.add(extra_1);
        paramsList.add(current_advice);
        paramsList.add(voltage_arc_advice);
        paramsList.add(speed_advice);
        paramsList.add(extension_advice);
        paramsList.add(extra_2);
        paramsList.add(current_practical);
        paramsList.add(voltage_arc_practical);
        paramsList.add(speed_practical);
        paramsList.add(extension_practical);
        paramsList.add(name);
        return paramsList; // 占位符参数列表
    }

    /**
     * 语句8：插入  /说明：对所有列进行插入  条件：已知表内列数
     */
    public static String generateSql8(String table, int colCount) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < colCount; i++) {
            if (i == colCount - 1) {
                sb.append("?");
            } else {
                sb.append("?, ");
            }

        }
        String sqlStr = "insert into "
                + table
                + " values ("
                + sb.toString()
                +")";
        return sqlStr;
    }

    /**
     * 语句9：更新  /说明：以id为条件，更新表内所有列（除了id）  条件：id， 其他：colsName为所有列值在内的数组
     */
    public static String generateSql9(String table, Object[] colsName) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 1; i < colsName.length; i++) {
            if (i == colsName.length - 1) {
                sb.append(colsName[i] + " = ? ");
            }else {
                sb.append(colsName[i] + " = ?, ");
            }
        }

        String sqlStr = "update "
                + table
                + " set "
                + sb.toString()
                + " where id = ?";
        return sqlStr;
    }

}
