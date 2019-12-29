package com.lichang.utils;

import com.lichang.utils.LoggerUtil;
import javafx.scene.chart.StackedBarChart;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类用于 合成 SQL语句，简化
 * 1. 方法可自行定义添加
 * 2. 通过List转Array，进驻参数，防止SQL注入
 */
public class SqlStrUtil {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 查询 语句
     */
    //语句1： 查询*
    public static String query_sql1(String table) {
        String sqlStr = "select * from "
                + table;
        return sqlStr;
    }

    //语句1_1： 查询*  条件：username、password
    public static String query_sql1_1(String table) {
        String sqlStr = "select * from "
                + table
                + " where username= ? and password= ?";

        return sqlStr; // 注意，表名不能通过占位符，需要拼接字符串
    }

    public static List<Object> query_list1_1(String username, String password) {
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(username);
        paramsList.add(password);
        return paramsList; // 占位符参数列表
    }

    //语句1_2： 查询*   条件：production_num
    public static String query_sql1_2(String table) {
        String sqlStr = "select * from "
                + table
                + " where production_num = ?";
        return sqlStr;
    }

    public static List<Object> query_list1_2(int production_num) {
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(production_num);
        return paramsList; // 占位符参数列表
    }

    //语句1_3： 查询*  条件：production_name
    public static String query_sql1_3(String table) {
        String sqlStr = "select * from "
                + table +
                " where production_name = ?";
        return sqlStr;
    }

    public static List<Object> query_list1_3(String production_name) {
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(production_name);
        return paramsList; // 占位符参数列表
    }

    //语句1_4： 查询*  条件：time
    public static String query_sql1_4(String table) {
        String sqlStr = "select * from "
                + table +
                " where time = ?";
        return sqlStr;
    }

    public static List<Object> query_list1_4(String time) {
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(time);
        return paramsList; // 占位符参数列表
    }

    //语句1_5：查询* 条件：production_name 和 production_num
    public static String query_sql1_5(String table) {
        String sqlStr = "select * from "
                + table +
                " where production_name = ? and production_num = ?";
        return sqlStr;
    }

    public static List<Object> query_list1_5(String production_name, int production_num) {
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(production_name);
        paramsList.add(production_num);
        return paramsList; // 占位符参数列表
    }

    //语句2： 查询count（id）
    public static String query_sql2(String table) {
        String sqlStr = "select count(id) from "
                + table;
        return sqlStr;
    }

    //语句2_1： 查询count（id） 条件：production_name
    public static String query_sql2_1(String table) {
        String sqlStr = "select count(id) from "
                + table +
                " where production_name = ?";
        return sqlStr;
    }

    public static List<Object> query_list2_1(String production_name) {
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(production_name);
        return paramsList; // 占位符参数列表
    }

    //语句3： 查询 表中最后一条记录（通过id排序）
    public static String query_sql3(String table) {
        String sqlStr = "SELECT * FROM " +
                table +
                " ORDER BY id DESC LIMIT 1";

        return sqlStr;
    }

    //语句3_1： 查询 表中最后一条记录（通过id排序）  条件： production_name
    public static String query_sql3_1(String table) {
        String sqlStr = "SELECT * FROM " +
                table +
                " where production_name = ?" +
                " ORDER BY id DESC LIMIT 1";

        return sqlStr;
    }

    public static List<Object> query_list3_1(String production_name) {
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(production_name);
        return paramsList; // 占位符参数列表
    }

    /**
     * 更新 语句
     */
    //语句1： 更新password  条件：username
    public static String update_sql1(String table) {
        String sqlStr = "update "
                + table
                + " set password = ?"
                + " where username = ?";
        return sqlStr;
    }

    public static List<Object> update_list1(String username, String password) {
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(password);
        paramsList.add(username);
        return paramsList; // 占位符参数列表
    }

    //语句2： 更新*（除id列） 条件：name  说明：仅针对 产品表 expert_production
    public static String update_sql2() {
        String sqlStr = "update expert_production " +
                "set base_metal_a = ?, " +
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
                "voltage_advice = ?, " +
                "speed_advice = ?, " +
                "extension_advice = ?, " +
                "value_limit = ?, " +
                "current_practical = ?, " +
                "voltage_practical = ?, " +
                "speed_practical = ?, " +
                "extension_practical = ? " +
                "where name = ?";
        return sqlStr;
    }

    public static List<Object> update_list2(String name,
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
                                            String voltage_advice,
                                            String speed_advice,
                                            String extension_advice,
                                            String value_limit,
                                            String current_practical,
                                            String voltage_practical,
                                            String speed_practical,
                                            String extension_practical
    ) {
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
        paramsList.add(voltage_advice);
        paramsList.add(speed_advice);
        paramsList.add(extension_advice);
        paramsList.add(value_limit);
        paramsList.add(current_practical);
        paramsList.add(voltage_practical);
        paramsList.add(speed_practical);
        paramsList.add(extension_practical);
        paramsList.add(name);
        return paramsList; // 占位符参数列表
    }

    //语句3： 更新 指定列（除id列） 说明：给colsName，更新colsName中所给出的列。
    public static String update_sql3(String table, Object[] colsName) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 1; i < colsName.length; i++) {
            if (i == colsName.length - 1) {
                sb.append(colsName[i] + " = ? ");
            } else {
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

    /**
     * 插入 语句
     */
    //语句1： 插入*（除id列）  说明：仅针对 产品表 expert_production
    public static String insert_sql1() {
        String sqlStr = "insert into expert_production (" +
                "name, " +
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
                "voltage_advice, " +
                "speed_advice, " +
                "extension_advice, " +
                "value_limit, " +
                "current_practical, " +
                "voltage_practical, " +
                "speed_practical, " +
                "extension_practical) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return sqlStr;
    }

    public static List<Object> insert_list1(String name,
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
                                            String voltage_advice,
                                            String speed_advice,
                                            String extension_advice,
                                            String value_limit,
                                            String current_practical,
                                            String voltage_practical,
                                            String speed_practical,
                                            String extension_practical
    ) {
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
        paramsList.add(voltage_advice);
        paramsList.add(speed_advice);
        paramsList.add(extension_advice);
        paramsList.add(value_limit);
        paramsList.add(current_practical);
        paramsList.add(voltage_practical);
        paramsList.add(speed_practical);
        paramsList.add(extension_practical);

        return paramsList; // 占位符参数列表
    }

    //语句2： 插入*  说明：给colCount 即该表的总列数（包括id）
    public static String insert_sql2(String table, int colCount) {
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
                + ")";
        return sqlStr;
    }

    /**
     * 其他
     */
    //当进行过 删除操作后，恢复自增ID正常顺序
    public static String[] recoverId(String table) {
        String sqlStr = "alter table " + table + " DROP COLUMN id";
        String sqlStr2 = "alter table " + table + " ADD COLUMN id INT NULL AUTO_INCREMENT FIRST ,ADD PRIMARY KEY (id)";

        return new String[] {sqlStr, sqlStr2};
    }
}
