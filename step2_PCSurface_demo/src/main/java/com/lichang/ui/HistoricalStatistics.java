/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import com.lichang.utils.HistoricalStatisticsUtil.Table;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.RealTimeMonitoringUtil.ChangePassword;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author unknown
 */
public class HistoricalStatistics extends JFrame {


    private static Logger log = LoggerUtil.getLogger(); // 日志

    // 自定义的变量
    private String username; // 当前用户名
    private boolean adminFlag; // 用户类型
    private JFreeChart realTimeLineChart; // 折线图模型
    private JPanel chartPanel; // 折线图
    private JPanel chartPanel2; // 折线图放大
    private JDialog jDialog; // 折线图
    private JDialog jDialog2; // 密码修改
    private JPanel changePasswordPanel; // 密码修改
    private JLabel oldValidationTip; // 旧密码 验证提示
    private boolean oldChangeFlag; //判断旧密码是否通过验证

    List<Map<String, Object>> machine_fault_data_mapsList; //故障表

    //无参（预设账户信息）
    public HistoricalStatistics() {
        log.debug("无参构造");

        //TEST: 测试用，直接打开该页面时，暂时给username和flag一个值
        //标记时间：2019/11/21 15:56  预解决时间：
        username = "admin";
        adminFlag = true;

        initComponents();

        this.setBounds(273, 95, 990, 625);
        setVisible(true);
    }

    //有参（接收登录账户信息）
    public HistoricalStatistics(String username, Boolean adminFlag) {
        log.debug("有参构造");
        this.username = username;
        this.adminFlag = adminFlag;

        initComponents();

        label3Bind(username); //显示当前用户信息

        this.setBounds(273, 95, 990, 625);
        setVisible(true);
    }

    /**
     * Lable3 账户信息: 显示当前登录用户
     * @param username
     */
    private void label3Bind(String username) {
        log.debug("Lable3 账户信息: 显示当前登录用户");
        label3.setText(username);
    }

    /**
     * MenuItem 用户设置:  切换用户
     * @param e
     */
    private void menuItem1ActionPerformed(ActionEvent e) {
        log.debug("MenuItem 用户设置:  切换用户");

        new Login();
        this.dispose();
    }

    /**
     * MenuItem 用户设置： 更改密码
     * @param e
     */
    private void menuItem2ActionPerformed(ActionEvent e) {
        log.debug("MenuItem 用户设置： 更改密码");

        if (!adminFlag) {
            JOptionPane.showMessageDialog(this, "您没有该权限！请用管理员身份登录！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        jDialog2 = new JDialog(this, "",true);
        changePasswordPanel = new JPanel();
        oldValidationTip = new JLabel();
        oldChangeFlag = false;

        changePasswordPanel.setLayout(null);

        //提示
        JTextArea tip = new JTextArea("提示：密码5~10个字符，可使用字母、数字、一般符号，需以字母开头");
        changePasswordPanel.add(tip);
        tip.setBounds(50, 20, 300, 40);
        tip.setLineWrap(true); // 自动换行
        tip.setWrapStyleWord(true);
        tip.setEditable(false); // 不可编辑
        tip.setBackground(new Color(240, 240, 240)); // 背景色统一


        //旧密码提示
        JLabel oldPasswordTip = new JLabel("请输入旧密码: ");
        changePasswordPanel.add(oldPasswordTip);
        oldPasswordTip.setBounds(40, 60, 110,30);
        oldPasswordTip.setFont(new Font("",Font.BOLD, 15));


        //新密码提示
        JLabel newPasswordTip = new JLabel("请输入新密码: ");
        changePasswordPanel.add(newPasswordTip);
        newPasswordTip.setBounds(40, 120, 110,30);
        newPasswordTip.setFont(new Font("",Font.BOLD, 15));


        //旧密码
        JTextField oldPasswordField = new JTextField();
        changePasswordPanel.add(oldPasswordField);
        oldPasswordField.setBounds(160, 60, 90, 30);
        oldPasswordField.setColumns(10);
        oldPasswordField.setFont(new Font("黑体", Font.PLAIN,15));

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

                if (ChangePassword.validate(table, username, password)) {
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
        newPasswordField.setFont(new Font("黑体", Font.PLAIN,15));

        newPasswordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = newPasswordField.getText();
                if (newPassword.length() > 10
                        || newPassword.length() < 5
                        || (newPassword.charAt(0) < 'A' || newPassword.charAt(0) > 'z')
                        || (newPassword.charAt(0) > 'Z' && newPassword.charAt(0) < 'a')) {
                    JOptionPane.showMessageDialog(jDialog2, "新密码格式错误，请重新输入", "提示", JOptionPane.WARNING_MESSAGE);
                }else {
                    if (oldChangeFlag) {
                        String password = newPasswordField.getText();
                        String table;

                        if (adminFlag == true) {
                            table = "admin";
                        } else {
                            table = "emp";
                        }

                        ChangePassword.newPassword(table, username, password);
                        JOptionPane.showMessageDialog(jDialog2, "新密码格式正确，修改成功！", "提示", JOptionPane.WARNING_MESSAGE);
                        jDialog2.dispose();
                    }else {
                        JOptionPane.showMessageDialog(jDialog2, "请先验证旧密码！", "提示", JOptionPane.WARNING_MESSAGE);
                    }

                }
            }
        });

        jDialog2.setSize(400,250);
        jDialog2.setAlwaysOnTop(true);
        jDialog2.setLocationRelativeTo(null);
        jDialog2.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jDialog2.add(changePasswordPanel);
        jDialog2.setVisible(true);
    }

