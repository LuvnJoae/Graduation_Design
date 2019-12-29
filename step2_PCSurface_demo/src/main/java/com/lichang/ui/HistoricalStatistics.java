/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import com.lichang.utils.HistoricalStatisticsUtils.LineChartUtil;
import com.lichang.utils.HistoricalStatisticsUtils.TableUtil;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.ChangePasswordUtil;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author unknown
 */
public class HistoricalStatistics extends JFrame {
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

    private List<Map<String, Object>> machine_data_brief_mapsList;
    private List<Map<String, Object>> machine_fault_data_mapsList;
    private List<Map<String, Object>> expert_production_mapsList;
    private String prodution_name;
    private JFreeChart lineChart; // 折线图模型
    private JPanel chartPanel; //折线图Panel
    private String machine_fault_data_timeCol; //工件或故障表的time列，用于作主键查看详情

    //无参（预设账户信息）
    public HistoricalStatistics() {
        username = "admin";
        adminFlag = true;

        initComponents();

        this.setBounds(273, 95, 990, 625);
        setVisible(true);
    }

    //有参（接收登录账户信息）
    public HistoricalStatistics(String username, Boolean adminFlag) {
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
     * 整体页面  事件监听
     */
    //当打开此frame时，触发
    private void thisWindowOpened(WindowEvent e) {
        setUI();
        updateTable1();
        updateTable2();
        updateChartPanel();
        updateComboBox1();
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
    //实时监测 按钮： 点击跳转
    private void button1ActionPerformed(ActionEvent e) {
        this.dispose();
    }

    //专家系统 按钮： 点击跳转
    private void button3ActionPerformed(ActionEvent e) {
        new ExpertSystem(username, adminFlag);
        this.dispose();
    }

    //管理与设置 按钮： 点击跳转
    private void button4ActionPerformed(ActionEvent e) {
        new Setting(username, adminFlag);
        this.dispose();
    }

    /**
     * 工件统计与查询
     */
    //table1: 设置表格格式
    private void initTable1Form() {
        //设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        table1.setDefaultRenderer(Object.class, r); //表格居中
        //设置表头居中
        ((DefaultTableCellRenderer)table1.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    //table1 主方法： 刷新table1
    private void updateTable1() {
        initTable1Form(); //设置表格格式

        machine_data_brief_mapsList = TableUtil.getData("machine_data_brief"); //获得工件简要表的内容

        //非空判断
        if (machine_data_brief_mapsList == null || machine_data_brief_mapsList.size() == 0) {
            return;
        }

        DefaultTableModel table1Model = (DefaultTableModel) table1.getModel(); //获取model
        table1Model.setRowCount(0); //先清空，再加载新数据

        for (Map<String, Object> map : machine_data_brief_mapsList) {
            Object[] newRowData = {
                    map.get("id"),
                    map.get("time").toString().split("\\.")[0], //去除timestamp后面的 .0
                    map.get("production_name"),
                    map.get("production_num"),
                    map.get("result"),
                    "<html><font color = 'blue'><u>查看</u></font></html>"
            };

            table1Model.addRow(newRowData);
        }
    }

    //查询 按钮
    private void button5ActionPerformed(ActionEvent e) {
        String findItem = textField1.getText(); //获取搜索条件
        findRegexTable1(findItem);
    }

    //返回 按钮
    private void button6ActionPerformed(ActionEvent e) {
        findRegexTable1(""); //返回即查询空字符串（则查询到全部内容）
        textField1.setText(""); //点击返回后，清空文本框
    }

    //查询、返回 主方法
    private void findRegexTable1(String findItem) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table1.getModel());
        table1.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(findItem)); //查询格式为 正则表达式，范围为整个table
    }

    //刷新 按钮
    private void button7ActionPerformed(ActionEvent e) {
        updateTable1(); //刷新表格

        updateChartPanel();
    }

    //事件： 鼠标点击 table1中查看的一列 时
    private void table1MouseClicked(MouseEvent e) {
        //若点击的为 查看 这列，则进入事件，创建新窗口，展示详情
        if (table1.getSelectedColumn() == 5) {
            int selectedRow = table1.getSelectedRow();
            machine_fault_data_timeCol = table1.getValueAt(selectedRow, 1).toString() + ".0"; //和timestamp格式相同
            if (table1.getValueAt(selectedRow, 4).equals("优秀") || table1.getValueAt(selectedRow, 4).equals("合格")) {
                new DetailsAll(this, "工件信息详情", true, machine_fault_data_timeCol); //创建新窗口
            } else if (table1.getValueAt(selectedRow, 4).equals("隐患") || table1.getValueAt(selectedRow, 4).equals("次品")) {
                new DetailsFault(this, "故障信息详情", true, machine_fault_data_timeCol); //创建新窗口
            } else {
                return;
            }
        } else {
            return;
        }
    }

    /**
     * 故障统计与查询
     */
    //table2: 设置表格格式
    private void initTable2Form() {
        //设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        table2.setDefaultRenderer(Object.class, r);
        //设置表头居中
        ((DefaultTableCellRenderer)table2.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    //table2 主方法： 刷新table1
    private void updateTable2() {
        initTable2Form(); //设置表格格式

        machine_fault_data_mapsList = TableUtil.getData("machine_fault_data"); //获得工件简要表的内容

        //非空判断
        if (machine_fault_data_mapsList == null || machine_fault_data_mapsList.size() == 0) {

            System.out.println("***************");
            return;
        }

        DefaultTableModel table2Model = (DefaultTableModel) table2.getModel(); //获取model
        table2Model.setRowCount(0); //先清空，再加载新数据

        for (Map<String, Object> map : machine_fault_data_mapsList) {
            Object[] newRowData = {
                    map.get("id"),
                    map.get("time").toString().split("\\.")[0], //去除timestamp后面的 .0
                    map.get("production_name"),
                    map.get("production_num"),
                    map.get("fault_type"),
                    map.get("fault_maxnum"),
                    map.get("result"),
                    "<html><font color = 'blue'><u>查看</u></font></html>"
            };

            table2Model.addRow(newRowData);
        }
    }

    //查询 按钮
    private void button8ActionPerformed(ActionEvent e) {
        String findItem = textField2.getText(); //获取搜索条件
        findRegexTable2(findItem);
    }

    //返回 按钮
    private void button9ActionPerformed(ActionEvent e) {
        findRegexTable2(""); //返回即查询空字符串（则查询到全部内容）
        textField2.setText(""); //点击返回后，清空文本框
    }

    //查询、返回 主方法
    private void findRegexTable2(String findItem) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table2.getModel());
        table2.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(findItem)); //查询格式为 正则表达式，范围为整个table
    }

