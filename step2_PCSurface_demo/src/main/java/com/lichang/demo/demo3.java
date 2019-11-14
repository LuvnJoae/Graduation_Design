package com.lichang.demo;

import com.lichang.ui.chart.LineChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class demo3 extends JFrame {
    public demo3() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100,100,100,100);

        LineChart lineChart = new LineChart();
        JFreeChart realTimeLineChart = lineChart.RealTimeLineChart();

        JPanel jPanel = new ChartPanel(realTimeLineChart);
        this.add(jPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new demo3();
    }

}