    /**
     * 实时监测 按钮：点击跳转
     * @param e
     */
    private void button1ActionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
     * 故障校验 按钮： 点击跳转
     * @param e
     */
    private void button3ActionPerformed(ActionEvent e) {
        new ExpertSystem(username, adminFlag);
        this.dispose();
    }

    /**
     * 整体页面  事件监听
     */
    //当激活此frame时，触发
    private void thisWindowActivated(WindowEvent e) {
        //加载表格 数据
        machine_fault_data_mapsList = Table.getData_table4("machine_fault_data");
    }

    /**
     * 故障查询 表格
     */
    private void initTable4() {
        DefaultTableModel table4Model = (DefaultTableModel) table4.getModel();
        ArrayList<Object> rowData_list = new ArrayList<>();

        for (Map<String, Object> map : machine_fault_data_mapsList) {
            rowData_list.add(map.get("id"));
            rowData_list.add(map.get("time"));
            rowData_list.add(map.get("num"));
            rowData_list.add(map.get("fault_type"));
            rowData_list.add(map.get("fault_maxnum"));
            rowData_list.add(map.get("result"));

            table4Model.addRow(rowData_list.toArray()); //添加行数据至model
            rowData_list = null; //清空list
        }
    }

    /**
     * 按钮
     */
    //刷新 按钮
    private void button7ActionPerformed(ActionEvent e) {
        initTable4();
    }



