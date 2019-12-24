/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import com.lichang.utils.ChangePasswordUtil;
import com.lichang.utils.HistoricalStatisticsUtils.LineChartUtil;
import com.lichang.utils.HistoricalStatisticsUtils.TableUtil;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SettingUtils.SettingUtil;
import org.apache.log4j.Logger;
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
public class Setting extends JFrame {
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

    private List<Map<String, Object>> machine_setting_mapsList; //焊机表

    //无参（预设账户信息）
    public Setting() {
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
    public Setting(String username, Boolean adminFlag) {
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
     */
    private void label3Bind(String username) {
        log.debug("Lable3 账户信息: 显示当前登录用户");
        label3.setText(username);
    }

    /**
     * Menu 菜单
     */
    //MenuItem 用户设置:  切换用户
    private void menuItem1ActionPerformed(ActionEvent e) {
        log.debug("MenuItem 用户设置:  切换用户");

        new Login();
        this.dispose();
    }

    //MenuItem 用户设置： 更改密码
    private void menuItem2ActionPerformed(ActionEvent e) {
        log.debug("MenuItem 用户设置： 更改密码");

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
        JTextArea tip = new JTextArea("提示：密码5~10个字符，可使用字母、数字、下划线，需以字母开头");
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

                if (!Pattern.matches("^[a-zA-Z][a-zA-Z0-9_]{4,15}$", newPassword)) {
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
    //实时监测界面 按钮： 点击跳转
    private void button1ActionPerformed(ActionEvent e) {
        this.dispose();
    }

    //历史统计与查询 按钮： 点击跳转
    private void button2ActionPerformed(ActionEvent e) {
        new HistoricalStatistics(username, adminFlag);
        this.dispose();
    }

    //专家系统 按钮： 点击跳转
    private void button3ActionPerformed(ActionEvent e) {
        new ExpertSystem(username, adminFlag);
        this.dispose();
    }

    /**
     * 整体页面  事件监听
     */
    //当打开此frame时，触发
    private void thisWindowOpened(WindowEvent e) {
        nowMachine();
        updateComboBox1();
    }

    /**
     * 焊机管理
     */
    //当前焊机： 焊机名称与状态
    private void nowMachine() {
        String[] nowMachine = SettingUtil.getNowMachine();
        String machineName = nowMachine[0];
        String machineStatus = nowMachine[1];
        label14.setText(machineName);
        label15.setText(machineStatus);
    }

    //修改当前焊机： 焊机下拉框内容
    private void updateComboBox1() {
        comboBox1.removeAllItems(); //清空原数据
        machine_setting_mapsList = SettingUtil.getData("machine_setting"); //获取产品表内容，更新内容
        if (machine_setting_mapsList == null || machine_setting_mapsList.size() == 0) {
            return;
        }
        //更新 产品选择 下拉框内容
        for (Map<String, Object> map : machine_setting_mapsList) {
            comboBox1.addItem((String) map.get("machine_name")); //下拉框添加内容
        }
        comboBox1.setSelectedIndex(-1);
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
        label4 = new JLabel();
        label5 = new JLabel();
        panel2 = new JPanel();
        label1 = new JLabel();
        label17 = new JLabel();
        label14 = new JLabel();
        label12 = new JLabel();
        label13 = new JLabel();
        label15 = new JLabel();
        label16 = new JLabel();
        comboBox1 = new JComboBox();
        label6 = new JLabel();
        label7 = new JLabel();
        textField3 = new JTextField();
        label9 = new JLabel();
        textField1 = new JTextField();
        label10 = new JLabel();
        label11 = new JLabel();
        textField2 = new JTextField();
        button5 = new JButton();
        label41 = new JLabel();
        button8 = new JButton();
        panel3 = new JPanel();
        label8 = new JLabel();
        label18 = new JLabel();
        label19 = new JLabel();
        label20 = new JLabel();
        label21 = new JLabel();
        label22 = new JLabel();
        label23 = new JLabel();
        comboBox2 = new JComboBox();
        label24 = new JLabel();
        label25 = new JLabel();
        textField4 = new JTextField();
        label26 = new JLabel();
        textField5 = new JTextField();
        label27 = new JLabel();
        label28 = new JLabel();
        textField6 = new JTextField();
        button6 = new JButton();
        panel4 = new JPanel();
        label29 = new JLabel();
        label30 = new JLabel();
        label31 = new JLabel();
        label32 = new JLabel();
        label33 = new JLabel();
        label34 = new JLabel();
        label35 = new JLabel();
        comboBox3 = new JComboBox();
        label36 = new JLabel();
        label37 = new JLabel();
        textField7 = new JTextField();
        label38 = new JLabel();
        textField8 = new JTextField();
        label39 = new JLabel();
        label40 = new JLabel();
        textField9 = new JTextField();
        button7 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u754c\u9762");
        setAlwaysOnTop(true);
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
        button2.addActionListener(e -> button2ActionPerformed(e));
        contentPane.add(button2);
        button2.setBounds(295, 60, 130, 30);

        //---- button3 ----
        button3.setText("\u4e13\u5bb6\u7cfb\u7edf");
        button3.addActionListener(e -> button3ActionPerformed(e));
        contentPane.add(button3);
        button3.setBounds(550, 60, 120, 30);

        //---- button4 ----
        button4.setText("\u7ba1\u7406\u4e0e\u8bbe\u7f6e");
        contentPane.add(button4);
        button4.setBounds(805, 60, 120, 30);
        contentPane.add(separator4);
        separator4.setBounds(5, 90, 965, 10);

        //---- label4 ----
        label4.setText("\u7528\u6237\u4fe1\u606f\u7ba1\u7406");
        label4.setFont(label4.getFont().deriveFont(label4.getFont().getSize() + 5f));
        contentPane.add(label4);
        label4.setBounds(530, 25, 125, 25);

        //---- label5 ----
        label5.setText("\u6570\u636e\u5e93\u8bbe\u7f6e");
        label5.setFont(label5.getFont().deriveFont(label5.getFont().getSize() + 5f));
        contentPane.add(label5);
        label5.setBounds(440, 30, 115, 25);

        //======== panel2 ========
        {
            panel2.setBackground(new Color(204, 204, 204));
            panel2.setLayout(null);

            //---- label1 ----
            label1.setText("\u710a\u673a\u7ba1\u7406");
            label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 5f));
            panel2.add(label1);
            label1.setBounds(new Rectangle(new Point(100, 10), label1.getPreferredSize()));

            //---- label17 ----
            label17.setText("\u5f53\u524d\u710a\u673a");
            label17.setFont(label17.getFont().deriveFont(label17.getFont().getSize() + 2f));
            panel2.add(label17);
            label17.setBounds(180, 55, 65, 18);

            //---- label14 ----
            label14.setText("\u5f53\u524d\u710a\u673a");
            label14.setFont(label14.getFont().deriveFont(label14.getFont().getSize() + 2f));
            label14.setForeground(new Color(0, 153, 204));
            panel2.add(label14);
            label14.setBounds(115, 80, 125, 20);

            //---- label12 ----
            label12.setText("\u710a\u673a\u540d\u79f0\uff1a");
            label12.setFont(label12.getFont().deriveFont(label12.getFont().getSize() + 2f));
            panel2.add(label12);
            label12.setBounds(35, 80, 80, 20);

            //---- label13 ----
            label13.setText("\u710a\u673a\u72b6\u6001\uff1a");
            label13.setFont(label13.getFont().deriveFont(label13.getFont().getSize() + 2f));
            panel2.add(label13);
            label13.setBounds(35, 115, 85, 20);

            //---- label15 ----
            label15.setText("\u5f53\u524d\u72b6\u6001");
            label15.setFont(label15.getFont().deriveFont(label15.getFont().getSize() + 2f));
            label15.setForeground(new Color(0, 153, 204));
            panel2.add(label15);
            label15.setBounds(115, 115, 125, 20);

            //---- label16 ----
            label16.setText("\u4fee\u6539\u5f53\u524d\u710a\u673a");
            label16.setFont(label16.getFont().deriveFont(label16.getFont().getSize() + 2f));
            panel2.add(label16);
            label16.setBounds(150, 165, 90, 20);

            //---- comboBox1 ----
            comboBox1.setSelectedIndex(-1);
            panel2.add(comboBox1);
            comboBox1.setBounds(115, 195, 125, comboBox1.getPreferredSize().height);

            //---- label6 ----
            label6.setText("\u710a\u673a\u540d\u79f0\uff1a");
            label6.setFont(label6.getFont().deriveFont(label6.getFont().getSize() + 2f));
            panel2.add(label6);
            label6.setBounds(35, 200, 80, label6.getPreferredSize().height);

            //---- label7 ----
            label7.setText("\u710a\u673a\u72b6\u6001\uff1a");
            label7.setFont(label7.getFont().deriveFont(label7.getFont().getSize() + 2f));
            panel2.add(label7);
            label7.setBounds(35, 240, 80, 20);
            panel2.add(textField3);
            textField3.setBounds(115, 235, 125, 30);

            //---- label9 ----
            label9.setText("\u6dfb\u52a0\u710a\u673a");
            label9.setFont(label9.getFont().deriveFont(label9.getFont().getSize() + 2f));
            panel2.add(label9);
            label9.setBounds(175, 320, 65, label9.getPreferredSize().height);
            panel2.add(textField1);
            textField1.setBounds(115, 350, 125, 30);

            //---- label10 ----
            label10.setText("\u710a\u673a\u540d\u79f0:");
            label10.setFont(label10.getFont().deriveFont(label10.getFont().getSize() + 2f));
            panel2.add(label10);
            label10.setBounds(35, 355, 75, 18);

            //---- label11 ----
            label11.setText("\u710a\u673a\u72b6\u6001:");
            label11.setFont(label11.getFont().deriveFont(label11.getFont().getSize() + 2f));
            panel2.add(label11);
            label11.setBounds(35, 395, 75, 18);
            panel2.add(textField2);
            textField2.setBounds(115, 390, 125, 30);

            //---- button5 ----
            button5.setText("\u786e\u5b9a");
            panel2.add(button5);
            button5.setBounds(new Rectangle(new Point(180, 425), button5.getPreferredSize()));

            //---- label41 ----
            label41.setText("Tips: \u4fee\u6539\u540e\uff0c\u5207\u6362\u4e00\u4e0b\u9875\u9762\u5373\u53ef\u6b63\u5e38\u663e\u793a");
            label41.setFont(label41.getFont().deriveFont(label41.getFont().getSize() - 1f));
            panel2.add(label41);
            label41.setBounds(0, 460, 220, 20);

            //---- button8 ----
            button8.setText("\u786e\u5b9a");
            panel2.add(button8);
            button8.setBounds(180, 270, 58, 28);

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
        contentPane.add(panel2);
        panel2.setBounds(5, 100, 295, 480);

        //======== panel3 ========
        {
            panel3.setBackground(new Color(204, 204, 204));
            panel3.setLayout(null);

            //---- label8 ----
            label8.setText("\u710a\u673a\u7ba1\u7406");
            label8.setFont(label8.getFont().deriveFont(label8.getFont().getSize() + 5f));
            panel3.add(label8);
            label8.setBounds(new Rectangle(new Point(100, 10), label8.getPreferredSize()));

            //---- label18 ----
            label18.setText("\u5f53\u524d\u710a\u673a");
            label18.setFont(label18.getFont().deriveFont(label18.getFont().getSize() + 2f));
            panel3.add(label18);
            label18.setBounds(180, 55, 65, 18);

            //---- label19 ----
            label19.setText("\u5f53\u524d\u710a\u673a");
            label19.setFont(label19.getFont().deriveFont(label19.getFont().getSize() + 2f));
            panel3.add(label19);
            label19.setBounds(115, 80, 125, 20);

            //---- label20 ----
            label20.setText("\u710a\u673a\u540d\u79f0\uff1a");
            label20.setFont(label20.getFont().deriveFont(label20.getFont().getSize() + 2f));
            panel3.add(label20);
            label20.setBounds(35, 80, 80, 20);

            //---- label21 ----
            label21.setText("\u710a\u673a\u72b6\u6001\uff1a");
            label21.setFont(label21.getFont().deriveFont(label21.getFont().getSize() + 2f));
            panel3.add(label21);
            label21.setBounds(35, 115, 85, 20);

            //---- label22 ----
            label22.setText("\u5f53\u524d\u72b6\u6001");
            label22.setFont(label22.getFont().deriveFont(label22.getFont().getSize() + 2f));
            panel3.add(label22);
            label22.setBounds(115, 115, 125, 20);

            //---- label23 ----
            label23.setText("\u4fee\u6539\u5f53\u524d\u710a\u673a");
            label23.setFont(label23.getFont().deriveFont(label23.getFont().getSize() + 2f));
            panel3.add(label23);
            label23.setBounds(150, 165, 90, 20);
            panel3.add(comboBox2);
            comboBox2.setBounds(115, 195, 125, comboBox2.getPreferredSize().height);

            //---- label24 ----
            label24.setText("\u710a\u673a\u540d\u79f0\uff1a");
            label24.setFont(label24.getFont().deriveFont(label24.getFont().getSize() + 2f));
            panel3.add(label24);
            label24.setBounds(35, 200, 80, label24.getPreferredSize().height);

            //---- label25 ----
            label25.setText("\u710a\u673a\u72b6\u6001\uff1a");
            label25.setFont(label25.getFont().deriveFont(label25.getFont().getSize() + 2f));
            panel3.add(label25);
            label25.setBounds(35, 240, 80, 20);
            panel3.add(textField4);
            textField4.setBounds(115, 235, 125, 30);

            //---- label26 ----
            label26.setText("\u6dfb\u52a0\u710a\u673a");
            label26.setFont(label26.getFont().deriveFont(label26.getFont().getSize() + 2f));
            panel3.add(label26);
            label26.setBounds(175, 290, 65, label26.getPreferredSize().height);
            panel3.add(textField5);
            textField5.setBounds(115, 325, 125, 30);

            //---- label27 ----
            label27.setText("\u710a\u673a\u540d\u79f0:");
            label27.setFont(label27.getFont().deriveFont(label27.getFont().getSize() + 2f));
            panel3.add(label27);
            label27.setBounds(35, 330, 75, 18);

            //---- label28 ----
            label28.setText("\u710a\u673a\u72b6\u6001:");
            label28.setFont(label28.getFont().deriveFont(label28.getFont().getSize() + 2f));
            panel3.add(label28);
            label28.setBounds(35, 370, 75, 18);
            panel3.add(textField6);
            textField6.setBounds(115, 365, 125, 30);

            //---- button6 ----
            button6.setText("\u786e\u5b9a");
            panel3.add(button6);
            button6.setBounds(new Rectangle(new Point(180, 440), button6.getPreferredSize()));

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
        contentPane.add(panel3);
        panel3.setBounds(340, 100, 295, 480);

        //======== panel4 ========
        {
            panel4.setBackground(new Color(204, 204, 204));
            panel4.setLayout(null);

            //---- label29 ----
            label29.setText("\u710a\u673a\u7ba1\u7406");
            label29.setFont(label29.getFont().deriveFont(label29.getFont().getSize() + 5f));
            panel4.add(label29);
            label29.setBounds(new Rectangle(new Point(100, 10), label29.getPreferredSize()));

            //---- label30 ----
            label30.setText("\u5f53\u524d\u710a\u673a");
            label30.setFont(label30.getFont().deriveFont(label30.getFont().getSize() + 2f));
            panel4.add(label30);
            label30.setBounds(180, 55, 65, 18);

            //---- label31 ----
            label31.setText("\u5f53\u524d\u710a\u673a");
            label31.setFont(label31.getFont().deriveFont(label31.getFont().getSize() + 2f));
            panel4.add(label31);
            label31.setBounds(115, 80, 125, 20);

            //---- label32 ----
            label32.setText("\u710a\u673a\u540d\u79f0\uff1a");
            label32.setFont(label32.getFont().deriveFont(label32.getFont().getSize() + 2f));
            panel4.add(label32);
            label32.setBounds(35, 80, 80, 20);

            //---- label33 ----
            label33.setText("\u710a\u673a\u72b6\u6001\uff1a");
            label33.setFont(label33.getFont().deriveFont(label33.getFont().getSize() + 2f));
            panel4.add(label33);
            label33.setBounds(35, 115, 85, 20);

            //---- label34 ----
            label34.setText("\u5f53\u524d\u72b6\u6001");
            label34.setFont(label34.getFont().deriveFont(label34.getFont().getSize() + 2f));
            panel4.add(label34);
            label34.setBounds(115, 115, 125, 20);

            //---- label35 ----
            label35.setText("\u4fee\u6539\u5f53\u524d\u710a\u673a");
            label35.setFont(label35.getFont().deriveFont(label35.getFont().getSize() + 2f));
            panel4.add(label35);
            label35.setBounds(150, 165, 90, 20);
            panel4.add(comboBox3);
            comboBox3.setBounds(115, 195, 125, comboBox3.getPreferredSize().height);

            //---- label36 ----
            label36.setText("\u710a\u673a\u540d\u79f0\uff1a");
            label36.setFont(label36.getFont().deriveFont(label36.getFont().getSize() + 2f));
            panel4.add(label36);
            label36.setBounds(35, 200, 80, label36.getPreferredSize().height);

            //---- label37 ----
            label37.setText("\u710a\u673a\u72b6\u6001\uff1a");
            label37.setFont(label37.getFont().deriveFont(label37.getFont().getSize() + 2f));
            panel4.add(label37);
            label37.setBounds(35, 240, 80, 20);
            panel4.add(textField7);
            textField7.setBounds(115, 235, 125, 30);

            //---- label38 ----
            label38.setText("\u6dfb\u52a0\u710a\u673a");
            label38.setFont(label38.getFont().deriveFont(label38.getFont().getSize() + 2f));
            panel4.add(label38);
            label38.setBounds(175, 290, 65, label38.getPreferredSize().height);
            panel4.add(textField8);
            textField8.setBounds(115, 325, 125, 30);

            //---- label39 ----
            label39.setText("\u710a\u673a\u540d\u79f0:");
            label39.setFont(label39.getFont().deriveFont(label39.getFont().getSize() + 2f));
            panel4.add(label39);
            label39.setBounds(35, 330, 75, 18);

            //---- label40 ----
            label40.setText("\u710a\u673a\u72b6\u6001:");
            label40.setFont(label40.getFont().deriveFont(label40.getFont().getSize() + 2f));
            panel4.add(label40);
            label40.setBounds(35, 370, 75, 18);
            panel4.add(textField9);
            textField9.setBounds(115, 365, 125, 30);

            //---- button7 ----
            button7.setText("\u786e\u5b9a");
            panel4.add(button7);
            button7.setBounds(new Rectangle(new Point(180, 440), button7.getPreferredSize()));

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
        panel4.setBounds(675, 100, 295, 480);

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
    private JLabel label4;
    private JLabel label5;
    private JPanel panel2;
    private JLabel label1;
    private JLabel label17;
    private JLabel label14;
    private JLabel label12;
    private JLabel label13;
    private JLabel label15;
    private JLabel label16;
    private JComboBox comboBox1;
    private JLabel label6;
    private JLabel label7;
    private JTextField textField3;
    private JLabel label9;
    private JTextField textField1;
    private JLabel label10;
    private JLabel label11;
    private JTextField textField2;
    private JButton button5;
    private JLabel label41;
    private JButton button8;
    private JPanel panel3;
    private JLabel label8;
    private JLabel label18;
    private JLabel label19;
    private JLabel label20;
    private JLabel label21;
    private JLabel label22;
    private JLabel label23;
    private JComboBox comboBox2;
    private JLabel label24;
    private JLabel label25;
    private JTextField textField4;
    private JLabel label26;
    private JTextField textField5;
    private JLabel label27;
    private JLabel label28;
    private JTextField textField6;
    private JButton button6;
    private JPanel panel4;
    private JLabel label29;
    private JLabel label30;
    private JLabel label31;
    private JLabel label32;
    private JLabel label33;
    private JLabel label34;
    private JLabel label35;
    private JComboBox comboBox3;
    private JLabel label36;
    private JLabel label37;
    private JTextField textField7;
    private JLabel label38;
    private JTextField textField8;
    private JLabel label39;
    private JLabel label40;
    private JTextField textField9;
    private JButton button7;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
