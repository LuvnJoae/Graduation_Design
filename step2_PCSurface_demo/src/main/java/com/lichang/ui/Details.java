/*
 * Created by JFormDesigner on Sat Dec 21 12:43:06 CST 2019
 */

package com.lichang.ui;

import java.awt.event.*;
import com.lichang.utils.DetailsUtil;
import com.lichang.utils.RealTimeMonitoringUtils.LineChartUtil_new;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.*;

//TODO: 内容
//标记时间：2019/12/21 15:12  预解决时间：
//1. 参数 折线图
//2. 参数具体表
//3. 故障信息表

/**
 * 用于展示 故障 详细信息
 */
public class Details extends JDialog {
    /**
     * 自定义变量
     */
    private String time; //故障表中的time，作为主键，读取信息
    private String production_name; //产品名称
    private int production_num; //工件编号
    private Map<String, Object> machine_fault_data_map; //故障表中根据time查询到的单条信息map。
    private List<Map<String, Object>> machine_data_all_mapsList; //参数数据详细表
    //折线图相关
    private JFreeChart lineChart; // 折线图模型
    private JPanel chartPanel; //折线图Panel


    public Details(Window owner) {
        super(owner);
        initComponents();

        setVisible(true);
    }

    public Details(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initComponents();

        setVisible(true);
    }

    public Details(Frame owner, String title, boolean modal, String time) {
        super(owner, title, modal);
        initComponents();

        this.time = time; //赋值production_name
        setVisible(true);
    }

    /**
     *  页面监听
     */
    //第一次打开该页面时
    private void thisWindowOpened(WindowEvent e) {
        updateTable1(); //加载故障信息表
        updateTable2(); //加载参数信息表
        updateChartPanel(); //加载折线图
    }

    /**
     * 故障信息表 table1
     */
    //设置表格格式
    private void initTable1Form() {
        //设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        table1.setDefaultRenderer(Object.class, r);
    }

    //table1 主方法：加载table1 数据，  添加一行
    private void updateTable1() {
        //非空判断
        if (time == null) {
            return;
        }else {
            System.out.println(time);
        }

        //获得 time 相应故障记录
        machine_fault_data_map = DetailsUtil.getFaultRecordForTime(time);

        //非空处理
        if (machine_fault_data_map == null) {
            return;
        }

        DefaultTableModel table1Model = (DefaultTableModel) table1.getModel(); //获取model
        //加载行 内容
        Object[] newRowData = {
                machine_fault_data_map.get("id"),
                machine_fault_data_map.get("time").toString().split("\\.")[0], //去除timestamp后面的 .0
                machine_fault_data_map.get("production_name"),
                machine_fault_data_map.get("production_num"),
                machine_fault_data_map.get("fault_type"),
                machine_fault_data_map.get("fault_maxnum"),
                machine_fault_data_map.get("result")
        };

        //设置格式
        initTable1Form();

        table1Model.addRow(newRowData); //添加行
    }

    /**
     * 参数信息表 table2
     */
    //设置表格格式
    private void initTable2Form() {
        //设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        table2.getColumnModel().getColumn(0).setCellRenderer(r); //设置仅第一列居中
//        table2.setDefaultRenderer(Object.class, r);
    }

    //table2 主方法 ： 加载数据 + 更新
    private void updateTable2() {
        //非空处理
        if (machine_fault_data_map == null) {
            return;
        }

        //获取所选产品名称 与 工件编号
        production_name = (String) machine_fault_data_map.get("production_name");
        production_num = (int) machine_fault_data_map.get("production_num");

        //在machine_data_all表中查询，获得详细参数信息
        machine_data_all_mapsList = DetailsUtil.getDetailData(production_name, production_num);

        //非空处理
        if (machine_data_all_mapsList == null || machine_data_all_mapsList.size() == 0) {
            return;
        }

        DefaultTableModel table2Model = (DefaultTableModel) table2.getModel(); //获取model
        table2Model.setRowCount(0); //先清空，再加载新数据

        for (Map<String, Object> map : machine_data_all_mapsList) {
            //加载行 内容
            Object[] newRowData = {
                    map.get("seq"),
                    map.get("voltage"),
                    map.get("current"),
                    map.get("speed")
            };

            table2Model.addRow(newRowData); //添加行
        }
    }

