package com.lichang.utils.RealTimeMonitoringUtil;

import com.lichang.DBbeans.Machine_data_now;
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
import org.junit.Test;

import java.awt.*;
import java.util.List;

/**
 * 用于生成一个折线图模型
 */
public class LineChart {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 生成折线模型 chart
     * @return
     */
    public static JFreeChart getRealTimeLineChart(String title, String categoryAxisLable, String valueAxisLable) {
        log.debug("生成折线图模型");
        CategoryDataset dataset = getDataset("all");
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
    public static JFreeChart getRealTimeLineChart() {
        return getRealTimeLineChart("", "", "");
    }

    /**
     * 将生成折线图 所需数据 添加到dataset中
     * @param para 用于区分电压还是电流
     * @return
     */
    @Test
    private static CategoryDataset getDataset(String para) {
        log.debug("生成折线图所需 数据");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sqlStr = SqlStrUtil.generateSql2("Machine_data_now"); //table 表名
        List<Object> params = SqlStrUtil.generateList2(1); // num为 工件编号
        List<Machine_data_now> machine_data_now_BeansList = (List<Machine_data_now>)
                JdbcTemplateUtil.queryMultForBean(sqlStr, Machine_data_now.class, params);

        log.debug(machine_data_now_BeansList);

        // 将List中的各行Bean对象中的信息添加到dataset中。
        if (para.equals("current")) {
            for (Machine_data_now machine_dataSingle : machine_data_now_BeansList) {
                dataset.addValue(machine_dataSingle.getCurrent(), "Current", String.valueOf(machine_dataSingle.getSeq()));
            }
        } else if (para.equals("voltage")) {
            for (Machine_data_now machine_dataSingle : machine_data_now_BeansList) {
                dataset.addValue(machine_dataSingle.getVoltage(), "Voltage", String.valueOf(machine_dataSingle.getSeq()));
            }
        } else if (para.equals("all")) {
            for (Machine_data_now machine_dataSingle : machine_data_now_BeansList) {
                dataset.addValue(machine_dataSingle.getCurrent(), "Current", String.valueOf(machine_dataSingle.getSeq()));
                dataset.addValue(machine_dataSingle.getVoltage(), "Voltage", String.valueOf(machine_dataSingle.getSeq()));
            }
        }
        else {
            log.error("参数输入错误");
            return null;
        }

        return dataset;
    }
}
