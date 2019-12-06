/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import java.awt.event.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
import com.lichang.utils.ExpertSystemUtil.ComboBoxUtil;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.RealTimeMonitoringUtil.ChangePassword;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

//TODO: 待解决问题
//标记时间：2019/12/4 17:32  预解决时间：
//1. 下拉框的数据库绑定
//2. 重设、保存 按钮功能的实现
//3. 生成焊接参数的触发
//4. 资料库
//5. 制定焊接规则
////6. 自定义焊接参数的实现


/**
 * @author unknown
 */
public class ExpertSystem extends JFrame {
    private static Logger log = LoggerUtil.getLogger(); // 日志

    // 自定义的变量
    private String username; // 当前用户名
    private boolean adminFlag; // 用户类型
    private JDialog jDialog2; // 密码修改
    private JPanel changePasswordPanel; // 密码修改
    private JLabel oldValidationTip; // 旧密码 验证提示
    private boolean oldChangeFlag; //判断旧密码是否通过验证


    //无参构造
    public ExpertSystem() {
        log.debug("无参构造");

        //TEST: 测试用，直接打开该页面时，暂时给username和flag一个值
        //标记时间：2019/11/21 15:56  预解决时间：
        username = "admin";
        adminFlag = true;

        initComponents();
        initComboBox1_2();
        initComboBox3();
        initComboBox4();
        initComboBox5();
        initComboBox6();
        initComboBox7();
        initComboBox8();
        initComboBox9();
        initComboBox10();

        setVisible(true);
    }

