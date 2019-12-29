/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.jgoodies.forms.factories.*;

import com.lichang.utils.ChangePasswordUtil;
import com.lichang.utils.LimitRuleUtil;
import com.lichang.utils.RealTimeMonitoringUtils.*;
import com.lichang.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;


/**
 * @author unknown
 */
public class RealTimeMonitoring extends JFrame {
    private static Logger log = LoggerUtil.getLogger(); // 日志

    /**
     * 自定义变量
     */
    //用户设置
    private String username; // 当前用户名
    private boolean adminFlag; // 用户类型
    private JDialog jDialog2; // 密码修改
    private JPanel changePasswordPanel; // 密码修改
    private JLabel oldValidationTip; // 旧密码 验证提示
    private boolean oldChangeFlag; //判断旧密码是否通过验证

    //表格相关
    private List<Map<String, Object>> machine_fault_data_mapsList; //故障表
    private List<Map<String, Object>> machine_data_now_mapsList; //当前工件数据表
    private List<Map<String, Object>> expert_production_mapsList; //产品表
    private int machine_fault_data_lastRecordNum = 0; //故障表上一个最后一条记录的num
    private String machine_fault_data_timeCol; //故障信息的time列，用于作主键查看详情


    //折线图相关
    private JFreeChart lineChart; // 折线图模型
    private JPanel chartPanel; //折线图Panel

    //Label相关
    private String prodution_name;

    //无参（预设账户信息）
    public RealTimeMonitoring() {
        username = "admin";
        adminFlag = true;
        initComponents();
        this.setBounds(273, 95, 990, 625);
        setVisible(true);
    }

    //有参（接收登录账户信息）
    public RealTimeMonitoring(String username, Boolean adminFlag) {
        this.username = username;
        this.adminFlag = adminFlag;
        initComponents();

        label3Bind(username); //显示当前用户信息

        this.setBounds(273, 95, 990, 625);
        setVisible(true);
    }

    /**
     * 界面风格
     */
    private void setUI() {
        this.getContentPane().setBackground(new Color(238,238,238)); //整体背景
    }

    /**
     * 页面监听
     */
    //第一次打开该页面时
    private void thisWindowOpened(WindowEvent e) {
        setUI();
        updateComboBox1(); //先更新一次下拉框
        scheduledExecutor(); //开启定时器
        //设置表格格式
        initTable1Form();
        initTable2Form();
    }

    /**
     * Lable3 账户信息: 显示当前登录用户
     */
    private void label3Bind(String username) {
        label3.setText(username);
    }

    /**
     * Menu 菜单
     */
    //MenuItem 用户设置:  切换用户
    private void menuItem1ActionPerformed(ActionEvent e) {
        new Login();
        this.dispose();
    }