    /**
     *  JFormDesigner自带，定义自生成
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
        scrollPane3 = new JScrollPane();
        table3 = new JTable();
        tabbedPane2 = new JTabbedPane();
        panel3 = new JPanel();
        scrollPane5 = new JScrollPane();
        table4 = new JTable();
        textField1 = new JTextField();
        button5 = new JButton();
        button6 = new JButton();
        button7 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u754c\u9762");
        setAlwaysOnTop(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                thisWindowActivated(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel1 ========
        {
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
            panel1.add(label3);
            label3.setBounds(55, 0, 60, 30);

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
        button1.addActionListener(e -> button1ActionPerformed(e));
        contentPane.add(button1);
        button1.setBounds(55, 60, 120, 30);

        //---- button2 ----
        button2.setText("\u5386\u53f2\u7edf\u8ba1\u4e0e\u67e5\u8be2");
        contentPane.add(button2);
        button2.setBounds(295, 60, 120, 30);

        //---- button3 ----
        button3.setText("\u4e13\u5bb6\u7cfb\u7edf");
        button3.addActionListener(e -> button3ActionPerformed(e));
        contentPane.add(button3);
        button3.setBounds(525, 60, 120, 30);

        //---- button4 ----
        button4.setText("\u7ba1\u7406\u4e0e\u8bbe\u7f6e");
        contentPane.add(button4);
        button4.setBounds(765, 60, 120, 30);
        contentPane.add(separator4);
        separator4.setBounds(5, 90, 920, 10);

        //======== tabbedPane1 ========
        {
            tabbedPane1.setFont(tabbedPane1.getFont().deriveFont(tabbedPane1.getFont().getSize() + 2f));

            //======== panel2 ========
            {
                panel2.setLayout(null);

                //======== scrollPane3 ========
                {

                    //---- table3 ----
                    table3.setRowHeight(20);
                    table3.setModel(new DefaultTableModel(
                        new Object[][] {
                            {null, null, null},
                        },
                        new String[] {
                            "\u6545\u969c\u5224\u5b9a", "\u6545\u969c\u8868\u73b0", "\u603b\u6b21\u6570"
                        }
                    ));
                    scrollPane3.setViewportView(table3);
                }
                panel2.add(scrollPane3);
                scrollPane3.setBounds(0, 0, 480, 190);

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
            tabbedPane1.addTab("\u6545\u969c\u7edf\u8ba1", panel2);
        }
        contentPane.add(tabbedPane1);
        tabbedPane1.setBounds(5, 95, 485, 220);

        //======== tabbedPane2 ========
        {
            tabbedPane2.setFont(tabbedPane2.getFont().deriveFont(tabbedPane2.getFont().getSize() + 2f));

            //======== panel3 ========
            {
                panel3.setLayout(null);

                //======== scrollPane5 ========
                {

                    //---- table4 ----
                    table4.setRowHeight(20);
                    table4.setModel(new DefaultTableModel(
                        new Object[][] {
                            {null, "", null, null, null, null, null},
                        },
                        new String[] {
                            "id", "\u6545\u969c\u65f6\u95f4", "\u5de5\u4ef6\u7f16\u53f7", "\u6545\u969c\u7c7b\u578b", "\u6700\u5927\u9891\u6b21", "\u5224\u5b9a", "\u8be6\u60c5"
                        }
                    ));
                    {
                        TableColumnModel cm = table4.getColumnModel();
                        cm.getColumn(0).setPreferredWidth(50);
                        cm.getColumn(1).setPreferredWidth(120);
                    }
                    scrollPane5.setViewportView(table4);
                }
                panel3.add(scrollPane5);
                scrollPane5.setBounds(0, 40, 995, 200);
                panel3.add(textField1);
                textField1.setBounds(0, 5, 120, 30);

                //---- button5 ----
                button5.setText("\u67e5\u8be2");
                panel3.add(button5);
                button5.setBounds(new Rectangle(new Point(125, 5), button5.getPreferredSize()));

                //---- button6 ----
                button6.setText("\u8fd4\u56de");
                panel3.add(button6);
                button6.setBounds(185, 5, 58, 28);

                //---- button7 ----
                button7.setText("\u5237\u65b0");
                button7.addActionListener(e -> button7ActionPerformed(e));
                panel3.add(button7);
                button7.setBounds(930, 5, 58, 28);

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
            tabbedPane2.addTab("\u6545\u969c\u67e5\u8be2", panel3);
        }
        contentPane.add(tabbedPane2);
        tabbedPane2.setBounds(5, 320, 1000, 270);

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
        setSize(1010, 625);
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
    private JScrollPane scrollPane3;
    private JTable table3;
    private JTabbedPane tabbedPane2;
    private JPanel panel3;
    private JScrollPane scrollPane5;
    private JTable table4;
    private JTextField textField1;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
