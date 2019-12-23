package com.lichang.demo;

import com.lichang.utils.HistoricalStatisticsUtils.TableUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class demo2 {

    public static void main(String[] args) throws UnknownHostException, ParseException {
        //通过遍历mapsList，查询每天的统计量
        String sqlStr = SqlStrUtil.query_sql1_3("machine_data_brief");
        List<Object> params = SqlStrUtil.query_list1_3("production_default");
        List<Map<String, Object>> machine_data_brief_mapsList = JdbcTemplateUtil.queryMult(sqlStr, params);

        HashMap<String, Integer> timeA_map = new LinkedHashMap<>(); //时间-优秀 map
        HashMap<String, Integer> timeB_map = new LinkedHashMap<>(); //时间-合格 map
        HashMap<String, Integer> timeC_map = new LinkedHashMap<>(); //时间-隐患 map
        HashMap<String, Integer> timeD_map = new LinkedHashMap<>(); //时间-次品 map

        //time 分割成只有日期的字符串
        String nowTime; //当前的time
        for (Map<String, Object> map : machine_data_brief_mapsList) {

            nowTime = map.get("time").toString().split("\\.")[0].split(" ")[0];

            //初值判断
            if (!timeA_map.containsKey(nowTime)) {
                timeA_map.put(nowTime, 0);
            }
            if (!timeB_map.containsKey(nowTime)) {
                timeB_map.put(nowTime, 0);
            }
            if (!timeC_map.containsKey(nowTime)) {
                timeC_map.put(nowTime, 0);
            }
            if (!timeD_map.containsKey(nowTime)) {
                timeD_map.put(nowTime, 0);
            }
            //有则加1
            if (map.get("result").equals("优秀")) {
                timeA_map.put(nowTime, timeA_map.get(nowTime) + 1);
            } else if (map.get("result").equals("合格")) {
                timeB_map.put(nowTime, timeB_map.get(nowTime) + 1);
            } else if (map.get("result").equals("隐患")) {
                timeC_map.put(nowTime, timeC_map.get(nowTime) + 1);
            } else if (map.get("result").equals("次品")) {
                timeD_map.put(nowTime, timeD_map.get(nowTime) + 1);
            }
        }

        // 将map中的信息添加到dataset中。
        for (Map.Entry<String, Integer> entry : timeA_map.entrySet()) {
            System.out.println(entry.getValue() + "***" + entry.getKey());
        }
        for (Map.Entry<String, Integer> entry : timeB_map.entrySet()) {
            System.out.println(entry.getValue() + "***" + entry.getKey());
        }
        for (Map.Entry<String, Integer> entry : timeC_map.entrySet()) {
            System.out.println(entry.getValue() + "***" + entry.getKey());
        }
        for (Map.Entry<String, Integer> entry : timeD_map.entrySet()) {
            System.out.println(entry.getValue() + "***" + entry.getKey());
        }




    }
}

