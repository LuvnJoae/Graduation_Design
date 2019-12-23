package com.lichang.utils;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于参数监测表 的 value_limit判断
 */
public class LimitRuleUtil {
    private static Logger log = LoggerUtil.getLogger();

    //阈值 提示（当超出设置阈值后，数据自动变色）
    public static void addValueLimit(List<Map<String, Object>> expert_production_mapsList, String prodution_name, JTable table) {
        Map<String, Object> productionMap = new HashMap<>();
        for (Map<String, Object> map : expert_production_mapsList) {
            if (map.get("name").equals(prodution_name)) {
                productionMap = map;
                break;
            }
        }
        log.debug("****addLimit check start***");
        //如果该产品还未设定 焊接参数建议值，则不添加阈值limit，直接返回
        if (productionMap.get("voltage_advice") == null
                || productionMap.get("voltage_advice").equals("0")
                || productionMap.get("voltage_advice").equals("")) {
            return;
        } else if (productionMap.get("current_advice") == null
                || productionMap.get("current_advice").equals("0")
                || productionMap.get("current_advice").equals("")) {
            return;
        } else if (productionMap.get("speed_advice") == null
                || productionMap.get("speed_advice").equals("0")
                || productionMap.get("speed_advice").equals("")) {
            return;
        }
        log.debug("****addLimit check end***");

        Object expert_production_voltageCol; //参考电压
        Object expert_production_currentCol; //参考电流
        Object expert_production_speedCol; //参考速度

        //判断是否存在 实际值,若存在，以实际值为标准，若不存在，以建议值为标准
        //电压
        if (productionMap.get("voltage_practical") == null
                || productionMap.get("voltage_practical").equals("0")
                || productionMap.get("voltage_practical").equals("")) {
            expert_production_voltageCol = productionMap.get("voltage_advice");
        } else {
            expert_production_voltageCol = productionMap.get("voltage_practical");
        }
        //电流
        if (productionMap.get("current_practical") == null
                || productionMap.get("current_practical").equals("0")
                || productionMap.get("current_practical").equals("")) {
            expert_production_currentCol = productionMap.get("current_advice");
        } else {
            expert_production_currentCol = productionMap.get("current_practical");
        }
        //速度
        if (productionMap.get("speed_practical") == null
                || productionMap.get("speed_practical").equals("0")
                || productionMap.get("speed_practical").equals("")) {
            expert_production_speedCol = productionMap.get("speed_advice");
        } else {
            expert_production_speedCol = productionMap.get("speed_practical");
        }

        //limit不存在时，直接返回
        if (productionMap.get("value_limit") == null || productionMap.get("value_limit").equals("")) {
            return;
        }

        String limitRule = (String) productionMap.get("value_limit"); //获得 value_limit字符串

        if (limitRule.split("%").length == 1) {
            String currentLimitRule = limitRule.split("%")[0]; //电压 value_limit字符串

            limit_main(currentLimitRule, expert_production_voltageCol, 1, table);
        } else if (limitRule.split("%").length == 2) {
            String currentLimitRule = limitRule.split("%")[0]; //电压 value_limit字符串
            String voltageLimitRule = limitRule.split("%")[1]; //电流 value_limit字符串

            limit_main(currentLimitRule, expert_production_currentCol, 1, table);
            limit_main(voltageLimitRule, expert_production_voltageCol, 2, table);
        } else if (limitRule.split("%").length == 3) {
            String currentLimitRule = limitRule.split("%")[0]; //电压 value_limit字符串
            String voltageLimitRule = limitRule.split("%")[1]; //电流 value_limit字符串
            String speedLimitRule = limitRule.split("%")[2]; //电流 value_limit字符串

            limit_main(currentLimitRule, expert_production_currentCol, 1, table);
            limit_main(voltageLimitRule, expert_production_voltageCol, 2, table);
            limit_main(speedLimitRule, expert_production_speedCol, 3, table);
        } else {
            return;
        }

    }

    //limit主方法
    private static void limit_main(String limitRule, Object expert_production_Col, int colSeq, JTable table) {
        log.debug("**********进入limit_main*********");
        try {
            String[] ruleArray = limitRule.replaceAll(" ", "").split(";"); //拆分成单个的rule
            for (int i = 0; i < ruleArray.length; i++) {
                String[] sinlgeRuleArray = ruleArray[i].split(",");
                String seqRange = sinlgeRuleArray[0]; //rule中的seq范围
                String valueRange = sinlgeRuleArray[1]; //rule中的值范围

                //定义 序号范围
                String seqMinStr = seqRange.split("-")[0];
                String seqMaxSTr = seqRange.split("-")[1];
                int seqMin;
                int seqMax;
                //判断是否 有 s 和 e （s为第一个序号, e为最后一个序号）
                if (seqMinStr.equals("s")) {
                    seqMin = 1;
                } else {
                    seqMin = Integer.valueOf(seqMinStr);
                }
                if (seqMaxSTr.equals("e")) {
                    seqMax = table.getRowCount();
                } else {
                    seqMax = Integer.parseInt(seqMaxSTr);
                }
                //如果给的seq超限，则默认为边界值。
                if (seqMin < 1) {
                    seqMin = 1;
                }
                if (seqMax > table.getRowCount()) {
                    seqMax = table.getRowCount();
                }

                //定义 值的范围
                double valueMin;
                double valueMax;
                //判断是否value参数为 1 或 单值， 为1则默认limit为建议值（若有实际值则为实际值），单值则默认固定这个单值
                if (valueRange.split("-").length == 1) {
                    if (valueRange.split("-")[0].equals("1")) {
                        valueMin = Double.valueOf((String)expert_production_Col);
                        valueMax = valueMin;
                    }else {
                        valueMin = Double.valueOf(valueRange.split("-")[0]);
                        valueMax = valueMin;
                    }
                } else {
                    valueMin = Double.valueOf(valueRange.split("-")[0]);
                    valueMax = Double.valueOf(valueRange.split("-")[1]);
                }
                //上色：
                //符合limit: 绿色
                //超出limit：红色
                //低于limit: 蓝色
                //未设置limit: 灰色（默认颜色）
                for (int j = seqMin-1; j < seqMax; j++) {
                    if ((double) table.getValueAt(j, colSeq) > valueMax) {
                        table.setValueAt("<html>" + "<font color='red'>" + table.getValueAt(j, colSeq) + "</font>" + "</html>", j, colSeq);
                    } else if ((double) table.getValueAt(j, colSeq) < valueMin) {
                        table.setValueAt("<html>" + "<font color='blue'>" + table.getValueAt(j, colSeq) + "</font>" + "</html>", j, colSeq);
                    } else {
                        table.setValueAt("<html>" + "<font color='green'>" + table.getValueAt(j, colSeq) + "</font>" + "</html>", j, colSeq);
                    }
                }
                log.debug("********执行完毕********");
            }
        } catch (Exception e) {
            log.error("出现错误",e);
        }
    }

}
