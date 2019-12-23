package com.lichang.utils.HistoricalStatisticsUtils;

import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.*;
import java.util.List;

//TODO: yaozuo
//标记时间：2019/12/23 17:33  预解决时间：
//1. 优化这个类结构
//2. 字段名乱码不显示
//3. 颜色重合不显眼


public class LineChartUtil {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 生成折线模型 chart
     * @return
     */
    public static JFreeChart getLineChart(String production_name,
                                          int production_num,
                                          String title,
                                          String categoryAxisLable,
                                          String valueAxisLable) {
        log.debug("生成折线图模型");

//        List<Map<String, Object>> mapsList = getData(production_name, production_num); //获取数据list
//        CategoryDataset dataset = getDataset(mapsList); //获取dataset
        CategoryDataset dataset = getDataset();

                JFreeChart chart = ChartFactory.createLineChart(
                title,
                categoryAxisLable,
                valueAxisLable,
                dataset, // 数据集
                PlotOrientation.VERTICAL,
                true,  // 显示图例
                true, // 采用标准生成器
                false // 是否生成超链接
        );

        CategoryPlot plot= (CategoryPlot) chart.getPlot(); // 获取chart绘图对象plot

        plot.setBackgroundPaint(Color.white);  // 背景颜色
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);  // 背景网格虚线
        plot.setOutlinePaint(Color.BLACK); // 轮廓颜色
        plot.setNoDataMessage("数据加载失败");  // 错误提示

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer(); // 获取渲染器
        renderer.setBaseShapesVisible(true); // 显示数据点

        return chart;
    }

    //无参 重载
    public static JFreeChart getLineChart(String production_name, int production_num) {
        return getLineChart(production_name, production_num,"", "", "");
    }

    //无参 重载
    public static JFreeChart getLineChart(String production_name) {
        return getLineChart(production_name, -1,"", "", "");
    }

    /**
     * 将生成折线图 所需数据 添加到dataset中
     * @return
     */
    private static CategoryDataset getDataset() {
        log.debug("生成折线图所需 数据");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        //通过遍历mapsList，查询每天的统计量
        String sqlStr = SqlStrUtil.query_sql1_3("machine_data_brief");
        List<Object> params = SqlStrUtil.query_list1_3("production_default");
        List<Map<String, Object>> machine_data_brief_mapsList = JdbcTemplateUtil.queryMult(sqlStr, params);

        //通过LinkedHashMap存储 日期：数量 ，保证折线图的有序性。
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
            dataset.addValue(entry.getValue(), "优秀",entry.getKey());
        }
        for (Map.Entry<String, Integer> entry : timeB_map.entrySet()) {
            dataset.addValue(entry.getValue(), "合格",entry.getKey());
        }
        for (Map.Entry<String, Integer> entry : timeC_map.entrySet()) {
            dataset.addValue(entry.getValue(), "隐患",entry.getKey());
        }
        for (Map.Entry<String, Integer> entry : timeD_map.entrySet()) {
            dataset.addValue(entry.getValue(), "次品",entry.getKey());
        }

        return dataset;
    }

}