    //刷新 按钮
    private void button10ActionPerformed(ActionEvent e) {
        updateTable2(); //刷新表格

        updateChartPanel(); //刷新柱状图
    }

    //事件： 鼠标点击 table1中查看的一列 时
    private void table2MouseClicked(MouseEvent e) {
        //若点击的为 查看 这列，则进入事件，创建新窗口，展示详情
        if (table2.getSelectedColumn() == 7) {
            int selectedRow = table2.getSelectedRow();
            machine_fault_data_timeCol = table2.getValueAt(selectedRow, 1).toString() + ".0"; //和timestamp格式相同
            new DetailsFault(this, "故障信息详情", true, machine_fault_data_timeCol); //创建新窗口
        } else {
            return;
        }
    }

    /**
     * 工件统计图相关
     */
    //刷新 产品下拉框内容
    private void updateComboBox1() {
        comboBox1.removeAllItems(); //清空原数据
        expert_production_mapsList = TableUtil.getData("expert_production"); //获取产品表内容，更新内容
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

    //工件统计图
    private void updateChartPanel() {
        //获取所选产品名称
        prodution_name = (String) comboBox1.getSelectedItem();

        lineChart = LineChartUtil.getLineChart(prodution_name); // 获得充满数据的chart模型

        //若chartPanel已存在，则删去重新创建
        if (chartPanel != null) {
            panel4.remove(chartPanel);
        }
        chartPanel = new ChartPanel(lineChart);

        chartPanel.setLayout(null);
        chartPanel.setBounds(0, 0, 420, 460);

        panel4.add(chartPanel);
        panel4.repaint();
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
        textField1 = new JTextField();
        button5 = new JButton();
        button6 = new JButton();
        button7 = new JButton();
        panel3 = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        textField2 = new JTextField();
        button8 = new JButton();
        button9 = new JButton();
        button10 = new JButton();
        panel4 = new JPanel();
        label1 = new JLabel();
        comboBox1 = new JComboBox();
        label4 = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u754c\u9762");
        setAlwaysOnTop(true);
        setResizable(false);
        setBackground(new Color(238, 238, 238));
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
            label2.setBounds(0, 0, 50, 30);

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
            label3.setBounds(50, 0, 85, 30);

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
        panel1.setBounds(750, 0, 226, 50);

        //---- button1 ----
        button1.setText("\u5b9e\u65f6\u76d1\u6d4b");
        button1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 14));
        button1.setForeground(new Color(51, 51, 51));
        button1.addActionListener(e -> button1ActionPerformed(e));
        contentPane.add(button1);
        button1.setBounds(55, 50, 135, 40);

