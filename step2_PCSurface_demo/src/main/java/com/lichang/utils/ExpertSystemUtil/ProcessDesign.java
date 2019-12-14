package com.lichang.utils.ExpertSystemUtil;

import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于工艺流程设计 内容与数据库的连接
 */
public class ProcessDesign {
    /**
     * 获取专家系统相应表中的全部数据 给 下拉框
     *
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

    /**
     * 更新产品
     */
    public static boolean updateData(String name,
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
        String sqlStr = SqlStrUtil.generateSql7("expert_production");
        List<Object> params = SqlStrUtil.generateList7(
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

    /**
     * 获取上次登录时的 产品选择 的下标，用于第二次登录时直接展示界面
     *      当用于多机时，采用 主机名 + ip 地址作为身份验证
     */
    public static String getLastProductionIndex() {

        String last_production_index = "-1"; //一旦出错，则默认为-1
        try {
            String ip = String.valueOf(InetAddress.getLocalHost()); //查询当前 主机名 + ip地址

            //查询是否存在该主机名+ip
            String sqlStr = "Select * from last_login_status where ip = ?";
            ArrayList<Object> params = new ArrayList<>();
            params.add(ip);

            Map<String, Object> map = JdbcTemplateUtil.querySingle(sqlStr, params);

            //不存在则返回默认值-1，存在则返回相应值
            if (map==null) {
                return last_production_index;
            }else {
                last_production_index = (String) map.get("last_production_index");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return last_production_index;
    }

    /**
     * 记录上次登录时的 产品选择 的下标，用于第二次登录时直接展示界面
     *      当用于多机时，采用 主机名 + ip 地址作为身份验证
     */
    public static boolean setLastProductionIndex(String lastProductionIndex) {

        boolean updateResult = false;
        try {
            String ip = String.valueOf(InetAddress.getLocalHost()); //查询当前 ip地址

            //查询是否存在该主机名+ip
            String sqlStr = "select * from last_login_status where ip = ?";
            ArrayList<Object> params = new ArrayList<>();
            params.add(ip);

            Map<String, Object> map = JdbcTemplateUtil.querySingle(sqlStr, params);

            //判断数据库是否已有此主机名+ip信息
            String sqlStr2;
            params = new ArrayList<>();
            //无，则新添加
            if (map == null) {
                sqlStr2 = "insert into last_login_status (ip, last_production_index) values (?, ?)";
                params.add(ip);
                params.add(lastProductionIndex);
            } else {
                //有，则对原数据进行更改
                sqlStr2 = "update last_login_status set last_production_index = ? where ip = ?";
                params.add(lastProductionIndex);
                params.add(ip);
            }

            updateResult = JdbcTemplateUtil.update(sqlStr2, params);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return updateResult;
    }
}
