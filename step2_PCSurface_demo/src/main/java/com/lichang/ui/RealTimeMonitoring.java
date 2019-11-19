/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;

import com.lichang.DBbeans.Machine_data;
import com.lichang.ui.chart.LineChart;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.TableUtil;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * @author unknown
 */
public class RealTimeMonitoring extends JFrame {

    private static Logger log = LoggerUtil.getLogger(); // 日志

    // 自定义的变量
    private JFreeChart realTimeLineChart;
    private JPanel chartPanel;
    private JPanel chartPanel2;
    private JDialog jDialog;

    public RealTimeMonitoring() {
        initComponents();

        initChartPanel();

        initTable();
        updateTable2();

        setVisible(true);
    }

    /**
     *  折线图： 用于生成 折线图 的 ChartPanel（包括刷新）
     */
    private void initChartPanel() {

        realTimeLineChart = LineChart.getRealTimeLineChart(); // 获得充满数据的chart模型
        if (chartPanel != null) {
            this.getContentPane().remove(chartPanel);
            this.getContentPane().repaint();

            chartPanel = new ChartPanel(realTimeLineChart); // 通过chart创建ChartPanel面板
        } else {
            chartPanel = new ChartPanel(realTimeLineChart); // 通过chart创建ChartPanel面板
        }

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

        chartPanel.setBounds(500, 160, 470, 190);
        getContentPane().add(chartPanel);

        this.invalidate();
        this.validate();
        /*
           添加事件： 点击放大
        */
        chartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                log.debug("点击事件");
                chartPanelMouseClicked(e, realTimeLineChart);
            }
        });
    }

    /**
     * 事件（折线图）：用于点击 折线图后的 放大操作。
     * @param e
     */
    private void chartPanelMouseClicked(MouseEvent e, JFreeChart realTimeLineChart) {
        log.debug("放大操作");
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

    /**
     * 表格1、2：设置表格格式、表头的内容与格式等
     */
    private void initTable() {
        /*
            修改表头1、2
         */
        String[] table1HeaderName = {"故障时间", "编号","故障表现", "最大频次", "判定"};
        DefaultTableModel table1model = (DefaultTableModel) table1.getModel();
        table1model.setColumnIdentifiers(table1HeaderName);
        table1.getTableHeader().setPreferredSize(new Dimension(1, 30)); // 设置表头高度
        table1.getTableHeader().setFont(new Font("", Font.BOLD, 15)); //设置表头字体

        String[] table2HeaderName = {"序号","电压 实时值", "电流 实时值", "送丝速度"};
        DefaultTableModel table2model = (DefaultTableModel) table2.getModel();
        table2model.setColumnIdentifiers(table2HeaderName);
        table2.getTableHeader().setPreferredSize(new Dimension(1, 30)); // 设置表头高度
        table2.getTableHeader().setFont(new Font("", Font.BOLD, 15)); //设置表头字体
    }

    /**
     * 表格1： 添加数据
     */
    private void updateTable1() {
        List<Machine_data> machine_data_BeansList = TableUtil.getDataBeans(); //获取机器数据
        int size = machine_data_BeansList.size(); // 获取一个过程的数据总数
        DefaultTableModel table2Model = (DefaultTableModel)table2.getModel(); //获取当前模型

        /*
            当数据多于模型所能容纳行数后，自动添加行数
         */
        String[] row = {"", "", ""}; // 定义空行 （row）
        while (true) {
            if (table2Model.getRowCount() < size) {
                table2Model.addRow(row);
            } else {
                break;
            }
        }

        // 向模型中添加数据
        for (int i = 0; i < size; i++) {
            int seq = machine_data_BeansList.get(i).getSeq();
            double voltage = machine_data_BeansList.get(i).getVoltage();
            double current = machine_data_BeansList.get(i).getCurrent();
            double speed = machine_data_BeansList.get(i).getSpeed();

            table2Model.setValueAt(seq,i,0);
            table2Model.setValueAt(voltage,i,1);
            table2Model.setValueAt(current,i,2);
            table2Model.setValueAt(speed,i,3);
        }
    }

    /**
     * 表格2： 添加数据
     */
    private void updateTable2() {
        List<Machine_data> machine_data_BeansList = TableUtil.getDataBeans(); //获取机器数据
        int size = machine_data_BeansList.size(); // 获取一个过程的数据总数
        DefaultTableModel table2Model = (DefaultTableModel)table2.getModel(); //获取当前模型

        /*
            当数据多于模型所能容纳行数后，自动添加行数
         */
        String[] row = {"", "", "", ""}; // 定义空行 （row）
        while (true) {
            if (table2Model.getRowCount() < size) {
                table2Model.addRow(row);
            } else {
                break;
            }
        }

        // 向模型中添加数据
        for (int i = 0; i < size; i++) {
            int seq = machine_data_BeansList.get(i).getSeq();
            double voltage = machine_data_BeansList.get(i).getVoltage();
            double current = machine_data_BeansList.get(i).getCurrent();
            double speed = machine_data_BeansList.get(i).getSpeed();

            table2Model.setValueAt(seq,i,0);
            table2Model.setValueAt(voltage,i,1);
            table2Model.setValueAt(current,i,2);
            table2Model.setValueAt(speed,i,3);
        }
    }

    /**
     * 测试： 刷新 折线图
     * @param e
     */
    private void button9ActionPerformed(ActionEvent e) throws NoSuchFieldException {
        initChartPanel();
    }

    /**
     * 测试：表格 添加数据
     * @param e
     */
    private void button10MouseClicked(MouseEvent e) {
        List<Machine_data> machine_data_BeansList = TableUtil.getDataBeans(); //获取机器数据
        int size = machine_data_BeansList.size(); // 获取一个过程的数据总数
        DefaultTableModel table2Model = (DefaultTableModel)table2.getModel(); //获取当前模型

        /*
            当数据多于模型所能容纳行数后，自动添加行数
         */
        String[] row = {"", "", "", ""}; // 定义空行 （row）
        while (true) {
            if (table2Model.getRowCount() < size) {
                table2Model.addRow(row);
            } else {
                break;
            }
        }

        // 向模型中添加数据
        for (int i = 0; i < size; i++) {
            int seq = machine_data_BeansList.get(i).getSeq();
            double voltage = machine_data_BeansList.get(i).getVoltage();
            double current = machine_data_BeansList.get(i).getCurrent();
            double speed = machine_data_BeansList.get(i).getSpeed();

            table2Model.setValueAt(seq,i,0);
            table2Model.setValueAt(voltage,i,1);
            table2Model.setValueAt(current,i,2);
            table2Model.setValueAt(speed,i,3);
        }
    }

    /**
     *  JFormDesigner自带，定义自生成
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel1 = new JPanel();
        label2 = new JLabel();
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menuItem3 = new JMenuItem();
        label3 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        button4 = new JButton();
        separator4 = new JPopupMenu.Separator();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        currentMachineLabel1 = new JLabel();
        currentStatuLabel1 = new JLabel();
        completedNumberLabel1 = new JLabel();
        faultNumberLabel2 = new JLabel();
        label9 = new JLabel();
        button5 = new JButton();
        button6 = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        button7 = new JButton();
        button8 = new JButton();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        button9 = new JButton();
        button10 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("\u754c\u9762");
        setAlwaysOnTop(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel1 ========
        {
            panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder
            ( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax. swing. border
            . TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt
            . Color. red) ,panel1. getBorder( )) ); panel1. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void
            propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( )
            ; }} );
            panel1.setLayout(null);

            //---- label2 ----
            label2.setText("\u5f53\u524d\uff1a");
            panel1.add(label2);
            label2.setBounds(0, 0, 40, 30);

            //======== menuBar1 ========
            {

                //======== menu1 ========
                {
                    menu1.setText("\u7528\u6237\u8bbe\u7f6e");
                    menu1.setMaximumSize(new Dimension(80, 32767));
                    menu1.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 14));
                    menu1.setHorizontalAlignment(SwingConstants.LEFT);

                    //---- menuItem1 ----
                    menuItem1.setText("\u5207\u6362\u7528\u6237");
                    menuItem1.setPreferredSize(new Dimension(74, 25));
                    menuItem1.setHorizontalTextPosition(SwingConstants.LEFT);
                    menuItem1.setMargin(new Insets(2, 0, 2, 0));
                    menu1.add(menuItem1);
                    menu1.addSeparator();

                    //---- menuItem2 ----
                    menuItem2.setText("\u66f4\u6539\u5bc6\u7801");
                    menuItem2.setPreferredSize(new Dimension(74, 25));
                    menuItem2.setHorizontalTextPosition(SwingConstants.LEFT);
                    menuItem2.setMargin(new Insets(2, 0, 2, 0));
                    menu1.add(menuItem2);
                    menu1.addSeparator();

                    //---- menuItem3 ----
                    menuItem3.setText("\u6ce8\u9500\u7528\u6237");
                    menuItem3.setPreferredSize(new Dimension(74, 25));
                    menuItem3.setHorizontalTextPosition(SwingConstants.LEFT);
                    menuItem3.setMargin(new Insets(2, 0, 2, 0));
                    menu1.add(menuItem3);
                }
                menuBar1.add(menu1);
            }
            panel1.add(menuBar1);
            menuBar1.setBounds(135, 0, 86, 30);

            //---- label3 ----
            label3.setText("admin");
            panel1.add(label3);
            label3.setBounds(55, 0, 45, 30);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel1.getComponentCount(); i++) {
                    Rectangle bounds = panel1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel1.setMinimumSize(preferredSize);
                panel1.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel1);
        panel1.setBounds(755, 0, panel1.getPreferredSize().width, 55);

        //---- button1 ----
        button1.setText("\u5b9e\u65f6\u76d1\u6d4b");
        contentPane.add(button1);
        button1.setBounds(55, 60, 120, 30);

        //---- button2 ----
        button2.setText("\u5386\u53f2\u7edf\u8ba1\u4e0e\u67e5\u8be2");
        contentPane.add(button2);
        button2.setBounds(295, 60, 120, 30);

        //---- button3 ----
        button3.setText("\u6545\u969c\u6821\u9a8c");
        contentPane.add(button3);
        button3.setBounds(525, 60, 120, 30);

        //---- button4 ----
        button4.setText("\u7ba1\u7406\u4e0e\u8bbe\u7f6e");
        contentPane.add(button4);
        button4.setBounds(765, 60, 120, 30);
        contentPane.add(separator4);
        separator4.setBounds(5, 90, 920, 10);

        //---- label4 ----
        label4.setText("\u5f53\u524d\u710a\u673a\uff1a");
        contentPane.add(label4);
        label4.setBounds(55, 135, 65, 25);

        //---- label5 ----
        label5.setText("\u5de5\u4f5c\u72b6\u6001\uff1a");
        contentPane.add(label5);
        label5.setBounds(55, 170, 65, 25);

        //---- label6 ----
        label6.setText("\u5df2\u5b8c\u6210\u5de5\u4ef6\uff1a");
        contentPane.add(label6);
        label6.setBounds(55, 205, 75, 25);

        //---- label7 ----
        label7.setText("\u6545\u969c\u5de5\u4ef6\uff1a");
        contentPane.add(label7);
        label7.setBounds(55, 240, 65, 25);

        //---- currentMachineLabel1 ----
        currentMachineLabel1.setText("\u710a\u673a1");
        contentPane.add(currentMachineLabel1);
        currentMachineLabel1.setBounds(135, 135, 60, 25);

        //---- currentStatuLabel1 ----
        currentStatuLabel1.setText("\u6b63\u5e38");
        contentPane.add(currentStatuLabel1);
        currentStatuLabel1.setBounds(135, 170, 60, 25);

        //---- completedNumberLabel1 ----
        completedNumberLabel1.setText("100");
        contentPane.add(completedNumberLabel1);
        completedNumberLabel1.setBounds(135, 205, 60, 25);

        //---- faultNumberLabel2 ----
        faultNumberLabel2.setText("1");
        contentPane.add(faultNumberLabel2);
        faultNumberLabel2.setBounds(135, 240, 60, 25);

        //---- label9 ----
        label9.setText("\u7535\u538b\u3001\u7535\u6d41\u5b9e\u65f6\u6ce2\u5f62");
        contentPane.add(label9);
        label9.setBounds(675, 125, 120, 25);

        //---- button5 ----
        button5.setText("\u6545\u969c\u76d1\u6d4b");
        contentPane.add(button5);
        button5.setBounds(0, 355, 120, 30);

        //---- button6 ----
        button6.setText("\u6e05\u7a7a");
        contentPane.add(button6);
        button6.setBounds(425, 355, 60, 30);

        //======== scrollPane1 ========
        {

            //---- table1 ----
            table1.setModel(new DefaultTableModel(
                new Object[][] {
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                },
                new String[] {
                    null, null, null, null, null
                }
            ));
            table1.setEnabled(false);
            table1.setRowHeight(30);
            table1.setRowMargin(3);
            scrollPane1.setViewportView(table1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 385, 485, 190);

        //---- button7 ----
        button7.setText("\u53c2\u6570\u76d1\u6d4b");
        contentPane.add(button7);
        button7.setBounds(500, 355, 120, 30);

        //---- button8 ----
        button8.setText("\u91cd\u8bbe");
        contentPane.add(button8);
        button8.setBounds(915, 355, 60, 30);

        //======== scrollPane2 ========
        {

            //---- table2 ----
            table2.setModel(new DefaultTableModel(
                new Object[][] {
                    {"", "", "", "", ""},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                },
                new String[] {
                    null, null, null, null, null
                }
            ));
            table2.setRowHeight(30);
            table2.setRowMargin(3);
            table2.setEnabled(false);
            scrollPane2.setViewportView(table2);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(500, 385, 475, 190);

        //---- button9 ----
        button9.setText("text");
        button9.addActionListener(e -> {
            try {
                button9ActionPerformed(e);
            } catch (NoSuchFieldException ex) {
                ex.printStackTrace();
            }
        });
        contentPane.add(button9);
        button9.setBounds(new Rectangle(new Point(360, 220), button9.getPreferredSize()));

        //---- button10 ----
        button10.setText("table");
        button10.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button10MouseClicked(e);
            }
        });
        contentPane.add(button10);
        button10.setBounds(new Rectangle(new Point(365, 260), button10.getPreferredSize()));

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        setSize(990, 625);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panel1;
    private JLabel label2;
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JLabel label3;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JPopupMenu.Separator separator4;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel currentMachineLabel1;
    private JLabel currentStatuLabel1;
    private JLabel completedNumberLabel1;
    private JLabel faultNumberLabel2;
    private JLabel label9;
    private JButton button5;
    private JButton button6;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JButton button7;
    private JButton button8;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton button9;
    private JButton button10;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
