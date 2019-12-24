/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

//TODO:
//标记时间：2019/12/24 17:38  预解决时间：
//1. 两个表之间，修改后的联动效果

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

    private List<Map<String, Object>> machine_now_mapsList; //当前焊机表
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
        updateComboBox1();
        displayNowMachine();
        updateComboBox2();
    }

    /**
     * 焊机管理
     */
    //当前焊机： 获得焊机下拉框内容
    private void updateComboBox1() {
        comboBox1.removeAllItems(); //清空原数据
        machine_setting_mapsList = SettingUtil.getData("machine_setting"); //获取焊机表内容，更新内容
        if (machine_setting_mapsList == null || machine_setting_mapsList.size() == 0) {
            return;
        }
        //更新 产品选择 下拉框内容
        for (Map<String, Object> map : machine_setting_mapsList) {
            comboBox1.addItem(map.get("machine_name")); //下拉框添加内容
        }
        comboBox1.setSelectedIndex(-1);
        label15.setText(""); //清空焊机状态（因为addItem会触发一个change事件）
    }

    //当前焊机： 显示焊机名称与状态
    private void displayNowMachine() {
        String[] nowMachine = SettingUtil.getNowMachine();
        String machineName = nowMachine[0];
        String machineStatus = nowMachine[1];

        for (int i = 0; i < comboBox1.getModel().getSize(); i++) {
            if (comboBox1.getModel().getElementAt(i).equals(machineName)) {
                comboBox1.setSelectedIndex(i);
                break;
            }
        }
        label15.setText(machineStatus);
    }

    //当前焊机： 焊机状态跟随焊机名称变化
    private void comboBox1ItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String machine_status = "出错"; //初始值

            for (Map<String, Object> map : machine_setting_mapsList) {
                if (map.get("machine_name").equals(comboBox1.getSelectedItem())) {
                    machine_status = (String) map.get("machine_status"); //找到对应的状态的，修改值
                }
            }
            label15.setText(machine_status);
        }
    }

    //当前焊机： 确定
    private void button9ActionPerformed(ActionEvent e) {
        String machine_name = (String) comboBox1.getSelectedItem();
        String machine_status = label15.getText();
        boolean result = SettingUtil.changeNowMachine(machine_name, machine_status); //向machine_now表中提交更新信息
        if (result) {
            JOptionPane.showMessageDialog(this, "修改成功！");
        } else {
            JOptionPane.showMessageDialog(this, "修改失败!", "提示",JOptionPane.WARNING_MESSAGE);
        }
    }

    //修改焊机： 获得焊机下拉框内容
    private void updateComboBox2() {
        comboBox2.removeAllItems(); //清空原数据
        machine_setting_mapsList = SettingUtil.getData("machine_setting"); //获取焊机表内容，更新内容
        if (machine_setting_mapsList == null || machine_setting_mapsList.size() == 0) {
            return;
        }
        //更新 产品选择 下拉框内容
        for (Map<String, Object> map : machine_setting_mapsList) {
            comboBox2.addItem((String) map.get("machine_name")); //下拉框添加内容
        }
        comboBox2.setSelectedIndex(-1);

        textField3.setText("");// 清空焊机状态（因为addItem会触发一个change事件）
    }

    //修改焊机：焊机状态跟随焊机名称变化
    private void comboBox2ItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String machine_status = "出错"; //初始值

            for (Map<String, Object> map : machine_setting_mapsList) {
                if (map.get("machine_name").equals(comboBox2.getSelectedItem())) {
                    machine_status = (String) map.get("machine_status"); //找到对应的状态的，修改值
                }
            }
            textField3.setText(machine_status);
        }
    }

    //修改焊机： 确定
    private void button8ActionPerformed(ActionEvent e) {
        if (textField3.getText().equals("") || textField3.getText() == null) {
            JOptionPane.showMessageDialog(this, "请输入焊机状态!", "提示",JOptionPane.WARNING_MESSAGE);
        } else if (comboBox2.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(this, "请选择焊机名称!", "提示",JOptionPane.WARNING_MESSAGE);
        } else {
            String machine_name = (String) comboBox2.getSelectedItem();
            String machine_status = textField3.getText();
            boolean result = SettingUtil.updateMachine(machine_name, machine_status);
            if (result) {
                JOptionPane.showMessageDialog(this, "修改成功！");
                updateComboBox2(); //修改成功后，刷新一下 修改焊机
                //修改完成后，更新当前表
                SettingUtil.updateMachineStatus(machine_name, machine_status);
            } else {
                JOptionPane.showMessageDialog(this, "修改失败!", "提示",JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //添加焊机： 确定
    private void button5ActionPerformed(ActionEvent e) {
        String machine_name = textField1.getText();
        String machine_status = textField2.getText();

        boolean result = SettingUtil.insertNewMachine(machine_name, machine_status);
        if (result) {
            JOptionPane.showMessageDialog(this, "添加成功！");
            updateComboBox2();//添加成功后，刷新一下 修改焊机
            textField1.setText(""); //清空内容
            textField2.setText(""); //清空内容
        } else {
            JOptionPane.showMessageDialog(this, "添加失败!", "提示",JOptionPane.WARNING_MESSAGE);
        }

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
        button9 = new JButton();
        comboBox2 = new JComboBox();
        panel3 = new JPanel();
        label8 = new JLabel();
        tabbedPane1 = new JTabbedPane();
        panel5 = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        button6 = new JButton();
        button7 = new JButton();
        button10 = new JButton();
        button11 = new JButton();
        panel6 = new JPanel();
        panel4 = new JPanel();
        label40 = new JLabel();

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
            label1.setBounds(new Rectangle(new Point(105, 5), label1.getPreferredSize()));

            //---- label17 ----
            label17.setText("\u5f53\u524d\u710a\u673a");
            label17.setFont(label17.getFont().deriveFont(label17.getFont().getSize() + 2f));
            panel2.add(label17);
            label17.setBounds(180, 35, 65, 18);

            //---- label12 ----
            label12.setText("\u710a\u673a\u540d\u79f0\uff1a");
            label12.setFont(label12.getFont().deriveFont(label12.getFont().getSize() + 2f));
            panel2.add(label12);
            label12.setBounds(35, 60, 80, 20);

            //---- label13 ----
            label13.setText("\u710a\u673a\u72b6\u6001\uff1a");
            label13.setFont(label13.getFont().deriveFont(label13.getFont().getSize() + 2f));
            panel2.add(label13);
            label13.setBounds(35, 95, 85, 20);

            //---- label15 ----
            label15.setText("\u5f53\u524d\u72b6\u6001");
            label15.setFont(label15.getFont().deriveFont(label15.getFont().getSize() + 2f));
            label15.setForeground(new Color(0, 153, 204));
            panel2.add(label15);
            label15.setBounds(115, 95, 125, 20);

            //---- label16 ----
            label16.setText("\u4fee\u6539\u710a\u673a");
            label16.setFont(label16.getFont().deriveFont(label16.getFont().getSize() + 2f));
            panel2.add(label16);
            label16.setBounds(180, 170, 65, 20);

            //---- comboBox1 ----
            comboBox1.setSelectedIndex(-1);
            comboBox1.addItemListener(e -> comboBox1ItemStateChanged(e));
            panel2.add(comboBox1);
            comboBox1.setBounds(115, 60, 125, comboBox1.getPreferredSize().height);

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
            button5.addActionListener(e -> button5ActionPerformed(e));
            panel2.add(button5);
            button5.setBounds(175, 425, 63, button5.getPreferredSize().height);

            //---- label41 ----
            label41.setText("Tips: \u4fee\u6539\u540e\uff0c\u5207\u6362\u4e00\u4e0b\u9875\u9762\u5373\u53ef\u6b63\u5e38\u663e\u793a");
            label41.setFont(label41.getFont().deriveFont(label41.getFont().getSize() - 1f));
            panel2.add(label41);
            label41.setBounds(0, 460, 220, 20);

            //---- button8 ----
            button8.setText("\u786e\u5b9a");
            button8.addActionListener(e -> button8ActionPerformed(e));
            panel2.add(button8);
            button8.setBounds(175, 270, 63, 28);

            //---- button9 ----
            button9.setText("\u786e\u5b9a");
            button9.addActionListener(e -> button9ActionPerformed(e));
            panel2.add(button9);
            button9.setBounds(175, 120, 63, 28);

            //---- comboBox2 ----
            comboBox2.setSelectedIndex(-1);
            comboBox2.addItemListener(e -> comboBox2ItemStateChanged(e));
            panel2.add(comboBox2);
            comboBox2.setBounds(115, 200, 125, 27);

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
            label8.setText("\u7528\u6237\u4fe1\u606f\u7ba1\u7406");
            label8.setFont(label8.getFont().deriveFont(label8.getFont().getSize() + 5f));
            panel3.add(label8);
            label8.setBounds(new Rectangle(new Point(100, 10), label8.getPreferredSize()));

            //======== tabbedPane1 ========
            {

                //======== panel5 ========
                {
                    panel5.setLayout(null);

                    //======== scrollPane2 ========
                    {

                        //---- table2 ----
                        table2.setRowHeight(20);
                        table2.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null},
                                {null, null},
                            },
                            new String[] {
                                "\u7528\u6237\u540d", "\u5bc6\u7801"
                            }
                        ));
                        table2.setEnabled(false);
                        scrollPane2.setViewportView(table2);
                    }
                    panel5.add(scrollPane2);
                    scrollPane2.setBounds(0, 50, 290, 355);

                    //---- button6 ----
                    button6.setText("\u4fee\u6539");
                    panel5.add(button6);
                    button6.setBounds(5, 10, 65, button6.getPreferredSize().height);

                    //---- button7 ----
                    button7.setText("\u5220\u9664");
                    panel5.add(button7);
                    button7.setBounds(75, 10, 65, 28);

                    //---- button10 ----
                    button10.setText("\u6dfb\u52a0");
                    panel5.add(button10);
                    button10.setBounds(145, 10, 65, 28);

                    //---- button11 ----
                    button11.setText("\u786e\u5b9a");
                    panel5.add(button11);
                    button11.setBounds(220, 10, 65, 28);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panel5.getComponentCount(); i++) {
                            Rectangle bounds = panel5.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panel5.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panel5.setMinimumSize(preferredSize);
                        panel5.setPreferredSize(preferredSize);
                    }
                }
                tabbedPane1.addTab("\u666e\u901a\u7528\u6237", panel5);

                //======== panel6 ========
                {
                    panel6.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panel6.getComponentCount(); i++) {
                            Rectangle bounds = panel6.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panel6.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panel6.setMinimumSize(preferredSize);
                        panel6.setPreferredSize(preferredSize);
                    }
                }
                tabbedPane1.addTab("\u7ba1\u7406\u5458\u7528\u6237", panel6);
            }
            panel3.add(tabbedPane1);
            tabbedPane1.setBounds(0, 45, 295, 435);

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

            //---- label40 ----
            label40.setFont(label40.getFont().deriveFont(label40.getFont().getSize() + 2f));
            panel4.add(label40);
            label40.setBounds(35, 370, 75, 18);

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
    private JButton button9;
    private JComboBox comboBox2;
    private JPanel panel3;
    private JLabel label8;
    private JTabbedPane tabbedPane1;
    private JPanel panel5;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton button6;
    private JButton button7;
    private JButton button10;
    private JButton button11;
    private JPanel panel6;
    private JPanel panel4;
    private JLabel label40;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