        //---- button2 ----
        button2.setText("\u5386\u53f2\u7edf\u8ba1\u4e0e\u67e5\u8be2");
        button2.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 14));
        button2.setForeground(new Color(51, 51, 51));
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

            //======== panel2 ========
            {
                panel2.setLayout(null);

                //======== scrollPane1 ========
                {

                    //---- table1 ----
                    table1.setRowHeight(20);
                    table1.setModel(new DefaultTableModel(
                        new Object[][] {
                        },
                        new String[] {
                            "id", "\u65f6\u95f4", "\u4ea7\u54c1\u540d\u79f0", "\u5de5\u4ef6\u7f16\u53f7", "\u68c0\u6d4b\u7ed3\u679c", "\u8be6\u60c5"
                        }
                    ) {
                        boolean[] columnEditable = new boolean[] {
                            false, false, false, false, false, false
                        };
                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return columnEditable[columnIndex];
                        }
                    });
                    {
                        TableColumnModel cm = table1.getColumnModel();
                        cm.getColumn(0).setPreferredWidth(50);
                        cm.getColumn(1).setPreferredWidth(120);
                        cm.getColumn(2).setPreferredWidth(120);
                        cm.getColumn(3).setPreferredWidth(60);
                        cm.getColumn(4).setPreferredWidth(60);
                        cm.getColumn(5).setPreferredWidth(60);
                    }
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
                scrollPane1.setBounds(0, 40, 540, 425);
                panel2.add(textField1);
                textField1.setBounds(5, 5, 135, 30);

                //---- button5 ----
                button5.setText("\u67e5\u8be2");
                button5.addActionListener(e -> button5ActionPerformed(e));
                panel2.add(button5);
                button5.setBounds(155, 5, 70, 30);

                //---- button6 ----
                button6.setText("\u8fd4\u56de");
                button6.addActionListener(e -> button6ActionPerformed(e));
                panel2.add(button6);
                button6.setBounds(225, 5, 70, 30);

                //---- button7 ----
                button7.setText("\u5237\u65b0");
                button7.addActionListener(e -> button7ActionPerformed(e));
                panel2.add(button7);
                button7.setBounds(460, 5, 70, 30);

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
            tabbedPane1.addTab("\u5de5\u4ef6\u7edf\u8ba1\u4e0e\u67e5\u8be2", panel2);

            //======== panel3 ========
            {
                panel3.setLayout(null);

                //======== scrollPane2 ========
                {

                    //---- table2 ----
                    table2.setRowHeight(20);
                    table2.setModel(new DefaultTableModel(
                        new Object[][] {
                            {null, null, null, null, null, null, null, null},
                        },
                        new String[] {
                            "id", "\u65f6\u95f4", "\u4ea7\u54c1\u540d\u79f0", "\u5de5\u4ef6\u7f16\u53f7", "\u6545\u969c\u7c7b\u578b", "\u6700\u5927\u9891\u6b21", "\u68c0\u6d4b\u7ed3\u679c", "\u8be6\u60c5"
                        }
                    ) {
                        boolean[] columnEditable = new boolean[] {
                            false, false, false, false, false, false, false, false
                        };
                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return columnEditable[columnIndex];
                        }
                    });
                    {
                        TableColumnModel cm = table2.getColumnModel();
                        cm.getColumn(0).setMinWidth(30);
                        cm.getColumn(1).setMinWidth(110);
                        cm.getColumn(2).setMinWidth(110);
                        cm.getColumn(3).setMinWidth(57);
                        cm.getColumn(4).setMinWidth(57);
                        cm.getColumn(5).setMinWidth(57);
                        cm.getColumn(6).setMinWidth(57);
                        cm.getColumn(7).setMinWidth(35);
                        cm.getColumn(7).setPreferredWidth(30);
                    }
                    table2.setAutoCreateRowSorter(true);
                    table2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            table2MouseClicked(e);
                        }
                    });
                    scrollPane2.setViewportView(table2);
                }
                panel3.add(scrollPane2);
                scrollPane2.setBounds(0, 40, 540, 425);
                panel3.add(textField2);
                textField2.setBounds(5, 5, 135, 30);

                //---- button8 ----
                button8.setText("\u67e5\u8be2");
                button8.addActionListener(e -> button8ActionPerformed(e));
                panel3.add(button8);
                button8.setBounds(155, 5, 70, 30);

                //---- button9 ----
                button9.setText("\u8fd4\u56de");
                button9.addActionListener(e -> button9ActionPerformed(e));
                panel3.add(button9);
                button9.setBounds(225, 5, 70, 30);

                //---- button10 ----
                button10.setText("\u5237\u65b0");
                button10.addActionListener(e -> button10ActionPerformed(e));
                panel3.add(button10);
                button10.setBounds(460, 5, 70, 30);

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
            tabbedPane1.addTab("\u6545\u969c\u7edf\u8ba1\u4e0e\u67e5\u8be2", panel3);
        }
        contentPane.add(tabbedPane1);
        tabbedPane1.setBounds(5, 100, 540, 490);

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
        panel4.setBounds(550, 130, 420, 460);

        //---- label1 ----
        label1.setText("\u5de5\u4ef6\u7edf\u8ba1\u56fe");
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 4f));
        label1.setForeground(new Color(51, 51, 51));
        contentPane.add(label1);
        label1.setBounds(705, 100, 100, 25);

        //---- comboBox1 ----
        comboBox1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                comboBox1MouseClicked(e);
            }
        });
        contentPane.add(comboBox1);
        comboBox1.setBounds(850, 100, 120, 30);

        //---- label4 ----
        label4.setText("\u4ea7\u54c1:");
        label4.setFont(label4.getFont().deriveFont(label4.getFont().getSize() + 2f));
        contentPane.add(label4);
        label4.setBounds(810, 105, 35, 19);

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
        pack();
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
    private JTextField textField1;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JPanel panel3;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JTextField textField2;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JPanel panel4;
    private JLabel label1;
    private JComboBox comboBox1;
    private JLabel label4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