    //接收登录账户信息
    public ExpertSystem(String username, Boolean adminFlag) {
        log.debug("有参构造");
        this.username = username;
        this.adminFlag = adminFlag;

        initComponents();
        initComboBox1_2();
        initComboBox3();
        initComboBox4();
        initComboBox5();
        initComboBox6();
        initComboBox7();
        initComboBox8();
        initComboBox9();
        initComboBox10();

        label3Bind(username); //显示当前用户信息

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
     * 历史统计与查询 按钮： 点击跳转
     * @param e
     */
    private void button2ActionPerformed(ActionEvent e) {
        new HistoricalStatistics(username, adminFlag);
        this.dispose();
    }

    /**
     * ComboBox 下拉框连接数据库
     */
    //母材选取
    private void initComboBox1_2() {
        /*
            调用数据库
         */
        List<Map<String, Object>> expert_base_metal_mapsList = ComboBoxUtil.getData("expert_base_metal");
        for (Map<String, Object> expert_base_metal_map : expert_base_metal_mapsList) {
            String name = (String) expert_base_metal_map.get("name");
            comboBox1.addItem(name);
            comboBox2.addItem(name);
        }

        /*
            自设
         */
        //TEST: 暂时用于自定义的内容
        //标记时间：2019/12/5 15:45  预解决时间：
//        String[] items = {
//                "碳钢碳锰钢",
//                "细晶粒钢",
//                "高强细晶粒结构钢",
//                "热强钢(非合金)",
//                "热强钢(合金)",
//                "不锈钢+耐热钢",
//                "铸铁球墨铸铁",
//                "铜和铜合金",
//                "镍和镍合金",
//                "铝材料",
//                "钛和钛合金"
//        };
//        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(items);
//        comboBox1.setModel(boxModel);
//        comboBox2.setModel(boxModel);

        comboBox1.setSelectedIndex(-1);
        comboBox2.setSelectedIndex(-1);
    }

    //焊接方法
    private void initComboBox3() {
        /*
            调用数据库
         */
        List<Map<String, Object>> expert_weld_method_mapsList = ComboBoxUtil.getData("expert_weld_method");
        for (Map<String, Object> expert_weld_method_map : expert_weld_method_mapsList) {
            String name = (String) expert_weld_method_map.get("name");
            comboBox3.addItem(name);
        }

        /*
            自设
         */
        //TEST: 暂时用于自定义的内容
        //标记时间：2019/12/5 15:40  预解决时间：
        String[] items = {
                "焊条电弧焊（手工电弧焊）",
                "埋弧电弧焊",
                "氩弧焊",
                "CO2气保焊",
                "气焊",
                "堆焊",
                "等离子焊",
                "电渣焊",
                "激光焊",
                "电阻焊",
                "摩擦焊",
                "冷压焊",
                "锻焊",
                "其他"};
        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(items);
        comboBox3.setModel(boxModel);

        comboBox3.setSelectedIndex(-1);
    }

    //焊接材料
    private void initComboBox4() {
        /*
            调用数据库
         */
        List<Map<String, Object>> expert_weld_metal_mapsList = ComboBoxUtil.getData("expert_weld_metal");
        for (Map<String, Object> expert_weld_metal_map : expert_weld_metal_mapsList) {
            String name = (String) expert_weld_metal_map.get("name");
            comboBox4.addItem(name);
        }

        /*
            自设
         */
        //TEST: 暂时用于自定义的内容
        //标记时间：2019/12/5 15:40  预解决时间：
//        String[] items = {
//                "提示：焊丝用于气保焊",
//                "提示：焊条用于手工焊",
//                "焊丝：YM-80A",
//                "焊丝：SM-70",
//                "焊丝：ER50-6",
//                "焊条：L-80SN",
//                "焊条：J506",
//                "其他"};
//        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(items);
//        comboBox4.setModel(boxModel);

        comboBox4.setSelectedIndex(-1);
    }

    //辅材
    private void initComboBox5() {
        /*
            调用数据库
         */
        List<Map<String, Object>> expert_auxiliary_materials_mapsList = ComboBoxUtil.getData("expert_auxiliary_materials");
        for (Map<String, Object> expert_auxiliary_materials_map : expert_auxiliary_materials_mapsList) {
            String name = (String) expert_auxiliary_materials_map.get("name");
            comboBox5.addItem(name);
        }

        /*
            自设
         */
        //TEST: 暂时用于自定义的内容
        //标记时间：2019/12/5 15:40  预解决时间：
//        String[] items = {
//                "CO2",
//                "其他"};
//        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(items);
//        comboBox5.setModel(boxModel);

        comboBox5.setSelectedIndex(-1);
    }

    //工件厚度
    private void initComboBox6() {
        /*
            调用数据库
         */
        List<Map<String, Object>> expert_workpiece_thickness_mapsList = ComboBoxUtil.getData("expert_workpiece_thickness");
        for (Map<String, Object> expert_workpiece_thickness_map : expert_workpiece_thickness_mapsList) {
            String name = (String) expert_workpiece_thickness_map.get("name");
            comboBox6.addItem(name);
        }

        /*
            自设
         */
        //TEST: 暂时用于自定义的内容
        //标记时间：2019/12/5 15:40  预解决时间：
//        String[] items = {
//                "车前架三角撑",
//                "其他"};
//        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(items);
//        comboBox6.setModel(boxModel);

        comboBox6.setSelectedIndex(-1);
    }

    //焊接位置
    private void initComboBox7() {
        /*
            调用数据库
         */
        List<Map<String, Object>> expert_weld_joint_mapsList = ComboBoxUtil.getData("expert_weld_joint");
        for (Map<String, Object> expert_weld_joint_map : expert_weld_joint_mapsList) {
            String name = (String) expert_weld_joint_map.get("welding_position");
            comboBox7.addItem(name);
        }

        /*
            自设
         */
        //TEST: 暂时用于自定义的内容
        //标记时间：2019/12/5 15:40  预解决时间：
//        String[] items = {
//                "PA 平焊",
//                "PB 平角焊",
//                "PC 横焊",
//                "PD 仰角焊",
//                "PE 仰焊",
//                "PF 向上立焊",
//                "PG 向下立焊",
//                "其他"
//                };
//        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(items);
//        comboBox7.setModel(boxModel);

        comboBox7.setSelectedIndex(-1);
    }

    //坡口
    private void initComboBox8() {
        /*
            调用数据库
         */
        List<Map<String, Object>> expert_weld_joint_mapsList = ComboBoxUtil.getData("expert_weld_joint");
        for (Map<String, Object> expert_weld_joint_map : expert_weld_joint_mapsList) {
            String name = (String) expert_weld_joint_map.get("groove_form");
            comboBox8.addItem(name);
        }

        /*
            自设
         */
        //TEST: 暂时用于自定义的内容
        //标记时间：2019/12/5 15:40  预解决时间：
//        String[] items = {
//                "I 形",
//                "V 形",
//                "V 形",
//                "U 形",
//                "J 形",
//                "组合",
//                "其他"
//                };
//        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(items);
//        comboBox8.setModel(boxModel);

        comboBox8.setSelectedIndex(-1);
    }

    //接头
    private void initComboBox9() {
        /*
            调用数据库
         */
        List<Map<String, Object>> expert_weld_joint_mapsList = ComboBoxUtil.getData("expert_weld_joint");
        for (Map<String, Object> expert_weld_joint_map : expert_weld_joint_mapsList) {
            String name = (String) expert_weld_joint_map.get("joint_form");
            comboBox9.addItem(name);
        }

        /*
            自设
         */
        //TEST: 暂时用于自定义的内容
        //标记时间：2019/12/5 15:40  预解决时间：
//        String[] items = {
//                "对接",
//                "角接",
//                "T字",
//                "搭接",
//                "其他"
//                };
//        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(items);
//        comboBox9.setModel(boxModel);

        comboBox9.setSelectedIndex(-1);
    }

    //热工艺
    private void initComboBox10() {
        /*
            调用数据库
         */
        List<Map<String, Object>> expert_thermal_process_mapsList = ComboBoxUtil.getData("expert_thermal_process");
        for (Map<String, Object> expert_thermal_process_map : expert_thermal_process_mapsList) {
            String name = (String) expert_thermal_process_map.get("heat_treatment_type");
            comboBox10.addItem(name);
        }

        /*
            自设
         */
        //TEST: 暂时用于自定义的内容
        //标记时间：2019/12/5 15:40  预解决时间：
//        String[] items = {
//                "其他"
//                };
//        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(items);
//        comboBox10.setModel(boxModel);

        comboBox10.setSelectedIndex(-1);
    }


    //TEST: 下拉框连接数据库 测试按钮
    //标记时间：2019/12/5 14:29  预解决时间：
    private void button8ActionPerformed(ActionEvent e) {

    }

    /**
     *  按钮
     */
    //自定义焊接参数
    private void button9ActionPerformed(ActionEvent e) {
        //使焊接参数的下拉框和文本域可选中并编辑
        comboBox12.setEditable(true);
        comboBox13.setEditable(true);
        comboBox14.setEditable(true);
        comboBox15.setEditable(true);
        comboBox16.setEditable(true);

        comboBox12.setEnabled(true);
        comboBox13.setEnabled(true);
        comboBox14.setEnabled(true);
        comboBox15.setEnabled(true);
        comboBox16.setEnabled(true);

        textField1.setEditable(true);
        textField2.setEditable(true);
        textField3.setEditable(true);
        textField4.setEditable(true);
        textField5.setEditable(true);
    }

    //生成焊接参数
    private void button7ActionPerformed(ActionEvent e) {
        // 流程填写完整 提示
        if (comboBox1.getSelectedIndex() == -1
                || comboBox2.getSelectedIndex() == -1
                || comboBox3.getSelectedIndex() == -1
                || comboBox4.getSelectedIndex() == -1
                || comboBox5.getSelectedIndex() == -1
                || comboBox6.getSelectedIndex() == -1
                || comboBox7.getSelectedIndex() == -1
                || comboBox8.getSelectedIndex() == -1
                || comboBox9.getSelectedIndex() == -1
                || comboBox10.getSelectedIndex() == -1
        ) {
            JOptionPane.showMessageDialog(this, "请将流程填写完整！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        comboBox12.setEnabled(true);
        comboBox13.setEnabled(true);
        comboBox14.setEnabled(true);
        comboBox15.setEnabled(true);
        comboBox16.setEnabled(true);

        textField1.setEditable(true);
        textField2.setEditable(true);
        textField3.setEditable(true);
        textField4.setEditable(true);
        textField5.setEditable(true);
    }

    /**
     * 规则 触发： 修改下拉框可显示内容
     */
    //母材选取A
    private void comboBox1ItemStateChanged(ItemEvent e) {
        // 下拉框触发事件有两个，Selected 和 deSelected（即选中和未被选中）。 所以规定触发事件为Selected
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String item = (String) comboBox1.getSelectedItem();
            switch (item) {
                case "母材1":

                    break;
                case "母材2":
                    System.out.println(2);
                    break;
                case "母材3":
                    System.out.println(3);
                    break;
                default:
            }
        }


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
        tabbedPane2 = new JTabbedPane();
        panel4 = new JPanel();
        label1 = new JLabel();
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
        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox();
        comboBox3 = new JComboBox();
        comboBox4 = new JComboBox();
        comboBox5 = new JComboBox();
        comboBox6 = new JComboBox();
        comboBox7 = new JComboBox();
        comboBox8 = new JComboBox();
        comboBox9 = new JComboBox();
        comboBox10 = new JComboBox();
        comboBox11 = new JComboBox();
        label14 = new JLabel();
        label15 = new JLabel();
        label16 = new JLabel();
        label17 = new JLabel();
        label18 = new JLabel();
        label19 = new JLabel();
        comboBox12 = new JComboBox();
        comboBox13 = new JComboBox();
        comboBox14 = new JComboBox();
        comboBox15 = new JComboBox();
        comboBox16 = new JComboBox();
        button5 = new JButton();
        button6 = new JButton();
        label20 = new JLabel();
        label21 = new JLabel();
        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        textField4 = new JTextField();
        textField5 = new JTextField();
        button7 = new JButton();
        button8 = new JButton();
        button9 = new JButton();
        panel2 = new JPanel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u754c\u9762");
        setAlwaysOnTop(true);
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
        button2.setBounds(295, 60, 120, 30);

        //---- button3 ----
        button3.setText("\u4e13\u5bb6\u7cfb\u7edf");
        contentPane.add(button3);
        button3.setBounds(525, 60, 120, 30);

        //---- button4 ----
        button4.setText("\u7ba1\u7406\u4e0e\u8bbe\u7f6e");
        contentPane.add(button4);
        button4.setBounds(765, 60, 120, 30);
        contentPane.add(separator4);
        separator4.setBounds(5, 90, 920, 10);

        //======== tabbedPane2 ========
        {

            //======== panel4 ========
            {
                panel4.setLayout(null);

                //---- label1 ----
                label1.setText("\u6bcd\u6750\u9009\u53d6");
                label1.setBackground(new Color(204, 204, 204));
                label1.setOpaque(true);
                label1.setHorizontalTextPosition(SwingConstants.CENTER);
                label1.setHorizontalAlignment(SwingConstants.CENTER);
                label1.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 1f));
                panel4.add(label1);
                label1.setBounds(25, 30, 85, 30);

                //---- label4 ----
                label4.setText("\u710a\u63a5\u65b9\u6cd5");
                label4.setBackground(new Color(204, 204, 204));
                label4.setOpaque(true);
                label4.setHorizontalTextPosition(SwingConstants.CENTER);
                label4.setHorizontalAlignment(SwingConstants.CENTER);
                label4.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label4.setFont(label4.getFont().deriveFont(label4.getFont().getSize() + 1f));
                panel4.add(label4);
                label4.setBounds(25, 75, 85, 30);

                //---- label5 ----
                label5.setText("\u710a\u63a5\u6750\u6599");
                label5.setBackground(new Color(204, 204, 204));
                label5.setOpaque(true);
                label5.setHorizontalTextPosition(SwingConstants.CENTER);
                label5.setHorizontalAlignment(SwingConstants.CENTER);
                label5.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label5.setFont(label5.getFont().deriveFont(label5.getFont().getSize() + 1f));
                panel4.add(label5);
                label5.setBounds(25, 120, 85, 30);

                //---- label6 ----
                label6.setText("\u8f85\u6750");
                label6.setBackground(new Color(204, 204, 204));
                label6.setOpaque(true);
                label6.setHorizontalTextPosition(SwingConstants.CENTER);
                label6.setHorizontalAlignment(SwingConstants.CENTER);
                label6.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label6.setFont(label6.getFont().deriveFont(label6.getFont().getSize() + 1f));
                panel4.add(label6);
                label6.setBounds(25, 165, 85, 30);

                //---- label7 ----
                label7.setText("\u5de5\u4ef6\u539a\u5ea6");
                label7.setBackground(new Color(204, 204, 204));
                label7.setOpaque(true);
                label7.setHorizontalTextPosition(SwingConstants.CENTER);
                label7.setHorizontalAlignment(SwingConstants.CENTER);
                label7.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label7.setFont(label7.getFont().deriveFont(label7.getFont().getSize() + 1f));
                panel4.add(label7);
                label7.setBounds(25, 210, 85, 30);

                //---- label8 ----
                label8.setText("\u710a\u63a5\u4f4d\u7f6e");
                label8.setBackground(new Color(204, 204, 204));
                label8.setOpaque(true);
                label8.setHorizontalTextPosition(SwingConstants.CENTER);
                label8.setHorizontalAlignment(SwingConstants.CENTER);
                label8.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label8.setFont(label8.getFont().deriveFont(label8.getFont().getSize() + 1f));
                panel4.add(label8);
                label8.setBounds(25, 255, 85, 30);

                //---- label9 ----
                label9.setText("\u63a5\u5934\u3001\u5761\u53e3");
                label9.setBackground(new Color(204, 204, 204));
                label9.setOpaque(true);
                label9.setHorizontalTextPosition(SwingConstants.CENTER);
                label9.setHorizontalAlignment(SwingConstants.CENTER);
                label9.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label9.setFont(label9.getFont().deriveFont(label9.getFont().getSize() + 1f));
                panel4.add(label9);
                label9.setBounds(25, 300, 85, 30);

                //---- label10 ----
                label10.setText("\u70ed\u5de5\u827a");
                label10.setBackground(new Color(204, 204, 204));
                label10.setOpaque(true);
                label10.setHorizontalTextPosition(SwingConstants.CENTER);
                label10.setHorizontalAlignment(SwingConstants.CENTER);
                label10.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label10.setFont(label10.getFont().deriveFont(label10.getFont().getSize() + 1f));
                panel4.add(label10);
                label10.setBounds(25, 345, 85, 30);

                //---- label11 ----
                label11.setText("\u5176\u4ed6");
                label11.setBackground(new Color(204, 204, 204));
                label11.setOpaque(true);
                label11.setHorizontalTextPosition(SwingConstants.CENTER);
                label11.setHorizontalAlignment(SwingConstants.CENTER);
                label11.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label11.setFont(label11.getFont().deriveFont(label11.getFont().getSize() + 1f));
                panel4.add(label11);
                label11.setBounds(25, 390, 85, 30);

                //---- label12 ----
                label12.setText("A");
                panel4.add(label12);
                label12.setBounds(120, 30, 10, 30);

                //---- label13 ----
                label13.setText("B");
                panel4.add(label13);
                label13.setBounds(270, 30, 10, 30);

                //---- comboBox1 ----
                comboBox1.setSelectedIndex(-1);
                comboBox1.addItemListener(e -> comboBox1ItemStateChanged(e));
                panel4.add(comboBox1);
                comboBox1.setBounds(135, 30, 120, 30);

                //---- comboBox2 ----
                comboBox2.setSelectedIndex(-1);
                panel4.add(comboBox2);
                comboBox2.setBounds(285, 30, 125, 30);

                //---- comboBox3 ----
                comboBox3.setSelectedIndex(-1);
                panel4.add(comboBox3);
                comboBox3.setBounds(135, 75, 275, 30);

                //---- comboBox4 ----
                comboBox4.setSelectedIndex(-1);
                panel4.add(comboBox4);
                comboBox4.setBounds(135, 120, 275, 30);

                //---- comboBox5 ----
                comboBox5.setEditable(true);
                comboBox5.setSelectedIndex(-1);
                panel4.add(comboBox5);
                comboBox5.setBounds(135, 165, 275, 30);

                //---- comboBox6 ----
                comboBox6.setEditable(true);
                comboBox6.setSelectedIndex(-1);
                panel4.add(comboBox6);
                comboBox6.setBounds(135, 210, 275, 30);

                //---- comboBox7 ----
                comboBox7.setSelectedIndex(-1);
                panel4.add(comboBox7);
                comboBox7.setBounds(135, 255, 275, 30);

                //---- comboBox8 ----
                comboBox8.setSelectedIndex(-1);
                panel4.add(comboBox8);
                comboBox8.setBounds(285, 300, 125, 30);

                //---- comboBox9 ----
                comboBox9.setSelectedIndex(-1);
                panel4.add(comboBox9);
                comboBox9.setBounds(135, 300, 125, 30);

                //---- comboBox10 ----
                comboBox10.setEditable(true);
                comboBox10.setSelectedIndex(-1);
                panel4.add(comboBox10);
                comboBox10.setBounds(135, 345, 275, 30);

                //---- comboBox11 ----
                comboBox11.setEditable(true);
                comboBox11.setSelectedIndex(-1);
                panel4.add(comboBox11);
                comboBox11.setBounds(135, 390, 275, 30);

                //---- label14 ----
                label14.setText("\u710a\u63a5\u53c2\u6570");
                label14.setBackground(new Color(204, 204, 204));
                label14.setOpaque(true);
                label14.setHorizontalTextPosition(SwingConstants.CENTER);
                label14.setHorizontalAlignment(SwingConstants.CENTER);
                label14.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label14.setFont(label14.getFont().deriveFont(label14.getFont().getSize() + 1f));
                panel4.add(label14);
                label14.setBounds(520, 30, 410, 30);

                //---- label15 ----
                label15.setText("\u710a\u63a5\u7535\u6d41");
                label15.setBackground(new Color(204, 204, 204));
                label15.setOpaque(true);
                label15.setHorizontalTextPosition(SwingConstants.CENTER);
                label15.setHorizontalAlignment(SwingConstants.CENTER);
                label15.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label15.setFont(label15.getFont().deriveFont(label15.getFont().getSize() + 1f));
                panel4.add(label15);
                label15.setBounds(520, 120, 85, 30);

                //---- label16 ----
                label16.setText("\u710a\u63a5\u901f\u5ea6");
                label16.setBackground(new Color(204, 204, 204));
                label16.setOpaque(true);
                label16.setHorizontalTextPosition(SwingConstants.CENTER);
                label16.setHorizontalAlignment(SwingConstants.CENTER);
                label16.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label16.setFont(label16.getFont().deriveFont(label16.getFont().getSize() + 1f));
                panel4.add(label16);
                label16.setBounds(520, 210, 85, 30);

                //---- label17 ----
                label17.setText("\u710a\u63a5\u7535\u538b");
                label17.setBackground(new Color(204, 204, 204));
                label17.setOpaque(true);
                label17.setHorizontalTextPosition(SwingConstants.CENTER);
                label17.setHorizontalAlignment(SwingConstants.CENTER);
                label17.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label17.setFont(label17.getFont().deriveFont(label17.getFont().getSize() + 1f));
                panel4.add(label17);
                label17.setBounds(520, 165, 85, 30);

                //---- label18 ----
                label18.setText("\u5e72\u4f38\u51fa\u91cf");
                label18.setBackground(new Color(204, 204, 204));
                label18.setOpaque(true);
                label18.setHorizontalTextPosition(SwingConstants.CENTER);
                label18.setHorizontalAlignment(SwingConstants.CENTER);
                label18.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label18.setFont(label18.getFont().deriveFont(label18.getFont().getSize() + 1f));
                panel4.add(label18);
                label18.setBounds(520, 255, 85, 30);

                //---- label19 ----
                label19.setText("\u5176\u5b83");
                label19.setBackground(new Color(204, 204, 204));
                label19.setOpaque(true);
                label19.setHorizontalTextPosition(SwingConstants.CENTER);
                label19.setHorizontalAlignment(SwingConstants.CENTER);
                label19.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label19.setFont(label19.getFont().deriveFont(label19.getFont().getSize() + 1f));
                panel4.add(label19);
                label19.setBounds(520, 300, 85, 30);

                //---- comboBox12 ----
                comboBox12.setSelectedIndex(-1);
                comboBox12.setEnabled(false);
                panel4.add(comboBox12);
                comboBox12.setBounds(635, 120, 140, 30);

                //---- comboBox13 ----
                comboBox13.setSelectedIndex(-1);
                comboBox13.setEnabled(false);
                panel4.add(comboBox13);
                comboBox13.setBounds(635, 165, 140, 30);

                //---- comboBox14 ----
                comboBox14.setSelectedIndex(-1);
                comboBox14.setEnabled(false);
                panel4.add(comboBox14);
                comboBox14.setBounds(635, 210, 140, 30);

                //---- comboBox15 ----
                comboBox15.setSelectedIndex(-1);
                comboBox15.setEnabled(false);
                panel4.add(comboBox15);
                comboBox15.setBounds(635, 255, 140, 30);

                //---- comboBox16 ----
                comboBox16.setSelectedIndex(-1);
                comboBox16.setEnabled(false);
                panel4.add(comboBox16);
                comboBox16.setBounds(635, 300, 140, 30);

                //---- button5 ----
                button5.setText("\u4fdd\u5b58");
                panel4.add(button5);
                button5.setBounds(865, 390, 65, 30);

                //---- button6 ----
                button6.setText("\u91cd\u8bbe");
                panel4.add(button6);
                button6.setBounds(865, 355, 65, 30);

                //---- label20 ----
                label20.setText("\u5efa\u8bae\u503c");
                label20.setBackground(new Color(204, 204, 204));
                label20.setOpaque(true);
                label20.setHorizontalTextPosition(SwingConstants.CENTER);
                label20.setHorizontalAlignment(SwingConstants.CENTER);
                label20.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label20.setFont(label20.getFont().deriveFont(label20.getFont().getSize() + 1f));
                panel4.add(label20);
                label20.setBounds(635, 75, 140, 30);

                //---- label21 ----
                label21.setText("\u8c03\u6574\u540e\u5b9e\u9645\u503c");
                label21.setBackground(new Color(204, 204, 204));
                label21.setOpaque(true);
                label21.setHorizontalTextPosition(SwingConstants.CENTER);
                label21.setHorizontalAlignment(SwingConstants.CENTER);
                label21.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                label21.setFont(label21.getFont().deriveFont(label21.getFont().getSize() + 1f));
                panel4.add(label21);
                label21.setBounds(800, 75, 130, 30);

                //---- textField1 ----
                textField1.setEditable(false);
                panel4.add(textField1);
                textField1.setBounds(800, 120, 130, 30);

                //---- textField2 ----
                textField2.setEditable(false);
                panel4.add(textField2);
                textField2.setBounds(800, 165, 130, 30);

                //---- textField3 ----
                textField3.setEditable(false);
                panel4.add(textField3);
                textField3.setBounds(800, 210, 130, 30);

                //---- textField4 ----
                textField4.setEditable(false);
                panel4.add(textField4);
                textField4.setBounds(800, 255, 130, 30);

                //---- textField5 ----
                textField5.setEditable(false);
                panel4.add(textField5);
                textField5.setBounds(800, 300, 130, 30);

                //---- button7 ----
                button7.setText("\u751f\u6210\u710a\u63a5\u53c2\u6570");
                button7.addActionListener(e -> button7ActionPerformed(e));
                panel4.add(button7);
                button7.setBounds(520, 390, 110, 30);

                //---- button8 ----
                button8.setText("test");
                button8.addActionListener(e -> button8ActionPerformed(e));
                panel4.add(button8);
                button8.setBounds(new Rectangle(new Point(435, 390), button8.getPreferredSize()));

                //---- button9 ----
                button9.setText("\u81ea\u5b9a\u4e49\u710a\u63a5\u53c2\u6570");
                button9.addActionListener(e -> button9ActionPerformed(e));
                panel4.add(button9);
                button9.setBounds(645, 390, 125, 30);

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
            tabbedPane2.addTab("\u5de5\u827a\u6d41\u7a0b\u8bbe\u8ba1", panel4);

            //======== panel2 ========
            {
                panel2.setLayout(null);

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
            tabbedPane2.addTab("\u8d44\u6599\u5e93", panel2);
        }
        contentPane.add(tabbedPane2);
        tabbedPane2.setBounds(5, 95, 970, 490);

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
        setSize(995, 625);
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
    private JTabbedPane tabbedPane2;
    private JPanel panel4;
    private JLabel label1;
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
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JComboBox comboBox7;
    private JComboBox comboBox8;
    private JComboBox comboBox9;
    private JComboBox comboBox10;
    private JComboBox comboBox11;
    private JLabel label14;
    private JLabel label15;
    private JLabel label16;
    private JLabel label17;
    private JLabel label18;
    private JLabel label19;
    private JComboBox comboBox12;
    private JComboBox comboBox13;
    private JComboBox comboBox14;
    private JComboBox comboBox15;
    private JComboBox comboBox16;
    private JButton button5;
    private JButton button6;
    private JLabel label20;
    private JLabel label21;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JPanel panel2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