    //阈值 提示（当超出设置阈值后，数据自动变色）
    private void addLimit() {

    }

    /**
     * 折线图
     */
    private void updateChartPanel() {
        if (production_name == null) {
            return;
        }

        //获取所选产品名称
        lineChart = LineChartUtil_new.getLineChart(production_name, production_num); // 获得充满数据的chart模型

        //若chartPanel已存在，则删去重新创建
        if (chartPanel != null) {
            panel1.remove(chartPanel);
        }
        chartPanel = new ChartPanel(lineChart);

        chartPanel.setLayout(null);
        chartPanel.setBounds(0, 0, 465, 410);

        panel1.add(chartPanel);
        panel1.repaint();
    }
    /**
     * 测试
     * @param e
     */
    private void button1ActionPerformed(ActionEvent e) {
//        updateTable1();
//        updateTable2();
//        updateChartPanel();
    }







    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel1 = new JPanel();
        label1 = new JLabel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        label2 = new JLabel();
        button1 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setFont(this.getFont().deriveFont(this.getFont().getSize() + 1f));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                thisWindowOpened(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== scrollPane1 ========
        {

            //---- table1 ----
            table1.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                    "id", "\u6545\u969c\u65f6\u95f4", "\u4ea7\u54c1\u540d\u79f0", "\u5de5\u4ef6\u7f16\u53f7", "\u6545\u969c\u7c7b\u578b", "\u6700\u5927\u9891\u6b21", "\u5224\u5b9a"
                }
            ) {
                boolean[] columnEditable = new boolean[] {
                    false, false, false, false, false, false, false
                };
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });
            table1.setRowHeight(25);
            scrollPane1.setViewportView(table1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 0, 985, 48);

        //======== panel1 ========
        {
            panel1.setBackground(new Color(153, 153, 153));
            panel1.setLayout(null);

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
        panel1.setBounds(0, 85, 465, 410);

        //---- label1 ----
        label1.setText("\u7535\u538b\u3001\u7535\u6d41 \u6298\u7ebf\u56fe");
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 3f));
        contentPane.add(label1);
        label1.setBounds(130, 60, 155, label1.getPreferredSize().height);

        //======== scrollPane2 ========
        {

            //---- table2 ----
            table2.setModel(new DefaultTableModel(
                new Object[][] {
                    {null, null, null, null},
                },
                new String[] {
                    "\u5e8f\u53f7", "\u7535\u5f27\u7535\u538b", "\u7535\u6d41", "\u710a\u63a5\u901f\u5ea6"
                }
            ) {
                boolean[] columnEditable = new boolean[] {
                    false, false, false, false
                };
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });
            {
                TableColumnModel cm = table2.getColumnModel();
                cm.getColumn(0).setPreferredWidth(60);
                cm.getColumn(1).setPreferredWidth(120);
                cm.getColumn(2).setPreferredWidth(120);
                cm.getColumn(3).setPreferredWidth(120);
            }
            table2.setRowHeight(20);
            scrollPane2.setViewportView(table2);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(470, 85, 515, 410);

        //---- label2 ----
        label2.setText("\u5177\u4f53\u53c2\u6570\u4fe1\u606f");
        label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 3f));
        contentPane.add(label2);
        label2.setBounds(610, 60, 105, 22);

        //---- button1 ----
        button1.setText("test");
        button1.addActionListener(e -> button1ActionPerformed(e));
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(5, 55), button1.getPreferredSize()));

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
        setSize(1000, 535);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel1;
    private JLabel label1;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JLabel label2;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
