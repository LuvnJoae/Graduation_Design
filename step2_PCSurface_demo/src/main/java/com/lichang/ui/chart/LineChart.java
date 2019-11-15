package com.lichang.ui.chart;

import com.lichang.DBbeans.Machine_data;
import com.lichang.utils.LineChartUtil;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;
import javafx.scene.control.ComboBox;
import jdk.jfr.StackTrace;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.awt.*;
import java.util.List;

/**
 * 用于生成一个折线图模型
 */
public class LineChart {
    private static Logger log = LoggerUtil.getLogger();

    /**
     * 生成折现模型 chart
     * @return
     */
    public JFreeChart getRealTimeLineChart() {
        log.debug("生成折线图模型");
        CategoryDataset dataset = getDataset("current");
        JFreeChart chart = ChartFactory.createLineChart(
                "Current",
                "Seq",
                "I /A",
                dataset, // 数据集
                PlotOrientation.VERTICAL,
                true,  // 显示图例
                true, // 采用标准生成器
                false // 是否生成超链接
        );
        CategoryPlot plot= (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlinePaint(Color.BLACK);

        return chart;
    }

    /**
     * 将生成折线图 所需数据 添加到dataset中
     * @param para 用于区分电压还是电流
     * @return
     */
    @Test
    private CategoryDataset getDataset(String para) {
        log.debug("生成折线图所需 数据");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Machine_data> machine_data_BeansList = LineChartUtil.getDataBeans();

        log.debug(machine_data_BeansList);

        // 将List中的各行Bean对象中的信息添加到dataset中。
        if (para.equals("current")) {
            for (Machine_data machine_data : machine_data_BeansList) {
                dataset.addValue(machine_data.getCurrent(), "Current", String.valueOf(machine_data.getSeq()));
            }
        } else if (para.equals("voltage")) {
            for (Machine_data machine_data : machine_data_BeansList) {
                dataset.addValue(machine_data.getVoltage(), "Voltage", String.valueOf(machine_data.getSeq()));
            }
        } else {
            log.error("参数输入错误");
            return null;
        }

        return dataset;
    }
}
