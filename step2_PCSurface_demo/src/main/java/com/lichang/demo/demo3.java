package com.lichang.demo;

import com.lichang.utils.RealTimeMonitoringUtil.LineChart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class demo3 extends JFrame {

    private JFreeChart realTimeLineChart;
    private JPanel chartPanel;
    private JDialog jDialog;
    private ChartPanel chartPanel2;
    private JButton button;


    public demo3() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(500, 160, 600, 500);

        button = new JButton("button");
        button.setBounds(100,100,100,50);

        getContentPane().add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test();
            }
        });

        test();

        setVisible(true);
    }

    public void test() {
        realTimeLineChart = LineChart.getRealTimeLineChart(); // 获得充满数据的chart模型
        chartPanel = new ChartPanel(realTimeLineChart); // 通过chart创建ChartPanel面板

        chartPanel.setLayout(null);
        {
            // compute preferred size（窗口大小属性）
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < chartPanel.getComponentCount(); i++) {
                Rectangle bounds = chartPanel.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = chartPanel.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            chartPanel.setMinimumSize(preferredSize);
            chartPanel.setPreferredSize(preferredSize);
        }
            /*
                添加事件： 点击放大
             */
        chartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chartPanelMouseClicked(e, realTimeLineChart);
            }
        });

        getContentPane().add(chartPanel);
        chartPanel.setBounds(500, 160, 470, 190);
    }


    private void chartPanelMouseClicked(MouseEvent e, JFreeChart realTimeLineChart) {
        // 新建 用于展示的JDialog
        jDialog = new JDialog(this, "",true);

        //给这个JDialog中，新建一个ChartPanel。
        chartPanel2 = new ChartPanel(realTimeLineChart);

        //添加并设置相应属性
        jDialog.add(chartPanel2);
        jDialog.setBounds(200,100,800,600);
        jDialog.setAlwaysOnTop(true);
        jDialog.setDefaultCloseOperation(HIDE_ON_CLOSE);
        jDialog.setVisible(true);
    }

    public static void main(String[] args) {
        new demo3();
    }

}