    //MenuItem 用户设置： 更改密码
    private void menuItem2ActionPerformed(ActionEvent e) {
        if (!adminFlag) {
            JOptionPane.showMessageDialog(this, "您没有该权限！请用管理员身份登录！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        jDialog2 = new JDialog(this, "", true);
        changePasswordPanel = new JPanel();
        oldValidationTip = new JLabel();
        oldChangeFlag = false;

        changePasswordPanel.setLayout(null);

        //提示
        JTextArea tip = new JTextArea("提示：密码5~10个字符，可使用字母、数字、下划线");
        changePasswordPanel.add(tip);
        tip.setBounds(50, 20, 300, 40);
        tip.setLineWrap(true); // 自动换行
        tip.setWrapStyleWord(true);
        tip.setEditable(false); // 不可编辑
        tip.setBackground(new Color(240, 240, 240)); // 背景色统一


        //旧密码提示
        JLabel oldPasswordTip = new JLabel("请输入旧密码: ");
        changePasswordPanel.add(oldPasswordTip);
        oldPasswordTip.setBounds(40, 60, 110, 30);
        oldPasswordTip.setFont(new Font("", Font.BOLD, 15));


        //新密码提示
        JLabel newPasswordTip = new JLabel("请输入新密码: ");
        changePasswordPanel.add(newPasswordTip);
        newPasswordTip.setBounds(40, 120, 110, 30);
        newPasswordTip.setFont(new Font("", Font.BOLD, 15));


        //旧密码
        JTextField oldPasswordField = new JTextField();
        changePasswordPanel.add(oldPasswordField);
        oldPasswordField.setBounds(160, 60, 90, 30);
        oldPasswordField.setColumns(10);
        oldPasswordField.setFont(new Font("黑体", Font.PLAIN, 15));

        //焦点监听：旧密码验证
        oldPasswordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {

                String password = oldPasswordField.getText();
                String table;

                if (adminFlag == true) {
                    table = "admin";
                } else {
                    table = "emp";
                }

                if (ChangePasswordUtil.validate(table, username, password)) {
                    oldValidationTip.setText("验证成功");
                    oldValidationTip.setForeground(Color.red);
                    oldChangeFlag = true;
                } else {
                    oldValidationTip.setText("验证失败");
                    oldValidationTip.setForeground(Color.red);
                    oldChangeFlag = false;
                }

                changePasswordPanel.add(oldValidationTip);
                oldValidationTip.setBounds(260, 60, 60, 30);
            }
        });

        //新密码
        JTextField newPasswordField = new JTextField();
        changePasswordPanel.add(newPasswordField);
        newPasswordField.setBounds(160, 120, 90, 30);
        newPasswordField.setColumns(10);
        newPasswordField.setFont(new Font("黑体", Font.PLAIN, 15));

        newPasswordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = newPasswordField.getText();

                if (!Pattern.matches("[a-zA-Z0-9_]{2,10}$", newPassword)) {
                    JOptionPane.showMessageDialog(jDialog2, "新密码格式错误，请重新输入", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (oldChangeFlag) {
                        String password = newPasswordField.getText();
                        String table;

                        if (adminFlag == true) {
                            table = "admin";
                        } else {
                            table = "emp";
                        }

                        ChangePasswordUtil.newPassword(table, username, password);
                        JOptionPane.showMessageDialog(jDialog2, "新密码格式正确，修改成功！", "提示", JOptionPane.WARNING_MESSAGE);
                        jDialog2.dispose();
                    } else {
                        JOptionPane.showMessageDialog(jDialog2, "请先验证旧密码！", "提示", JOptionPane.WARNING_MESSAGE);
                    }

                }
            }
        });

        jDialog2.setSize(400, 250);
        jDialog2.setAlwaysOnTop(true);
        jDialog2.setLocationRelativeTo(null);
        jDialog2.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jDialog2.add(changePasswordPanel);
        jDialog2.setVisible(true);
    }

    /**
     * 界面跳转 按钮
     */
    //历史统计与查询 按钮： 点击跳转
    private void button2ActionPerformed(ActionEvent e) {
        new HistoricalStatistics(username, adminFlag);
    }

    //专家系统 按钮： 点击跳转
    private void button3ActionPerformed(ActionEvent e) {
        new ExpertSystem(username, adminFlag);
    }

    //管理与设置 按钮： 点击跳转
    private void button4ActionPerformed(ActionEvent e) {
        new Setting(username, adminFlag);
    }

    /**
     * 定时器 定时刷新数据（表、Label、图）
     */
    private void scheduledExecutor() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateTable1(); //刷新表格1
                updateTable2(); //刷新表格2

                updateChartPanel(); //刷新折线图

                updateMachine(); //更新 当前焊机、状态
                updateLabel13(); //更新 完成工件
                updateLabel14(); //更新 故障工件
                updateLabel15(); //更新 产品编号
                updateLabel16(); //更新 检测结果

            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 1, 5, TimeUnit.SECONDS);
    }

    /**
     * 故障监测表 table1
     */
    //设置表格格式
    private void initTable1Form() {
        //设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        table1.setDefaultRenderer(Object.class, r);
        //设置表头居中
        ((DefaultTableCellRenderer)table1.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

    }

    //加载table1 数据，  添加一行
    private void addTable1Data() {
        initTable1Form(); //设置表格格式
        //获取所选产品名称
        prodution_name = (String) comboBox1.getSelectedItem();
        //获得最后一条记录
        Map<String, Object> machine_fault_data_map = TableUtil_new.getLastRecord("machine_fault_data", prodution_name);

        //非空处理
        if (machine_fault_data_map == null) {
            return;
        }

        DefaultTableModel table1Model = (DefaultTableModel) table1.getModel(); //获取model
        //加载行 内容
        Object[] newRowData = {
                machine_fault_data_map.get("time").toString().split("\\.")[0], //去除timestamp后面的 .0
                machine_fault_data_map.get("production_name"),
                machine_fault_data_map.get("production_num"),
                machine_fault_data_map.get("fault_type"),
                machine_fault_data_map.get("fault_maxnum"),
                machine_fault_data_map.get("result"),
                "<html><font color = 'blue'><u>查看</u></font></html>"
        };
        table1Model.addRow(newRowData); //添加行

    }

    //table1 主方法： 更新  条件：若有新的故障数据，则添加
    private void updateTable1() {
        //获取所选产品名称
        prodution_name = (String) comboBox1.getSelectedItem();
        //获得最后一条记录
        Map<String, Object> machine_fault_data_map = TableUtil_new.getLastRecord("machine_fault_data", prodution_name);
        //非空处理
        if (machine_fault_data_map == null) {
            return;
        }

        int machine_fault_data_nowRecordNum = (int) machine_fault_data_map.get("production_num");
        //判断最后一条数据是否已经更新
        if (machine_fault_data_nowRecordNum != machine_fault_data_lastRecordNum) {
            //已更新，则添加新数据，并更新recordNum的值
            machine_fault_data_lastRecordNum = machine_fault_data_nowRecordNum;
            addTable1Data();
        } else {
            return;
        }
    }

    //按钮： 清空故障表内容
    private void button6ActionPerformed(ActionEvent e) {
        DefaultTableModel table1Model = (DefaultTableModel) table1.getModel();
        table1Model.setRowCount(0);
    }

    //事件： 鼠标点击 table1中查看的一列 时
    private void table1MouseClicked(MouseEvent e) {
        //若点击的为 查看 这列，则进入事件，创建新窗口，展示详情
        if (table1.getSelectedColumn() == 6) {
            int selectedRow = table1.getSelectedRow();
            machine_fault_data_timeCol = table1.getValueAt(selectedRow, 0).toString() + ".0"; //和timestamp格式相同
            new DetailsFault(this, "故障信息详情", true, machine_fault_data_timeCol); //创建新窗口
        } else {
            return;
        }
    }

    /**
     * 参数监测表 table2
     */
    //设置表格格式
    private void initTable2Form() {
        //设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        table2.getColumnModel().getColumn(0).setCellRenderer(r); //设置仅第一列居中
        //设置表头居中
        ((DefaultTableCellRenderer)table2.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    //table2 主方法 ： 加载数据 + 更新
    private void updateTable2() {
        initTable2Form(); //设置表格格式

        //获取所选产品名称
        prodution_name = (String) comboBox1.getSelectedItem();
        //获得最后一条记录
        machine_data_now_mapsList = TableUtil_new.getData("machine_data_now", prodution_name);

        //非空处理
        if (machine_data_now_mapsList == null || machine_data_now_mapsList.size() == 0) {
            return;
        }

        DefaultTableModel table2Model = (DefaultTableModel) table2.getModel(); //获取model
        table2Model.setRowCount(0); //先清空，再加载新数据

        for (Map<String, Object> map : machine_data_now_mapsList) {
            //加载行 内容
            Object[] newRowData = {
                    map.get("seq"),
                    map.get("current"),
                    map.get("voltage"),
                    map.get("speed")
            };

            table2Model.addRow(newRowData); //添加行
        }

        addValueLimit(); //添加value_limit rule
    }

    //阈值 （当超出设置阈值后，数据自动变色）
    private void addValueLimit() {
        LimitRuleUtil.addValueLimit(expert_production_mapsList, prodution_name, table2); //进入limitRule主方法
    }

    /**
     * 参数 折线图
     */
    private void updateChartPanel() {
        //获取所选产品名称
        prodution_name = (String) comboBox1.getSelectedItem();

        lineChart = LineChartUtil_new.getLineChart(prodution_name); // 获得充满数据的chart模型

        //若chartPanel已存在，则删去重新创建
        if (chartPanel != null) {
            panel4.remove(chartPanel);
        }
        chartPanel = new ChartPanel(lineChart);

        chartPanel.setLayout(null);
        chartPanel.setBounds(0, 0, 460, 195);

        panel4.add(chartPanel);
        panel4.repaint();
    }

    /**
     * Label 数据绑定与更新
     */
    //更新 当前焊机、状态
    private void updateMachine() {
        Map<String, Object> machine_map = LabelUpdateTextUtil_new.getMachine();
        if (machine_map == null) {
            label11.setText("-1");
            label12.setText("-1");
        } else {
            String machine_name = (String) machine_map.get("machine_name");
            String machine_status = (String) machine_map.get("machine_status");
            label11.setText(machine_name);
            label12.setText(machine_status);
        }

    }

    //更新 完成工件
    private void updateLabel13() {
        //获取所选产品名称
        prodution_name = (String) comboBox1.getSelectedItem();

        Long completedCount = LabelUpdateTextUtil_new.getCompletedCount(prodution_name); //获取已完成工件数
        label13.setText(String.valueOf(completedCount));
    }

    //更新 故障工件
    private void updateLabel14() {
        //获取所选产品名称
        prodution_name = (String) comboBox1.getSelectedItem();

        Long faultCount = LabelUpdateTextUtil_new.getFaultCount(prodution_name); //获取已完成工件数
        label14.setText(String.valueOf(faultCount));
    }

    //更新 工件编号
    private void updateLabel15() {
        //获取所选产品名称
        prodution_name = (String) comboBox1.getSelectedItem();

        int prodution_num = LabelUpdateTextUtil_new.getProdutionNum(prodution_name); //获取已完成工件数
        label15.setText(String.valueOf(prodution_num));
    }

    //更新 检测结果
    private void updateLabel16() {
        //获取所选产品名称
        prodution_name = (String) comboBox1.getSelectedItem();

        String result = LabelUpdateTextUtil_new.getResult(prodution_name); //获取已完成工件数
        label16.setText(result);
    }

    /**
     * 产品选择 下拉框
     */
    //刷新 产品下拉框内容
    private void updateComboBox1() {
        comboBox1.removeAllItems(); //清空原数据
        expert_production_mapsList = TableUtil_new.getData("expert_production"); //获取产品表内容，更新内容
        if (expert_production_mapsList == null || expert_production_mapsList.size() == 0) {
            return;
        }
        //更新 产品选择 下拉框内容
        for (Map<String, Object> map : expert_production_mapsList) {
            comboBox1.addItem((String) map.get("name")); //下拉框添加内容
        }
        comboBox1.setSelectedIndex(-1);
    }

    //点击事件 刷新条件：当点击 产品选择下拉框时， 刷新该下拉框内容
    private void comboBox1MouseClicked(MouseEvent e) {
        updateComboBox1();
    }

    /**
     * 其他 按钮
     */
    //手动刷新 按钮
    private void button5ActionPerformed(ActionEvent e) {
        expert_production_mapsList = TableUtil_new.getData("expert_production"); //刷新产品表
        updateTable1(); //刷新表格1
        updateTable2(); //刷新表格2

        updateChartPanel(); //刷新折线图

        updateLabel13(); //更新 完成工件
        updateLabel14(); //更新 故障工件
        updateLabel15(); //更新 产品编号
        updateLabel16(); //更新 检测结果
    }

    /**
     * 测试 按钮
     */
    //测试1： 添加故障记录
    private void button7ActionPerformed(ActionEvent e) {
        addTable1Data();
    }


    /**
     * JFormDesigner自带，定义自生成
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        label2 = new JLabel();
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        label3 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        button4 = new JButton();
        separator4 = new JPopupMenu.Separator();
        tabbedPane1 = new JTabbedPane();
        panel2 = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        tabbedPane2 = new JTabbedPane();
        panel3 = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        button5 = new JButton();
        label1 = new JLabel();
        comboBox1 = new JComboBox<>();
        button6 = new JButton();
        panel4 = new JPanel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        label11 = new JLabel();
        label12 = new JLabel();
        label13 = new JLabel();
        label14 = new JLabel();
        label15 = new JLabel();
        label16 = new JLabel();
        button7 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("\u754c\u9762");
        setAlwaysOnTop(true);
        setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        setBackground(new Color(238, 238, 238));
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                thisWindowOpened(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel1 ========
        {
            panel1.setBackground(new Color(238, 238, 238));
            panel1.setLayout(null);

            //---- label2 ----
            label2.setText("\u5f53\u524d\uff1a");
            label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 2f));
            panel1.add(label2);
            label2.setBounds(5, 0, 50, 30);

            //======== menuBar1 ========
            {
                menuBar1.setBorder(null);

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
                    menuItem1.addActionListener(e -> menuItem1ActionPerformed(e));
                    menu1.add(menuItem1);
                    menu1.addSeparator();

                    //---- menuItem2 ----
                    menuItem2.setText("\u66f4\u6539\u5bc6\u7801");
                    menuItem2.setPreferredSize(new Dimension(74, 25));
                    menuItem2.setHorizontalTextPosition(SwingConstants.LEFT);
                    menuItem2.setMargin(new Insets(2, 0, 2, 0));
                    menuItem2.addActionListener(e -> menuItem2ActionPerformed(e));
                    menu1.add(menuItem2);
                    menu1.addSeparator();
                }
                menuBar1.add(menu1);
            }
            panel1.add(menuBar1);
            menuBar1.setBounds(135, 0, 86, 30);

            //---- label3 ----
            label3.setText("admin");
            label3.setFont(label3.getFont().deriveFont(label3.getFont().getSize() + 2f));
            panel1.add(label3);
            label3.setBounds(55, 0, 80, 30);

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
        panel1.setBounds(740, 0, 236, 50);

        //---- button1 ----
        button1.setText("\u5b9e\u65f6\u76d1\u6d4b");
        button1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 14));
        button1.setForeground(new Color(51, 51, 51));
        contentPane.add(button1);
        button1.setBounds(55, 50, 135, 40);

        //---- button2 ----
        button2.setText("\u5386\u53f2\u7edf\u8ba1\u4e0e\u67e5\u8be2");
        button2.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 14));
        button2.setForeground(new Color(51, 51, 51));
        button2.addActionListener(e -> button2ActionPerformed(e));
        contentPane.add(button2);
        button2.setBounds(295, 50, 135, 40);

        //---- button3 ----
        button3.setText("\u4e13\u5bb6\u7cfb\u7edf");
        button3.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 14));
        button3.setForeground(new Color(51, 51, 51));
        button3.addActionListener(e -> button3ActionPerformed(e));
        contentPane.add(button3);
        button3.setBounds(540, 50, 135, 40);

        //---- button4 ----
        button4.setText("\u7ba1\u7406\u4e0e\u8bbe\u7f6e");
        button4.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 14));
        button4.setForeground(new Color(51, 51, 51));
        button4.addActionListener(e -> button4ActionPerformed(e));
        contentPane.add(button4);
        button4.setBounds(795, 50, 135, 40);
        contentPane.add(separator4);
        separator4.setBounds(5, 90, 965, 10);

        //======== tabbedPane1 ========
        {
            tabbedPane1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 15));
            tabbedPane1.setForeground(new Color(204, 204, 0));
            tabbedPane1.setBackground(Color.white);

            //======== panel2 ========
            {
                panel2.setLayout(null);

                //======== scrollPane1 ========
                {

                    //---- table1 ----
                    table1.setModel(new DefaultTableModel(
                        new Object[][] {
                        },
                        new String[] {
                            "\u6545\u969c\u65f6\u95f4", "\u4ea7\u54c1\u540d\u79f0", "\u5de5\u4ef6\u7f16\u53f7", "\u6545\u969c\u7c7b\u578b", "\u6700\u5927\u9891\u6b21", "\u5224\u5b9a", "\u8be6\u60c5"
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
                    {
                        TableColumnModel cm = table1.getColumnModel();
                        cm.getColumn(0).setMinWidth(110);
                        cm.getColumn(1).setMinWidth(110);
                        cm.getColumn(2).setMinWidth(55);
                        cm.getColumn(3).setMinWidth(55);
                        cm.getColumn(4).setMinWidth(55);
                        cm.getColumn(5).setMinWidth(30);
                        cm.getColumn(6).setMinWidth(30);
                    }
                    table1.setRowHeight(20);
                    table1.setAutoCreateRowSorter(true);
                    table1.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            table1MouseClicked(e);
                        }
                    });
                    scrollPane1.setViewportView(table1);
                }
                panel2.add(scrollPane1);
                scrollPane1.setBounds(0, 0, 485, 260);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panel2.getComponentCount(); i++) {
                        Rectangle bounds = panel2.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel2.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel2.setMinimumSize(preferredSize);
                    panel2.setPreferredSize(preferredSize);
                }
            }
            tabbedPane1.addTab("\u6545\u969c\u76d1\u6d4b", panel2);
        }
        contentPane.add(tabbedPane1);
        tabbedPane1.setBounds(0, 300, 485, 290);

        //======== tabbedPane2 ========
        {
            tabbedPane2.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 15));
            tabbedPane2.setForeground(new Color(51, 51, 51));

            //======== panel3 ========
            {
                panel3.setLayout(null);

                //======== scrollPane2 ========
                {

                    //---- table2 ----
                    table2.setRowHeight(20);
                    table2.setModel(new DefaultTableModel(
                        new Object[][] {
                        },
                        new String[] {
                            "\u5e8f\u53f7", "\u7535\u6d41", "\u7535\u5f27\u7535\u538b", "\u710a\u63a5\u901f\u5ea6"
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
                        cm.getColumn(0).setPreferredWidth(80);
                        cm.getColumn(1).setPreferredWidth(130);
                        cm.getColumn(2).setPreferredWidth(130);
                        cm.getColumn(3).setPreferredWidth(130);
                    }
                    table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    scrollPane2.setViewportView(table2);
                }
                panel3.add(scrollPane2);
                scrollPane2.setBounds(0, 0, 490, 260);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panel3.getComponentCount(); i++) {
                        Rectangle bounds = panel3.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel3.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel3.setMinimumSize(preferredSize);
                    panel3.setPreferredSize(preferredSize);
                }
            }
            tabbedPane2.addTab("\u53c2\u6570\u76d1\u6d4b", panel3);
        }
        contentPane.add(tabbedPane2);
        tabbedPane2.setBounds(490, 300, 485, 290);

        //---- button5 ----
        button5.setText("\u624b\u52a8\u5237\u65b0");
        button5.setFont(button5.getFont().deriveFont(button5.getFont().getSize() + 1f));
        button5.addActionListener(e -> button5ActionPerformed(e));
        contentPane.add(button5);
        button5.setBounds(270, 270, 85, 35);

        //---- label1 ----
        label1.setText("\u4ea7\u54c1\u9009\u62e9\uff1a");
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 3f));
        contentPane.add(label1);
        label1.setBounds(220, 110, 80, label1.getPreferredSize().height);

        //---- comboBox1 ----
        comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
            "production_default"
        }));
        comboBox1.setSelectedIndex(-1);
        comboBox1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                comboBox1MouseClicked(e);
            }
        });
        contentPane.add(comboBox1);
        comboBox1.setBounds(305, 105, 150, 30);

        //---- button6 ----
        button6.setText("\u6e05\u7a7a\u6545\u969c\u8bb0\u5f55");
        button6.setFont(button6.getFont().deriveFont(button6.getFont().getSize() + 1f));
        button6.addActionListener(e -> button6ActionPerformed(e));
        contentPane.add(button6);
        button6.setBounds(360, 270, 105, 35);

        //======== panel4 ========
        {
            panel4.setBackground(new Color(153, 153, 153));
            panel4.setLayout(null);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel4.getComponentCount(); i++) {
                    Rectangle bounds = panel4.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel4.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel4.setMinimumSize(preferredSize);
                panel4.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel4);
        panel4.setBounds(510, 100, 460, 195);

        //---- label4 ----
        label4.setText("<html>\u7535<br>\u538b<br>\u7535<br>\u6d41<br>\u5b9e<br>\u65f6<br>\u56fe</html>");
        label4.setFont(label4.getFont().deriveFont(label4.getFont().getSize() + 2f));
        contentPane.add(label4);
        label4.setBounds(485, 130, 20, label4.getPreferredSize().height);

        //---- label5 ----
        label5.setText("\u5f53\u524d\u710a\u673a\uff1a");
        label5.setFont(label5.getFont().deriveFont(label5.getFont().getSize() + 3f));
        contentPane.add(label5);
        label5.setBounds(new Rectangle(new Point(25, 110), label5.getPreferredSize()));

        //---- label6 ----
        label6.setText("\u710a\u673a\u72b6\u6001\uff1a");
        label6.setFont(label6.getFont().deriveFont(label6.getFont().getSize() + 3f));
        contentPane.add(label6);
        label6.setBounds(new Rectangle(new Point(25, 155), label6.getPreferredSize()));

        //---- label7 ----
        label7.setText("\u5b8c\u6210\u5de5\u4ef6\uff1a");
        label7.setFont(label7.getFont().deriveFont(label7.getFont().getSize() + 3f));
        contentPane.add(label7);
        label7.setBounds(new Rectangle(new Point(25, 200), label7.getPreferredSize()));

        //---- label8 ----
        label8.setText("\u6545\u969c\u5de5\u4ef6\uff1a");
        label8.setFont(label8.getFont().deriveFont(label8.getFont().getSize() + 3f));
        contentPane.add(label8);
        label8.setBounds(new Rectangle(new Point(25, 245), label8.getPreferredSize()));

        //---- label9 ----
        label9.setText("\u5de5\u4ef6\u7f16\u53f7\uff1a");
        label9.setFont(label9.getFont().deriveFont(label9.getFont().getSize() + 3f));
        contentPane.add(label9);
        label9.setBounds(new Rectangle(new Point(220, 155), label9.getPreferredSize()));

        //---- label10 ----
        label10.setText("\u68c0\u6d4b\u7ed3\u679c\uff1a");
        label10.setFont(label10.getFont().deriveFont(label10.getFont().getSize() + 3f));
        contentPane.add(label10);
        label10.setBounds(new Rectangle(new Point(220, 200), label10.getPreferredSize()));

        //---- label11 ----
        label11.setText("-1");
        label11.setFont(label11.getFont().deriveFont(label11.getFont().getStyle() | Font.BOLD, label11.getFont().getSize() + 3f));
        label11.setBackground(Color.white);
        label11.setForeground(new Color(0, 153, 204));
        contentPane.add(label11);
        label11.setBounds(100, 110, 90, 20);

        //---- label12 ----
        label12.setText("-1");
        label12.setFont(label12.getFont().deriveFont(label12.getFont().getStyle() | Font.BOLD, label12.getFont().getSize() + 3f));
        label12.setBackground(Color.white);
        label12.setForeground(new Color(0, 153, 204));
        contentPane.add(label12);
        label12.setBounds(100, 155, 90, 20);

        //---- label13 ----
        label13.setText("0");
        label13.setFont(label13.getFont().deriveFont(label13.getFont().getStyle() | Font.BOLD, label13.getFont().getSize() + 3f));
        label13.setBackground(Color.white);
        label13.setForeground(new Color(0, 153, 204));
        contentPane.add(label13);
        label13.setBounds(100, 200, 75, label13.getPreferredSize().height);

        //---- label14 ----
        label14.setText("0");
        label14.setFont(label14.getFont().deriveFont(label14.getFont().getStyle() | Font.BOLD, label14.getFont().getSize() + 3f));
        label14.setBackground(Color.white);
        label14.setForeground(new Color(255, 51, 0));
        contentPane.add(label14);
        label14.setBounds(100, 245, 75, label14.getPreferredSize().height);

        //---- label15 ----
        label15.setText("-1");
        label15.setFont(label15.getFont().deriveFont(label15.getFont().getStyle() | Font.BOLD, label15.getFont().getSize() + 3f));
        label15.setBackground(Color.white);
        label15.setForeground(new Color(0, 153, 204));
        contentPane.add(label15);
        label15.setBounds(305, 155, 85, label15.getPreferredSize().height);

        //---- label16 ----
        label16.setText("-1");
        label16.setFont(label16.getFont().deriveFont(label16.getFont().getStyle() | Font.BOLD, label16.getFont().getSize() + 3f));
        label16.setBackground(Color.white);
        label16.setForeground(new Color(0, 153, 204));
        contentPane.add(label16);
        label16.setBounds(305, 200, 85, label16.getPreferredSize().height);

        //---- button7 ----
        button7.setText("\u6d4b\u8bd5\uff1a\u6dfb\u52a0\u6545\u969c\u8bb0\u5f55");
        button7.setFont(button7.getFont().deriveFont(button7.getFont().getSize() + 1f));
        button7.addActionListener(e -> button7ActionPerformed(e));
        contentPane.add(button7);
        button7.setBounds(105, 270, 160, 35);

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
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JLabel label2;
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JLabel label3;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JPopupMenu.Separator separator4;
    private JTabbedPane tabbedPane1;
    private JPanel panel2;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JTabbedPane tabbedPane2;
    private JPanel panel3;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton button5;
    private JLabel label1;
    private JComboBox<String> comboBox1;
    private JButton button6;
    private JPanel panel4;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JLabel label10;
    private JLabel label11;
    private JLabel label12;
    private JLabel label13;
    private JLabel label14;
    private JLabel label15;
    private JLabel label16;
    private JButton button7;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
