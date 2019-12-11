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
     * 合成Sql语句 1 ： 查询
     *      表  table
     *      查询参数 username，password
     * @param table
     * @return
     */
    public static String generateSql1(String table) {
        log.debug("查询：Sql合成1：" + table);
        String sqlStr = "select * from "
                + table
                + " where username= ? and password= ?";

        return sqlStr; // 注意，表名不能通过占位符，需要拼接字符串
    }

    public static List<Object> generateList1(String username, String password) {
        log.debug("查询Sql的params合成 ：" + username + "   " + password);
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(username);
        paramsList.add(password);
        return paramsList; // 占位符参数列表
    }

    /**
     * 合成Sql语句 2 ： 查询
     *      表  table
     *      查询参数 num
     * @param table
     * @return
     */
    public static String generateSql2(String table) {
        log.debug("查询：Sql合成2：" + table);
        String sqlStr = "select * from "
                + table
                + " where num = ?";
        return sqlStr;
    }

    public static List<Object> generateList2(int num) {
        log.debug("查询Sql的params合成 ：" + num);
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(num);
        return paramsList; // 占位符参数列表
    }

    /**
     * 合成Sql语句 3 ： 修改
     *      修改表 table
     *      查询参数 username
     *      修改参数 password
     * @param table
     * @return
     */
    public static String generateSql3(String table) {
        log.debug("修改：Sql合成3：" + table);
        String sqlStr = "update "
                + table
                + " set password = ?"
                + " where username = ?";
        return sqlStr;
    }

    public static List<Object> generateList3(String username, String password) {
        log.debug("修改Sql的params合成 ：" + username + "   " + password);
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(password);
        paramsList.add(username);
        return paramsList; // 占位符参数列表
    }

    /**
     * 合成Sql 语句4： 查询
     *      查询 总记录数
     *      查询表 table
     *      查询参数 id
     * @param table
     * @return
     */
    public static String generateSql4(String table) {
        log.debug("查询：Sql合成4：" + table);
        String sqlStr = "select count(id) from "
                + table;
        return sqlStr;
    }

    /**
     * 合成Sql 语句5： 查询
     *      查询 表内的所有记录
     *      查询表 table
     * @param table
     * @return
     */
    public static String generateSql5(String table) {
        log.debug("查询：Sql合成5：" + table);
        String sqlStr = "select * from "
                + table;
        return sqlStr;
    }

    /**
     *  合成Sql 语句6： 插入新记录
     * @param table
     * @return
     */
    public static String generateSql6(String table) {
        log.debug("插入：Sql合成6：" + table);
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
        log.debug("插入Sql的params合成 ");
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
}
