package com.lichang.utils.RealTimeMonitoringUtils;

import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SqlStrUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * 用于生成一个折线图模型
 */
public class LineChartUtil_new {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 生成折线模型 chart
     */
    public static JFreeChart getLineChart(String production_name,
                                          int production_num,
                                          String title,
                                          String categoryAxisLable,
                                          String valueAxisLable) {
        List<Map<String, Object>> mapsList = getData(production_name, production_num); //获取数据list
        CategoryDataset dataset = getDataset(mapsList); //获取dataset
        JFreeChart chart = LineChart_main(dataset, title, categoryAxisLable, valueAxisLable);//获得Chart模型
        return chart;
    }

    //无参 重载
    public static JFreeChart getLineChart(String production_name, int production_num) {
        return getLineChart(production_name, production_num, "", "", "");
    }

    //无参 重载
    public static JFreeChart getLineChart(String production_name) {
        return getLineChart(production_name, -1, "", "", "");
    }

    /**
     * 生成折线图 模型 主方法
     */
    private static JFreeChart LineChart_main(CategoryDataset dataset, String title, String categoryAxisLable, String valueAxisLable) {
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

        CategoryPlot plot = (CategoryPlot) chart.getPlot(); // 获取chart绘图对象plot

        plot.setBackgroundPaint(Color.white);  // 背景颜色
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);  // 背景网格虚线
        plot.setOutlinePaint(Color.BLACK); // 轮廓颜色
        plot.setNoDataMessage("数据加载失败，检查是否已选择产品");  // 错误提示

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer(); // 获取渲染器
        renderer.setBaseShapesVisible(true); // 显示数据点

        //设置标识字体
        Font legendFont = new Font("", Font.PLAIN, 16);
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(legendFont);

        return chart;
    }

    /**
     * 生成 chart所用的 dataset
     */
    private static CategoryDataset getDataset(List<Map<String, Object>> mapsList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // 将List中的信息添加到dataset中。
        for (Map<String, Object> map : mapsList) {
            dataset.addValue((double) map.get("current"), "Current", String.valueOf(map.get("seq")));
            dataset.addValue((double) map.get("voltage"), "Voltage", String.valueOf(map.get("seq")));
        }

        return dataset;
    }

    /**
     * 获得 指定表 的List( machine_data_now 或 machine_data_all)
     */
    private static List<Map<String, Object>> getData(String production_name, int production_num) {
        List<Map<String, Object>> mapsList;

        //如果production_num == -1 ,则认为是取machine_data_now里的数据（这个表不需要num）
        if (production_num == -1) {
            String sqlStr = SqlStrUtil.query_sql1_3("machine_data_now"); //table 表名
            List<Object> params = SqlStrUtil.query_list1_3(production_name);
            List<Map<String, Object>> machine_data_now_mapsList = JdbcTemplateUtil.queryMult(sqlStr, params);

            mapsList = machine_data_now_mapsList;
        } else {
            //如果production_num 不为-1 ,则认为是取machine_data_all里的数据（需要name和num共同选择）
            String sqlStr = SqlStrUtil.query_sql1_5("machine_data_all");
            List<Object> params = SqlStrUtil.query_list1_5(production_name, production_num);
            List<Map<String, Object>> machine_data_all_mapsList = JdbcTemplateUtil.queryMult(sqlStr, params);

            mapsList = machine_data_all_mapsList;
        }
        return mapsList;
    }
}
