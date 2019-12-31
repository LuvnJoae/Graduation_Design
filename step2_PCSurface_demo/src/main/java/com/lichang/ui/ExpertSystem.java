/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.lichang.utils.ExpertSystemUtils.KnowledgeBaseUtil;
import com.lichang.utils.ExpertSystemUtils.ProcessDesignUtil;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.ChangePasswordUtil;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

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

    /**
     * 流程设计
     */
    // 下拉框模型
    private ComboBoxModel boxModel1;
    private ComboBoxModel boxModel2;
    private ComboBoxModel boxModel3;
    private ComboBoxModel boxModel4;
    private ComboBoxModel boxModel5;
    private ComboBoxModel boxModel6;
    private ComboBoxModel boxModel7;
    private ComboBoxModel boxModel8;
    private ComboBoxModel boxModel9;
    private ComboBoxModel boxModel10;

    //专家系统 各表的内容
    private List<Map<String, Object>> expert_base_metal_mapsList;
    private List<Map<String, Object>> expert_weld_method_mapsList;
    private List<Map<String, Object>> expert_weld_metal_mapsList;
    private List<Map<String, Object>> expert_auxiliary_materials_mapsList;
    private List<Map<String, Object>> expert_workpiece_thickness_mapsList;
    private List<Map<String, Object>> expert_weld_joint_mapsList;
    private List<Map<String, Object>> expert_thermal_process_mapsList;
    private List<Map<String, Object>> expert_process_parameters_mapsList;
    private List<Map<String, Object>> expert_production_mapsList;

    //下拉框的值
    String comboBox1_item;
    String comboBox2_item;
    String comboBox3_item;
    String comboBox4_item;
    String comboBox5_item;
    String comboBox6_item;
    String comboBox7_item;
    String comboBox8_item;
    String comboBox9_item;
    String comboBox10_item;
    String comboBox11_item;
    String comboBox12_item;
    String comboBox13_item;
    String comboBox14_item;
    String comboBox15_item;
    String comboBox16_item;

    //实际值
    String textField1_item;
    String textField2_item;
    String textField3_item;
    String textField4_item;
    String textField6_item;

    //用于产品选择下拉框
    private HashMap<String, Map<String, Object>> productionMaps; //存储production的name，与其他参数的映射信息
    private boolean productionSelectFlag = false; //用于在使用产品选择功能时，暂时不遵循规则

    /**
     * 资料库
     */
    //保存table的model
    private DefaultTableModel table1Model;
    private DefaultTableModel table2Model;
    private DefaultTableModel table3Model;
    private DefaultTableModel table4Model;
    private DefaultTableModel table5Model;
    private DefaultTableModel table6Model;
    private DefaultTableModel table7Model;
    private DefaultTableModel table8Model;
    private DefaultTableModel table9Model;

    //作用确定按钮，辨别是添加还是修改
    String addFlag = "0"; //无状态：0，添加：1， 删除：2，修改：3

    //当前tabbed下标
    String tabbedPannel1_index;

    //无参构造
    public ExpertSystem() {
        username = "admin";
        adminFlag = true;

        UIManager.put("ComboBox.disabledForeground", new Color(0, 45, 145)); //修改ComboBox不可选中时的字体颜色
        UIManager.put("TextField.inactiveForeground", new Color(0, 45, 145)); //修改TextField不可选中时的字体颜色
        initComponents();
        updateComboBox17(); //加载 产品选择

        this.setBounds(273, 95, 990, 625);
        setVisible(true);
    }

    //有参构造 接收登录账户信息
    public ExpertSystem(String username, Boolean adminFlag) {
        this.username = username;
        this.adminFlag = adminFlag;

        UIManager.put("ComboBox.disabledForeground", new Color(0, 45, 145)); //修改ComboBox不可选中时的字体颜色
        UIManager.put("TextField.inactiveForeground", new Color(0, 45, 145)); //修改TextField不可选中时的字体颜色
        initComponents();

        updateComboBox17(); //加载 产品选择

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
     * 整体页面 事件监听
     */
    //打开该frame时，触发
    private void thisWindowOpened(WindowEvent e) {
        //非管理员用户，禁止使用重设 、设计、 仅调参等功能
        if (!adminFlag) {
            button6.setEnabled(false); //重设
            button10.setEnabled(false); //设计
            button16.setEnabled(false); //仅调参
            textField6.setEnabled(false); //添加产品 文本框
        }

        //获取并设置上次登录时选择的产品下标
        String lastProductionName = ProcessDesignUtil.getLastProductionName();

        //用于：当删除产品时，没有切换到 流程设计，导致存储的最后产品选择下标实际上已被删除，再次启动软件时找不到该下标。
        boolean findFlag = false;
        for (int i = 0; i < comboBox17.getModel().getSize(); i++) {
            if (comboBox17.getItemAt(i).equals(lastProductionName)) {
                comboBox17.setSelectedIndex(i);
                findFlag = true;
                break;
            }
        }
        //没找到，则下标默认为-1
        if (!findFlag) {
            comboBox17.setSelectedIndex(-1);
        }

        //非管理员用户，禁止使用 资料库中：添加、删除、修改 功能
        if (!adminFlag) {
            button20.setEnabled(false); //添加
            button9.setEnabled(false); //删除
            button22.setEnabled(false); //修改
        }
    }

    //关闭该frame时，触发
    private void thisWindowClosed(WindowEvent e) {
        String lastProductionName = (String) comboBox17.getSelectedItem();
        ProcessDesignUtil.setLastProductionName(lastProductionName);
        return;
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
        //关闭所有frame
        Window[] frame = JFrame.getWindows();
        for (Window window : frame) {
            window.dispose();
        }
        new Login();
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

        changePasswordPanel.setBackground(new Color(238,238,238));
        changePasswordPanel.setLayout(null);

        //提示
        JTextArea tip = new JTextArea("提示：密码2~10个字符，可使用字母、数字、下划线");
        changePasswordPanel.add(tip);
        tip.setBounds(50, 20, 300, 40);
        tip.setBorder(null); //设置无边框
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

    //历史统计与查询 按钮： 点击跳转
    private void button2ActionPerformed(ActionEvent e) {
        new HistoricalStatistics(username, adminFlag);
        this.dispose();
    }

    //管理与设置 按钮： 点击跳转
    private void button4ActionPerformed(ActionEvent e) {
        new Setting(username, adminFlag);
        this.dispose();
    }

    /**
     * TextField 1-4文本框 相关操作
     */
    //文本框 1-4 清空数据
    private void emptyTextField_1_to_4() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");

        textField1.removeAll();
        textField2.removeAll();
        textField3.removeAll();
        textField4.removeAll();
    }

    //文本框 1-4 enable
    private void enableTextField_1_to_4() {
        textField1.setEnabled(true);
        textField2.setEnabled(true);
        textField3.setEnabled(true);
        textField4.setEnabled(true);
    }

    //文本框 1-4 enableFalse
    private void enableFalseTextField_1_to_4() {
        textField1.setEnabled(false);
        textField2.setEnabled(false);
        textField3.setEnabled(false);
        textField4.setEnabled(false);
    }

    /**
     * ComboBox 1-16下拉框 连接数据库
     */
    //下拉框 1-11 enable
    private void enableComboBox_1_to_11() {
        comboBox1.setEnabled(true);
        comboBox2.setEnabled(true);
        comboBox3.setEnabled(true);
        comboBox4.setEnabled(true);
        comboBox5.setEnabled(true);
        comboBox6.setEnabled(true);
        comboBox7.setEnabled(true);
        comboBox8.setEnabled(true);
        comboBox9.setEnabled(true);
        comboBox10.setEnabled(true);
        comboBox11.setEnabled(true);
    }

    //下拉框 12-16 enable
    private void enableComboBox_12_to_16() {
        comboBox12.setEnabled(true);
        comboBox13.setEnabled(true);
        comboBox14.setEnabled(true);
        comboBox15.setEnabled(true);
        comboBox16.setEnabled(true);
    }

    //下拉框 1-16 enableFalse
    private void enableFalseComboBox() {
        comboBox1.setEnabled(false);
        comboBox2.setEnabled(false);
        comboBox3.setEnabled(false);
        comboBox4.setEnabled(false);
        comboBox5.setEnabled(false);
        comboBox6.setEnabled(false);
        comboBox7.setEnabled(false);
        comboBox8.setEnabled(false);
        comboBox9.setEnabled(false);
        comboBox10.setEnabled(false);
        comboBox11.setEnabled(false);
        comboBox12.setEnabled(false);
        comboBox13.setEnabled(false);
        comboBox14.setEnabled(false);
        comboBox15.setEnabled(false);
        comboBox16.setEnabled(false);
    }

    //下拉框 1-16 清空原模型数据
    private void emptyComboBox() {
        //内容清空
        comboBox1.removeAllItems();
        comboBox2.removeAllItems();
        comboBox3.removeAllItems();
        comboBox4.removeAllItems();
        comboBox5.removeAllItems();
        comboBox6.removeAllItems();
        comboBox7.removeAllItems();
        comboBox8.removeAllItems();
        comboBox9.removeAllItems();
        comboBox10.removeAllItems();
        comboBox11.removeAllItems();
        comboBox12.removeAllItems();
        comboBox13.removeAllItems();
        comboBox14.removeAllItems();
        comboBox15.removeAllItems();
        comboBox16.removeAllItems();

        //模型清空
        boxModel1 = null;
        boxModel2 = null;
        boxModel3 = null;
        boxModel4 = null;
        boxModel5 = null;
        boxModel6 = null;
        boxModel7 = null;
        boxModel8 = null;
        boxModel9 = null;
        boxModel10 = null;


    }

    //下拉框 1-10 获取并添加数据。（真实：数据库）
    private void initComboBox_fromDB() {
        //获取表数据
        expert_base_metal_mapsList = ProcessDesignUtil.getData("expert_base_metal"); // 母材选取
        expert_weld_method_mapsList = ProcessDesignUtil.getData("expert_weld_method"); // 焊接方法
        expert_weld_metal_mapsList = ProcessDesignUtil.getData("expert_weld_metal"); // 焊接材料
        expert_auxiliary_materials_mapsList = ProcessDesignUtil.getData("expert_auxiliary_materials"); // 辅材
        expert_workpiece_thickness_mapsList = ProcessDesignUtil.getData("expert_workpiece_thickness"); // 工件厚度
        expert_weld_joint_mapsList = ProcessDesignUtil.getData("expert_weld_joint"); // 焊接接头、坡口、焊接位置
        expert_thermal_process_mapsList = ProcessDesignUtil.getData("expert_thermal_process"); // 热工艺

        //给下拉框添加内容
        initComboBox_addData_fromDB(comboBox1, expert_base_metal_mapsList, "name");
        initComboBox_addData_fromDB(comboBox2, expert_base_metal_mapsList, "name");
        initComboBox_addData_fromDB(comboBox3, expert_weld_method_mapsList, "name");
        initComboBox_addData_fromDB(comboBox4, expert_weld_metal_mapsList, "name");
        initComboBox_addData_fromDB(comboBox5, expert_auxiliary_materials_mapsList, "name");
        initComboBox_addData_fromDB(comboBox6, expert_workpiece_thickness_mapsList, "name");
        initComboBox_addData_fromDB(comboBox7, expert_weld_joint_mapsList, "weld_position");
        initComboBox_addData_fromDB(comboBox8, expert_weld_joint_mapsList, "joint_form");
        initComboBox_addData_fromDB(comboBox9, expert_weld_joint_mapsList, "groove_form");
        initComboBox_addData_fromDB(comboBox10, expert_thermal_process_mapsList, "name");
    }

    //下拉框 添加数据项（真）
    private void initComboBox_addData_fromDB(JComboBox comboBox, List<Map<String, Object>> expert_comboBox_mapsList, String colName) {
        for (Map<String, Object> expert_comboBox_map : expert_comboBox_mapsList) {
            String col = (String) expert_comboBox_map.get(colName);
            comboBox.addItem(col);
        }
        comboBox.setSelectedIndex(-1);
    }

    //下拉框 获取并添加数据。（测试：自定义样例）
    private void initComboBox_fromTest() {
        // 自定义下拉框内容
        // 母材选取 A & B
        String[] comboBox1_items = {
                "碳钢碳锰钢",
                "细晶粒钢",
                "高强细晶粒结构钢",
                "热强钢(非合金)",
                "热强钢(合金)",
                "不锈钢+耐热钢",
                "铸铁球墨铸铁",
                "铜和铜合金",
                "镍和镍合金",
                "铝材料",
                "钛和钛合金"
        };
        String[] comboBox2_items = {
                "碳钢碳锰钢",
                "细晶粒钢",
                "高强细晶粒结构钢",
                "热强钢(非合金)",
                "热强钢(合金)",
                "不锈钢+耐热钢",
                "铸铁球墨铸铁",
                "铜和铜合金",
                "镍和镍合金",
                "铝材料",
                "钛和钛合金"
        };

        // 焊接方法
        String[] comboBox3_items = {
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

        // 焊接材料
        String[] comboBox4_items = {
                "提示：焊丝用于气保焊",
                "提示：焊条用于手工焊",
                "焊丝：YM-80A",
                "焊丝：SM-70",
                "焊丝：ER50-6",
                "焊条：L-80SN",
                "焊条：J506",
                "其他"};

        // 辅材
        String[] comboBox5_items = {
                "CO2",
                "其他"};
        DefaultComboBoxModel<String> box5Model = new DefaultComboBoxModel<>(comboBox5_items);

        // 工件厚度
        String[] comboBox6_items = {
                "车前架三角撑",
                "其他"};

        // 焊接位置
        String[] comboBox7_items = {
                "PA 平焊",
                "PB 平角焊",
                "PC 横焊",
                "PD 仰角焊",
                "PE 仰焊",
                "PF 向上立焊",
                "PG 向下立焊",
                "其他"
        };

        // 接头
        String[] comboBox8_items = {
                "对接",
                "角接",
                "T字",
                "搭接",
                "其他"
        };

        // 坡口
        String[] comboBox9_items = {
                "I 形",
                "V 形",
                "V 形",
                "U 形",
                "J 形",
                "组合",
                "其他"
        };

        // 热工艺
        String[] comboBox10_items = {
                "其他"
        };

        initComboBox_addData_fromTest(comboBox1, comboBox1_items);
        initComboBox_addData_fromTest(comboBox2, comboBox2_items);
        initComboBox_addData_fromTest(comboBox3, comboBox3_items);
        initComboBox_addData_fromTest(comboBox4, comboBox4_items);
        initComboBox_addData_fromTest(comboBox5, comboBox5_items);
        initComboBox_addData_fromTest(comboBox6, comboBox6_items);
        initComboBox_addData_fromTest(comboBox7, comboBox7_items);
        initComboBox_addData_fromTest(comboBox8, comboBox8_items);
        initComboBox_addData_fromTest(comboBox9, comboBox9_items);
        initComboBox_addData_fromTest(comboBox10, comboBox10_items);
    }

    //下拉框 添加数据项（测）
    private void initComboBox_addData_fromTest(JComboBox comboBox, String[] comboBox_items) {
        DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<>(comboBox_items);
        comboBox.setModel(boxModel);
        comboBox.setSelectedIndex(-1);
    }

    //下拉框 获取初始model（初始化后的）
    private void getInitComboBoxModel() {
        boxModel1 = comboBox1.getModel();
        boxModel2 = comboBox2.getModel();
        boxModel3 = comboBox3.getModel();
        boxModel4 = comboBox4.getModel();
        boxModel5 = comboBox5.getModel();
        boxModel6 = comboBox6.getModel();
        boxModel7 = comboBox7.getModel();
        boxModel8 = comboBox8.getModel();
        boxModel9 = comboBox9.getModel();
        boxModel10 = comboBox10.getModel();
    }

    /**
     * 按钮 事件触发
     */
    //设定下拉框内容 主方法
    private void setComboBox_main() {
        emptyComboBox(); //清空下拉框
        emptyTextField_1_to_4(); //清空文本框

        //initComboBox_fromTest();
        initComboBox_fromDB(); //初始化下拉框 加载内容
        getInitComboBoxModel(); //获取初始各下拉框model，用于规则推理重置model

        enableComboBox_1_to_11(); //使能下拉框1-11
    }

    //出参 主方法
    private boolean setParams_main() {
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
            return false;
        }

        //开启焊接参数 相关使能
        enableComboBox_12_to_16(); //使能12-16的下拉框
        enableTextField_1_to_4(); //使能1-4的文本框

        //按照规则生成焊接参数
        generateProcessParameters();
        return true;
    }

    //自定义 按钮
    private void button7ActionPerformed(ActionEvent e) {
        setComboBox_main(); //设定下拉框内容

        //关闭 其他按钮的使能
        button6.setEnabled(false); //重设
        button10.setEnabled(false); //设计
        button16.setEnabled(false); //仅调参
        button17.setEnabled(false); //保存
    }

    //出参（右下） 按钮
    private void button15ActionPerformed(ActionEvent e) {
        setParams_main(); //调用出参 主方法

        //开启 其他按钮使能
        enableComboBox_12_to_16(); //使能12-16的下拉框
    }

    //设计 （添加产品）按钮
    private void button10ActionPerformed(ActionEvent e) {
        if (textField6.getText() != null && textField6.getText().length() > 0) {
            //是否已有该产品名称检测
            updateComboBox17(); //更新产品下拉框内容
            ComboBoxModel box17Model = comboBox17.getModel();
            for (int i = 0; i < box17Model.getSize(); i++) {
                Object comboBox17_item = box17Model.getElementAt(i);
                if (comboBox17_item.equals(textField6.getText())) {
                    JOptionPane.showMessageDialog(this, "该产品已存在！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            setComboBox_main(); //调用 设定下拉框内容 主方法

            //开启 其他按钮使能
            button13.setEnabled(true);

            //关闭 其他按钮使能
            button6.setEnabled(false); //产品选择： 重设按钮
            button16.setEnabled(false); //右下：仅调参按钮
            button17.setEnabled(false); //右下：保存按钮
            button7.setEnabled(false); //右下：自定义按钮
            button15.setEnabled(false); //右下：出参按钮
            comboBox17.setEnabled(false); //产品选择 下拉框
        } else {
            JOptionPane.showMessageDialog(this, "请先填写产品名称！", "提示", JOptionPane.WARNING_MESSAGE);
        }

    }

    //出参 （添加产品） 按钮
    private void button13ActionPerformed(ActionEvent e) {
        boolean flag = setParams_main();//调用 出参 主方法
        //开启 其他按钮使能
        if (flag) {
            button14.setEnabled(true); //设置只有先按下出参按钮，且成功了，才能点击保存
        } else {
            return;
        }
    }

    //保存 （添加产品）按钮
    private void button14ActionPerformed(ActionEvent e) {
        addProduction();  //向数据库中添加该产品信息

        //关闭 其他按钮使能
        button14.setEnabled(false); //保存 按钮
        button13.setEnabled(false); //出参 按钮

        enableFalseComboBox(); //关闭下拉框使能
        enableFalseTextField_1_to_4(); //关闭文本框使能
    }

    //重设 （产品选择）按钮
    private void button6ActionPerformed(ActionEvent e) {
        //是否已选择产品
        if (comboBox17.getSelectedIndex() == -1 || comboBox17.getSelectedItem() == null || comboBox17.getSelectedItem().equals("")) {
            JOptionPane.showMessageDialog(this, "请先选择产品！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        setComboBox_main(); //调用 设定下拉框内容 主方法
        //关闭 其他按钮的使能
        button10.setEnabled(false); //设计 按钮
        button16.setEnabled(false); //仅调参 按钮
        button17.setEnabled(false); //右下：保存 按钮
        button15.setEnabled(false); //自定义 按钮
        button7.setEnabled(false); //右下：出参 按钮
        //开启 其他按钮的使能
        button12.setEnabled(true); //出参 按钮

    }

    //出参 （产品选择）按钮
    private void button12ActionPerformed(ActionEvent e) {
        boolean flag = setParams_main(); //调用 出参 主方法
        //开启 其他按钮使能
        if (flag) {
            button11.setEnabled(true); //设置只有先按下出参按钮，且成功了，才能点击保存
        } else {
            return;
        }

    }

    //保存 （产品选择）按钮
    private void button11ActionPerformed(ActionEvent e) {
        updateProduction();  //向数据库中 更新该产品信息
        //关闭 其他按钮使能
        button11.setEnabled(false); //保存 按钮
        button12.setEnabled(false); //出参 按钮

        enableFalseComboBox(); //关闭下拉框使能
        enableFalseTextField_1_to_4(); //关闭文本框使能
    }

    //仅调参 按钮
    private void button16ActionPerformed(ActionEvent e) {
        if (comboBox17.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "请先选择产品！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //开启 使能
        enableTextField_1_to_4(); //开启 文本框使能
        comboBox16.setEnabled(true); //开启 阈值value_limit使能
        button17.setEnabled(true); //右下：保存按钮 开启使能
        //关闭 使能
        comboBox17.setEnabled(false); //产品选择 下拉框
        button6.setEnabled(false); //重设 按钮
        button10.setEnabled(false); //设计 按钮
        button7.setEnabled(false); //自定义 按钮
        button15.setEnabled(false); //出参 按钮
    }

    //保存 （右下） 按钮
    private void button17ActionPerformed(ActionEvent e) {
        updateProduction(); //更新值
        //关闭 使能
        enableFalseTextField_1_to_4(); //关闭 文本框使能
        comboBox16.setEnabled(false); //关闭 阈值value_limit使能
        button17.setEnabled(false); //右下：保存按钮 关闭使能
        //开启 使能
        comboBox17.setEnabled(true); //产品选择 下拉框
        button6.setEnabled(true); //重设 按钮
        button10.setEnabled(true); //设计 按钮
        button7.setEnabled(true); //自定义 按钮
        button15.setEnabled(true); //出参 按钮
    }

    //取消 按钮
    private void button18ActionPerformed(ActionEvent e) {
        productionSelectFlag = false; //恢复规则
        //清空下拉框内容
        emptyComboBox();
        //关闭下拉框使能
        enableFalseComboBox();
        //清空文本框内容
        emptyTextField_1_to_4();
        //关闭文本框使能
        enableFalseTextField_1_to_4();
        textField6.setText("");
        textField6.removeAll();
        //关闭产品选择下拉框
        comboBox17.setSelectedIndex(-1);
        //关闭按钮使能
        button11.setEnabled(false); //产品选择： 保存
        button12.setEnabled(false); //产品选择： 出参
        button13.setEnabled(false); //添加产品： 保存
        button14.setEnabled(false); //添加产品： 出参
        button17.setEnabled(false); //右下：保存按钮
        //恢复按钮使能
        //权限管理
        if (adminFlag) {
            button10.setEnabled(true); //添加产品：设计按钮
            button6.setEnabled(true); //产品选择： 重设按钮
            button16.setEnabled(true); //右下：仅调参按钮
        }
        button7.setEnabled(true); //右下：自定义按钮
        button15.setEnabled(true); //右下：出参按钮
        comboBox17.setEnabled(true);
    }

    /**
     * 添加产品 相关
     */
    //添加记录 至 数据库
    private void addProduction() {
        comboBox1_item = (String) comboBox1.getSelectedItem();
        comboBox2_item = (String) comboBox2.getSelectedItem();
        comboBox3_item = (String) comboBox3.getSelectedItem();
        comboBox4_item = (String) comboBox4.getSelectedItem();
        comboBox5_item = (String) comboBox5.getSelectedItem();
        comboBox6_item = (String) comboBox6.getSelectedItem();
        comboBox7_item = (String) comboBox7.getSelectedItem();
        comboBox8_item = (String) comboBox8.getSelectedItem();
        comboBox9_item = (String) comboBox9.getSelectedItem();
        comboBox10_item = (String) comboBox10.getSelectedItem();
        comboBox11_item = (String) comboBox11.getSelectedItem();
        comboBox12_item = (String) comboBox12.getSelectedItem();
        comboBox13_item = (String) comboBox13.getSelectedItem();
        comboBox14_item = (String) comboBox14.getSelectedItem();
        comboBox15_item = (String) comboBox15.getSelectedItem();
        comboBox16_item = (String) comboBox16.getSelectedItem();

        textField1_item = textField1.getText();
        textField2_item = textField2.getText();
        textField3_item = textField3.getText();
        textField4_item = textField4.getText();
        textField6_item = textField6.getText();

        boolean result = ProcessDesignUtil.setData(
                textField6_item,
                comboBox1_item,
                comboBox2_item,
                comboBox3_item,
                comboBox4_item,
                comboBox5_item,
                comboBox6_item,
                comboBox7_item,
                comboBox8_item,
                comboBox9_item,
                comboBox10_item,
                comboBox11_item,
                comboBox12_item,
                comboBox13_item,
                comboBox14_item,
                comboBox15_item,
                comboBox16_item,
                textField1_item,
                textField2_item,
                textField3_item,
                textField4_item
        );

        if (result) {
            JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "保存失败！请重试！", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * 产品选择 相关
     */
    //刷新 产品选择下拉框 内容
    private void updateComboBox17() {
        comboBox17.removeAllItems(); //先清空原数据

        expert_production_mapsList = ProcessDesignUtil.getData("expert_production"); //更新内容

        //非空判断，如果为空，直接清空所有下拉框。用于所有产品被删除时的刷新
        if (expert_production_mapsList == null || expert_production_mapsList.size() == 0) {
            emptyComboBox();
            return;
        }

        productionMaps = new HashMap<>(); //以name为键，其他信息的map为值，建立映射关系
        //更新 产品选择 下拉框内容
        for (Map<String, Object> map : expert_production_mapsList) {
            String name = (String) map.get("name");
            productionMaps.put(name, map);

            comboBox17.addItem(name); //下拉框添加内容
        }

        emptyComboBox(); //这里要再清空一下，因为addItem会触发一次comboBox的 change事件

        comboBox17.setSelectedIndex(-1);
    }

    //刷新条件：当点击 产品选择下拉框时， 刷新该下拉框内容
    private void comboBox17MouseClicked(MouseEvent e) {
        updateComboBox17();
    }

    //根据 产品选择下拉框 所选内容，加载其余参数
    private void loadProductionParams(String productionName) {
        productionSelectFlag = true; //暂时忽略规则，进入产品选择的功能

        emptyComboBox(); //先清空原数据
        emptyTextField_1_to_4();

        Map<String, Object> production_paramMap = productionMaps.get(productionName);

        comboBox1.addItem(production_paramMap.get("base_metal_a"));
        comboBox2.addItem(production_paramMap.get("base_metal_b"));
        comboBox3.addItem(production_paramMap.get("weld_method"));
        comboBox4.addItem(production_paramMap.get("weld_metal"));
        comboBox5.addItem(production_paramMap.get("auxiliary_materials"));
        comboBox6.addItem(production_paramMap.get("workpiece_thickness"));
        comboBox7.addItem(production_paramMap.get("weld_joint_joint"));
        comboBox8.addItem(production_paramMap.get("weld_joint_groove"));
        comboBox9.addItem(production_paramMap.get("weld_joint_weldposition"));
        comboBox10.addItem(production_paramMap.get("thermal_parameters"));
        comboBox11.addItem(production_paramMap.get("extra_1"));
        comboBox12.addItem(production_paramMap.get("current_advice"));
        comboBox13.addItem(production_paramMap.get("voltage_advice"));
        comboBox14.addItem(production_paramMap.get("speed_advice"));
        comboBox15.addItem(production_paramMap.get("extension_advice"));
        comboBox16.addItem(production_paramMap.get("value_limit"));

        textField1.setText(String.valueOf(production_paramMap.get("current_practical")));
        textField2.setText(String.valueOf(production_paramMap.get("voltage_practical")));
        textField3.setText(String.valueOf(production_paramMap.get("speed_practical")));
        textField4.setText(String.valueOf(production_paramMap.get("extension_practical")));

        productionSelectFlag = false; //恢复规则
    }

    //选中相应产品时，触发相应参数加载动作
    private void comboBox17ItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            loadProductionParams((String) comboBox17.getSelectedItem());
        }
    }

    //更新该产品内容
    private void updateProduction() {
        comboBox1_item = (String) comboBox1.getSelectedItem();
        comboBox2_item = (String) comboBox2.getSelectedItem();
        comboBox3_item = (String) comboBox3.getSelectedItem();
        comboBox4_item = (String) comboBox4.getSelectedItem();
        comboBox5_item = (String) comboBox5.getSelectedItem();
        comboBox6_item = (String) comboBox6.getSelectedItem();
        comboBox7_item = (String) comboBox7.getSelectedItem();
        comboBox8_item = (String) comboBox8.getSelectedItem();
        comboBox9_item = (String) comboBox9.getSelectedItem();
        comboBox10_item = (String) comboBox10.getSelectedItem();
        comboBox11_item = (String) comboBox11.getSelectedItem();
        comboBox12_item = String.valueOf(comboBox12.getSelectedItem()) ;
        comboBox13_item = String.valueOf(comboBox13.getSelectedItem());
        comboBox14_item = String.valueOf(comboBox14.getSelectedItem());
        comboBox15_item = String.valueOf(comboBox15.getSelectedItem());
        comboBox16_item = String.valueOf(comboBox16.getSelectedItem());

        textField1_item = textField1.getText();
        textField2_item = textField2.getText();
        textField3_item = textField3.getText();
        textField4_item = textField4.getText();
        textField6_item = textField6.getText();

        boolean result = ProcessDesignUtil.updateData(
                (String) comboBox17.getSelectedItem(),
                comboBox1_item,
                comboBox2_item,
                comboBox3_item,
                comboBox4_item,
                comboBox5_item,
                comboBox6_item,
                comboBox7_item,
                comboBox8_item,
                comboBox9_item,
                comboBox10_item,
                comboBox11_item,
                comboBox12_item,
                comboBox13_item,
                comboBox14_item,
                comboBox15_item,
                comboBox16_item,
                textField1_item,
                textField2_item,
                textField3_item,
                textField4_item
        );

        if (result) {
            JOptionPane.showMessageDialog(this, "修改成功！", "提示", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "修改失败！请重试！", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * 规则 触发与制定： 修改下拉框可显示内容
     */
    //母材选取A -> 母材选取B： 规则制定
    private void comboBox1ItemStateChanged(ItemEvent e) {
        //当进入到 产品选择功能时，暂时放弃规则。
        if (productionSelectFlag) {
            return;
        }

        // 下拉框触发事件有两个，Selected 和 deSelected（即选中和未被选中）。 所以规定触发事件为Selected
        if (e.getStateChange() == ItemEvent.SELECTED) {
            comboBox1_item = (String) comboBox1.getSelectedItem(); //所选内容

            // 按照规则推理后的 受影响的下拉框 模型内容
            Object[] comboBox_items = searchForRule2(
                    boxModel2,
                    comboBox1_item,
                    "app_base_metal",
                    "name",
                    expert_base_metal_mapsList);

            //更新模型
            updateComboBoxModel(comboBox2, comboBox_items); //更新受影响的 下拉框内容
        }
    }

    //母材选取B + 母材选取A -> 焊接方法: 规则制定
    private void comboBox2ItemStateChanged(ItemEvent e) {
        //当进入到 产品选择功能时，暂时放弃规则。
        if (productionSelectFlag) {
            return;
        }

        if (e.getStateChange() == ItemEvent.SELECTED) {
            comboBox1_item = (String) comboBox1.getSelectedItem();
            comboBox2_item = (String) comboBox2.getSelectedItem();

            // 按照规则推理后的 受影响的下拉框 模型内容
            //母材A
            Object[] comboBox_items_from1 = searchForRule2(
                    boxModel3,
                    comboBox1_item,
                    "app_weld_method",
                    "name",
                    expert_base_metal_mapsList);
            //母材B
            Object[] comboBox_items_from2 = searchForRule2(
                    boxModel3,
                    comboBox2_item,
                    "app_weld_method",
                    "name",
                    expert_base_metal_mapsList);

            ArrayList<String> comboBox_items_list = new ArrayList<>(); //存储交集数据

            //寻找二者交集（母材A与母材B 的app_weld_method交集）
            boolean flag = false;
            for (Object item1 : comboBox_items_from1) {
                for (Object item2 : comboBox_items_from2) {
                    if (item1.equals(item2)) {
                        comboBox_items_list.add((String) item1);
                        flag = true;
                        break;
                    }
                }
            }

            if (!flag) {
                comboBox_items_list.add("无可用");
            }

            Object[] comboBox_items = comboBox_items_list.toArray();

            //更新模型
            updateComboBoxModel(comboBox3, comboBox_items); //更新受影响的 下拉框内容

        }
    }

    //焊接方法 -> 焊接材料：规则制定
    private void comboBox3ItemStateChanged(ItemEvent e) {
        //当进入到 产品选择功能时，暂时放弃规则。
        if (productionSelectFlag) {
            return;
        }

        if (e.getStateChange() == ItemEvent.SELECTED) {
            comboBox3_item = (String) comboBox3.getSelectedItem();

            //按照规则推理后的 受影响的下拉框 模型内容
            Object[] comboBox_items = searchForRule2(
                    boxModel4,
                    comboBox3_item,
                    "app_weld_metal",
                    "name",
                    expert_weld_method_mapsList);

            //更新模型
            updateComboBoxModel(comboBox4, comboBox_items); //更新受影响的 下拉框内容
        }
    }

    //焊接材料 + 焊接方法 -> 辅材： 规则制定
    private void comboBox4ItemStateChanged(ItemEvent e) {
        //当进入到 产品选择功能时，暂时放弃规则。
        if (productionSelectFlag) {
            return;
        }

        if (e.getStateChange() == ItemEvent.SELECTED) {
            comboBox3_item = (String) comboBox3.getSelectedItem();
            comboBox4_item = (String) comboBox4.getSelectedItem();

            //按照规则推理后的 受影响的下拉框 模型内容
            //焊接方法
            Object[] comboBox_items_from3 = searchForRule2(
                    boxModel5,
                    comboBox3_item,
                    "app_auxiliary_materials",
                    "name",
                    expert_weld_method_mapsList);

            //焊接材料
            Object[] comboBox_items_from4 = searchForRule2(
                    boxModel5,
                    comboBox4_item,
                    "app_auxiliary_materials",
                    "name",
                    expert_weld_metal_mapsList);

            ArrayList<String> comboBox_items_list = new ArrayList<>(); //存储交集数据

            //寻找二者交集
            boolean flag = false;
            for (Object item3 : comboBox_items_from3) {
                for (Object item4 : comboBox_items_from4) {
                    if (item3.equals(item4)) {
                        comboBox_items_list.add((String) item3);
                        flag = true;
                        break;
                    }
                }
            }

            if (!flag) {
                comboBox_items_list.add("无可用");
            }

            Object[] comboBox_items = comboBox_items_list.toArray();

            updateComboBoxModel(comboBox5, comboBox_items); //更新受影响的 下拉框内容
        }
    }

    //-> 工件厚度： （暂不设约束）
    private void comboBox5ItemStateChanged(ItemEvent e) {

    }

    //-> 焊接位置： （暂不设约束）
    private void comboBox6ItemStateChanged(ItemEvent e) {

    }

    //工件厚度 -> 接头： 规则制定
    private void comboBox6_2ItemStateChanged(ItemEvent e) {
        //当进入到 产品选择功能时，暂时放弃规则。
        if (productionSelectFlag) {
            return;
        }
        if (e.getStateChange() == ItemEvent.SELECTED) {
            comboBox6_item = (String) comboBox6.getSelectedItem();

            //按照规则推理后的 受影响的下拉框 模型内容
            Object[] comboBox_items = searchForRule2(
                    boxModel8,
                    comboBox6_item,
                    "app_weld_joint",
                    "name",
                    expert_workpiece_thickness_mapsList);

            //更新模型
            updateComboBoxModel(comboBox8, comboBox_items); //更新受影响的 下拉框内容
        }
    }

    //工件厚度 -> 坡口： 规则制定
    private void comboBox6_3ItemStateChanged(ItemEvent e) {
        //当进入到 产品选择功能时，暂时放弃规则。
        if (productionSelectFlag) {
            return;
        }

        if (e.getStateChange() == ItemEvent.SELECTED) {
            comboBox6_item = (String) comboBox6.getSelectedItem();

            //按照规则推理后的 受影响的下拉框 模型内容
            Object[] comboBox_items = searchForRule2(
                    boxModel9,
                    comboBox6_item,
                    "app_weld_joint",
                    "name",
                    expert_workpiece_thickness_mapsList);

            //更新模型
            updateComboBoxModel(comboBox9, comboBox_items); //更新受影响的 下拉框内容
        }
    }

    //热工艺： 规则制定（暂不设约束）
    private void comboBox9ItemStateChanged(ItemEvent e) {

    }

    //焊接参数：规则生成
    private void generateProcessParameters() {
        //当进入到 产品选择功能时，暂时放弃规则。
        if (productionSelectFlag) {
            return;
        }

        /*
            解释：
            焊接参数的生成，是根据流程设计中的各种参数选择最终决定的。
            所以，对于每一个流程项，给一个数字，作为选择的标志。
            最终全部流程选择完成后，得出一个字符串，根据字符串，判断所采用的焊接参数
            如：母材A项，选择了母材1，则为 1；
            母材B项，选择了母材2，则为 2；
            焊接方法，选择了焊接方法2，则为 2；
            焊接材料，选择了 焊接材料2，则为 2；
            ···（其他就不写了）
            所以最终得到一个字符串， 1222。
            则根据这个字符串，来查询所采用的焊接参数。
            而这个字符串与焊接参数的对应关系，在数据库中已经体现出来。（焊接参数表，seq列）
            具体的对应关系，则应焊接人员配合进行填写数据
         */

        //其他表，获得对应seq
        comboBox1_item = (String) comboBox1.getSelectedItem();
        comboBox2_item = (String) comboBox2.getSelectedItem();
        comboBox3_item = (String) comboBox3.getSelectedItem();
        comboBox4_item = (String) comboBox4.getSelectedItem();

        StringBuilder processParametersStr = new StringBuilder(); //用来辅助生成焊接参数

        for (Map<String, Object> map : expert_base_metal_mapsList) {
            if (map.get("name").equals(comboBox1_item)) {
                processParametersStr.append(map.get("seq"));
            }
        }
        for (Map<String, Object> map : expert_base_metal_mapsList) {
            if (map.get("name").equals(comboBox2_item)) {
                processParametersStr.append(map.get("seq"));
            }
        }
        for (Map<String, Object> map : expert_weld_method_mapsList) {
            if (map.get("name").equals(comboBox3_item)) {
                processParametersStr.append(map.get("seq"));
            }
        }
        for (Map<String, Object> map : expert_weld_metal_mapsList) {
            if (map.get("name").equals(comboBox4_item)) {
                processParametersStr.append(map.get("seq"));
            }
        }

        //焊接参数表
        String seq = String.valueOf(processParametersStr);
        expert_process_parameters_mapsList = ProcessDesignUtil.getData("expert_process_parameters");

        //存储符合规则的焊接参数
        List<String> current_list = new ArrayList<>();
        List<String> voltage_list = new ArrayList<>();
        List<String> speed_list = new ArrayList<>();
        List<String> extension_list = new ArrayList<>();

        boolean resultFlag = false;

        for (Map<String, Object> expert_process_parameters_map : expert_process_parameters_mapsList) {
            String seq_col = (String) expert_process_parameters_map.get("seq");

            if (Pattern.matches(".*" + seq + ".*", seq_col)) {
                String current_col = (String) expert_process_parameters_map.get("current");
                String voltage_col = (String) expert_process_parameters_map.get("voltage");
                String speed_col = (String) expert_process_parameters_map.get("speed");
                String extension_col = (String) expert_process_parameters_map.get("extension");

                current_list.add(current_col);
                voltage_list.add(voltage_col);
                speed_list.add(speed_col);
                extension_list.add(extension_col);

                resultFlag = true;
            }
        }

        if (!resultFlag) {
            JOptionPane.showMessageDialog(this, "查询不到该对应规则！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        updateComboBoxModel(comboBox12, current_list.toArray(), false);
        updateComboBoxModel(comboBox13, voltage_list.toArray(), false);
        updateComboBoxModel(comboBox14, speed_list.toArray(), false);
        updateComboBoxModel(comboBox15, extension_list.toArray(), false);
    }

    //规则推理：具体规则 + 返回推理后的 model 内容数组
    private Object[] searchForRule2(ComboBoxModel model,
                                    String comboBox_item,
                                    String app,
                                    String colName,
                                    List<Map<String, Object>> expert_mapsList) {
        /*
            参数说明：
                model： 受影响的下拉框原模型（即包含所有选项的初始模型）
                comboBox_item：当前所选下拉框的选项
                app：当前下拉框内容的 app列（即应用范围）
                colName：当前下拉框内容的 识别标识列（即comboBox_item所在的列）。如name列
                expert_mapsList：当前下拉框内容 的表名
         */

        //非空处理
        if (model == null || model.getSize() == 0) {
            return new Object[0];  //JComboBox加载时，会先触发一次事件，此时model还是null，所以要判断一下
            //确切说， JComboBox的 addItem方法，也会触发 itemStateChanged 事件。
        }

        //找到app（应用范围）
        boolean flag = false; //判断是否为空值
        for (Map<String, Object> map : expert_mapsList) {
            if (map.get(colName).equals(comboBox_item)) {
                if (map.get(app) == null) {
                    app = "无可用";
                    flag = false;
                } else {
                    app = (String) map.get(app);
                    flag = true;
                }
            }
        }

        //判断是否找到对应信息
        if (!flag) {
            return new Object[]{app};
        }

        ArrayList<String> comboBox_items_list = new ArrayList<>(); //存储已找到的数据

        //遍历原模型
        for (int i = 0; i < model.getSize(); i++) {
            String item = (String) model.getElementAt(i);
            //对受影响的原模型，依次遍历，通过正则表达式，将每个选项与app的内容进行匹配
            if (Pattern.matches(".*" + item + ".*", app)) {
                comboBox_items_list.add(item);
            }
        }

        Object[] comboBox_items = comboBox_items_list.toArray();
        return comboBox_items;
    }

    //规则推理：更新下拉框 model
    private void updateComboBoxModel(JComboBox comboBox, Object[] comboBox_items, boolean flag) {
        DefaultComboBoxModel<Object> boxModel = new DefaultComboBoxModel<>(comboBox_items);
        comboBox.setModel(boxModel);

        if (flag) {
            comboBox.setSelectedIndex(-1);
        } else {
            comboBox.setSelectedIndex(0);
        }
    }

    //重载
    private void updateComboBoxModel(JComboBox comboBox, Object[] comboBox_items) {
        updateComboBoxModel(comboBox, comboBox_items, true);
    }

    /**
     * 资料库 表格
     */
    //表格: 载入内容 主方法
    private void initTable_main(List<Map<String, Object>> expert_mapsList, JTable table, String[] colsName, boolean editable) {
        DefaultTableModel tableModel; //模型
        if (editable){
            //用于当 修改 内容时，重新加载一个 格式相同，但可以编辑的表格
            tableModel = new DefaultTableModel(colsName, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };

            table.setModel(tableModel);

            switch (tabbedPannel1_index) {
                case "0":
                    setTableForm(table8);
                    break;
                case "1":
                    setTableForm(table1);
                    break;
                case "2":
                    setTableForm(table2);
                    break;
                case "3":
                    setTableForm(table3);
                    break;
                case "4":
                    setTableForm(table4);
                    break;
                case "5":
                    setTableForm(table5);
                    break;
                case "6":
                    setTableForm(table6);
                    break;
                case "7":
                    setTableForm(table7);
                    break;
                case "8":
                    setTableForm(table8);
                    break;
                default:
                    break;
            }
        } else {
            tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);
        }

        for (int i = 0; i < expert_mapsList.size(); i++) {
            List<Object> newRow_list = new ArrayList<>();
            for (String s : colsName) {
                newRow_list.add(expert_mapsList.get(i).get(s)); //将每个col的内容加载进list
            }
            Object[] newRow = newRow_list.toArray();

            tableModel.addRow(newRow); //添加模型

        }

    }

    //表格： 加载 各表格
    private void initTable(String index, boolean editable) {
        //加载 母材 表格
        String[] table1_colsName = {
                "id",
                "name",
                "seq",
                "app_base_metal",
                "app_weld_method",
                "temperature_rating",
                "weld_pro",
                "mechanical_pro",
                "standards",
                "chemical_composition",
                "technology _points"
        };

        //加载 焊接方法 表格
        String[] table2_colsName = {
                "id",
                "name",
                "seq",
                "app_weld_metal",
                "app_auxiliary_materials",
                "characteristic",
                "device_type",
                "device_parameters",
                "scope"
        };

        //加载 焊接材料 表格
        String[] table3_colsName = {
                "id",
                "name",
                "seq",
                "app_auxiliary_materials",
                "type",
                "chemical_composition",
                "mechanical_pro",
                "application"
        };

        //加载 辅材 表格
        String[] table4_colsName = {
                "id",
                "name",
                "seq",
                "app",
                "parameter"
        };

        //加载 工件厚度 表格
        String[] table5_colsName = {
                "id",
                "name",
                "seq",
                "app_weld_joint",
                "thickness"
        };

        //加载 焊接接头、坡口、位置 表格
        String[] table6_colsName = {
                "id",
                "name",
                "seq",
                "app",
                "joint_form",
                "groove_form",
                "groove_parameter",
                "weld_position"
        };

        //加载 热工艺 表格
        String[] table7_colsName = {
                "id",
                "name",
                "seq",
                "app",
                "heat_treatment_type",
                "preheating_tem",
                "preheating_time",
                "interlayer_tem",
                "heat_treatment_tem",
                "heat_treatment_time"
        };

        //加载 产品 表格
        String[] table8_colsName = {
                "id",
                "name",
                "base_metal_a",
                "base_metal_b",
                "weld_method",
                "weld_metal",
                "auxiliary_materials",
                "workpiece_thickness",
                "weld_joint_joint",
                "weld_joint_groove",
                "weld_joint_weldposition",
                "thermal_parameters",
                "extra_1",
                "current_advice",
                "voltage_advice",
                "speed_advice",
                "extension_advice",
                "value_limit",
                "current_practical",
                "voltage_practical",
                "speed_practical",
                "extension_practical"
        };


        //加载 焊接参数 表格
        String[] table9_colsName = {
                "id",
                "name",
                "seq",
                "app",
                "current",
                "current_type",
                "current_polarity",
                "voltage",
                "line_energy",
                "speed",
                "extension"
        };

        //index 为选择更新哪个表格，若为 空 ，则更新全部表格
        switch (index){
            case "0":
                initTable_main(expert_production_mapsList, table8, table8_colsName, editable);
                break;
            case "1":
                initTable_main(expert_base_metal_mapsList, table1, table1_colsName, editable);
                break;
            case "2":
                initTable_main(expert_weld_method_mapsList, table2, table2_colsName, editable);
                break;
            case "3":
                initTable_main(expert_weld_metal_mapsList, table3, table3_colsName, editable);
                break;
            case "4":
                initTable_main(expert_auxiliary_materials_mapsList, table4, table4_colsName, editable);
                break;
            case "5":
                initTable_main(expert_workpiece_thickness_mapsList, table5, table5_colsName, editable);
                break;
            case "6":
                initTable_main(expert_weld_joint_mapsList, table6, table6_colsName, editable);
                break;
            case "7":
                initTable_main(expert_thermal_process_mapsList, table7, table7_colsName, editable);
                break;
            case "8":
                initTable_main(expert_process_parameters_mapsList, table9, table9_colsName, editable);
                break;
            default:
                initTable_main(expert_production_mapsList, table8, table8_colsName, editable);
                initTable_main(expert_base_metal_mapsList, table1, table1_colsName, editable);
                initTable_main(expert_weld_method_mapsList, table2, table2_colsName, editable);
                initTable_main(expert_weld_metal_mapsList, table3, table3_colsName, editable);
                initTable_main(expert_auxiliary_materials_mapsList, table4, table4_colsName, editable);
                initTable_main(expert_workpiece_thickness_mapsList, table5, table5_colsName, editable);
                initTable_main(expert_weld_joint_mapsList, table6, table6_colsName, editable);
                initTable_main(expert_thermal_process_mapsList, table7, table7_colsName, editable);
                initTable_main(expert_process_parameters_mapsList, table9, table9_colsName, editable);
                break;
        }
    }

    //表格： 加载 各表格 重载1
    private void initTable(String index){
        initTable(index, false);
    }

    //表格： 加载 各表格 重载2
    private void initTable(){
        initTable("", false);
    }

    //表格： 刷新 数据 （重新获取数据）
    private void updateData() {
        //重新获取 数据库信息
        expert_production_mapsList = KnowledgeBaseUtil.getData("expert_production"); //产品
        expert_base_metal_mapsList = KnowledgeBaseUtil.getData("expert_base_metal"); // 母材选取
        expert_weld_method_mapsList = KnowledgeBaseUtil.getData("expert_weld_method"); // 焊接方法
        expert_weld_metal_mapsList = KnowledgeBaseUtil.getData("expert_weld_metal"); // 焊接材料
        expert_auxiliary_materials_mapsList = KnowledgeBaseUtil.getData("expert_auxiliary_materials"); // 辅材
        expert_workpiece_thickness_mapsList = KnowledgeBaseUtil.getData("expert_workpiece_thickness"); // 工件厚度
        expert_weld_joint_mapsList = KnowledgeBaseUtil.getData("expert_weld_joint"); // 焊接接头、坡口、焊接位置
        expert_thermal_process_mapsList = KnowledgeBaseUtil.getData("expert_thermal_process"); // 热工艺
        expert_process_parameters_mapsList = KnowledgeBaseUtil.getData("expert_process_parameters"); //焊接参数
    }

    //表格： 设置表格列宽等格式
    private void setTableForm(JTable table) {
        TableColumnModel cm = table.getColumnModel();
        if (table == table8){
            cm.getColumn(0).setPreferredWidth(50);
            cm.getColumn(1).setPreferredWidth(100);
            for (int i = 2; i < cm.getColumnCount(); i++) {
                cm.getColumn(i).setPreferredWidth(300);
            }
        }else {
            cm.getColumn(0).setPreferredWidth(50);
            cm.getColumn(1).setPreferredWidth(100);
            cm.getColumn(2).setPreferredWidth(100);
            if (cm.getColumnCount() < 7) {
                for (int i = 3; i < cm.getColumnCount(); i++) {
                    cm.getColumn(i).setPreferredWidth(300);
                }
            } else {
                for (int i = 3; i < cm.getColumnCount(); i++) {
                    cm.getColumn(i).setPreferredWidth(180);
                }
            }
        }
    }


    /**
     * 资料库 按钮
     */
    //跳转事件：资料库（TabbedPanel） 点击事件
    private void tabbedPane2MouseClicked(MouseEvent e) {
        updateData(); //当点击资料库时，第一次 加载数据来源
        initTable(); //更新表格
    }

    //搜索 按钮
    private void button21ActionPerformed(ActionEvent e) {
        String tabbedPannel1_index = String.valueOf(tabbedPane1.getSelectedIndex()); //获取当前所选tabbedPanel1的下标
        String findItem = textField11.getText(); //获取搜索条件

        //对于每个表格，检索不同
        switch (tabbedPannel1_index) {
            case "0":
                findRegex(table8, findItem);
                break;
            case "1":
                findRegex(table1, findItem);
                break;
            case "2":
                findRegex(table2, findItem);
                break;
            case "3":
                findRegex(table3, findItem);
                break;
            case "4":
                findRegex(table4, findItem);
                break;
            case "5":
                findRegex(table5, findItem);
                break;
            case "6":
                findRegex(table6, findItem);
                break;
            case "7":
                findRegex(table7, findItem);
                break;
            case "8":
                findRegex(table9, findItem);
                break;
            default:
                break;
        }
    }

    //返回 按钮
    private void button24ActionPerformed(ActionEvent e) {
        String tabbedPannel1_index = String.valueOf(tabbedPane1.getSelectedIndex()); //获取当前所选tabbedPanel1的下标
        String findItem = ""; //设置检索条件为空，则默认显示所有结果

        //对于每个表格，检索不同
        switch (tabbedPannel1_index) {
            case "0":
                findRegex(table8, findItem);
                break;
            case "1":
                findRegex(table1, findItem);
                break;
            case "2":
                findRegex(table2, findItem);
                break;
            case "3":
                findRegex(table3, findItem);
                break;
            case "4":
                findRegex(table4, findItem);
                break;
            case "5":
                findRegex(table5, findItem);
                break;
            case "6":
                findRegex(table6, findItem);
                break;
            case "7":
                findRegex(table7, findItem);
                break;
            case "8":
                findRegex(table9, findItem);
                break;
            default:
                break;
        }

        textField11.setText(""); //点击返回后，清空文本框
    }

    //搜索、返回 主方法
    private void findRegex(JTable table, String findItem) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(findItem)); //查询格式为 正则表达式，范围为整个table
    }

    //刷新 按钮
    private void button42ActionPerformed(ActionEvent e) {
        updateData(); //调用刷新主方法，更新表格数据来源
        initTable(); //更新表格
    }

    //添加 按钮
    private void button20ActionPerformed(ActionEvent e) {
        tabbedPannel1_index = String.valueOf(tabbedPane1.getSelectedIndex()); //获取当前所选tabbedPanel1的下标
        addFlag = "1"; //标志位
        //对于每个表格，检索不同
        switch (tabbedPannel1_index) {
            case "0":
                table8Model = (DefaultTableModel) table8.getModel(); //保存原模型
                addCount(table8);
                break;
            case "1":
                table1Model = (DefaultTableModel) table1.getModel(); //保存原模型
                addCount(table1);
                break;
            case "2":
                table2Model = (DefaultTableModel) table2.getModel(); //保存原模型
                addCount(table2);
                break;
            case "3":
                table3Model = (DefaultTableModel) table3.getModel(); //保存原模型
                addCount(table3);
                break;
            case "4":
                table4Model = (DefaultTableModel) table4.getModel(); //保存原模型
                addCount(table4);
                break;
            case "5":
                table5Model = (DefaultTableModel) table5.getModel(); //保存原模型
                addCount(table5);
                break;
            case "6":
                table6Model = (DefaultTableModel) table6.getModel(); //保存原模型
                addCount(table6);
                break;
            case "7":
                table7Model = (DefaultTableModel) table7.getModel(); //保存原模型
                addCount(table7);
                break;
            case "8":
                table9Model = (DefaultTableModel) table9.getModel(); //保存原模型
                addCount(table9);
                break;
            default:
                break;
        }

        //去使能其他按钮
        button23.setEnabled(false); //刷新
        button20.setEnabled(false); //添加
        button9.setEnabled(false); //删除
        button22.setEnabled(false); //修改
        button21.setEnabled(false); //搜索
        button24.setEnabled(false); //返回
        //开使能
        button5.setEnabled(true); //确定
        button19.setEnabled(true); //返回2
    }

    //添加 主方法
    private void addCount(JTable table) {
        //获取 原列名
        ArrayList<String> colsName_list = new ArrayList<>();
        for (int i = 0; i < table.getColumnCount(); i++) {
            colsName_list.add(table.getColumnName(i));
        }
        Object[] colsName = colsName_list.toArray();

        //获取，最后的id
        int rowCount = table.getRowCount();
        int lastId;
        if (rowCount == 0) {
            lastId = 0;
        }else {
            lastId = (int) table.getValueAt(rowCount-1, 0);
        }
        String nextId = String.valueOf(lastId + 1);

        //设置新model
        table.setModel(new DefaultTableModel(
                new Object[][] {
                },
                colsName
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }
        });
        //设置新模型的列宽格式
        setTableForm(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);

        //加载空行(除了ID)
        Object[] r = {nextId};
        ((DefaultTableModel) table.getModel()).addRow(r);
    }

    //删除 按钮
    private void button9ActionPerformed(ActionEvent e) {
        tabbedPannel1_index = String.valueOf(tabbedPane1.getSelectedIndex()); //获取当前所选tabbedPanel1的下标
        addFlag = "2"; //标志位
        boolean deleteFlag;
        //对于每个表格，检索不同
        switch (tabbedPannel1_index) {
            case "0":
                deleteFlag = delete(table8);
                break;
            case "1":
                deleteFlag = delete(table1);
                break;
            case "2":
                deleteFlag = delete(table2);
                break;
            case "3":
                deleteFlag = delete(table3);
                break;
            case "4":
                deleteFlag = delete(table4);
                break;
            case "5":
                deleteFlag = delete(table5);
                break;
            case "6":
                deleteFlag = delete(table6);
                break;
            case "7":
                deleteFlag = delete(table7);
                break;
            case "8":
                deleteFlag = delete(table9);
                break;
            default:
                deleteFlag = false;
                break;
        }

        if (deleteFlag) {
            //去使能其他按钮
            button23.setEnabled(false); //刷新
            button20.setEnabled(false); //添加
            button9.setEnabled(false); //删除
            button22.setEnabled(false); //修改
            button21.setEnabled(false); //搜索
            button24.setEnabled(false); //返回1
            //开使能
            button5.setEnabled(true); //确定
            button19.setEnabled(true); //返回2
        }

    }

    //删除 主方法
    private boolean delete(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选中 所删除的行！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(this, "再次确认！如确定删除，请点击确定键！", "提示", JOptionPane.WARNING_MESSAGE);
        return true;
    }

    //修改 按钮
    private void button22ActionPerformed(ActionEvent e) {
        tabbedPannel1_index = String.valueOf(tabbedPane1.getSelectedIndex()); //获取当前所选tabbedPanel1的下标
        addFlag = "3"; //标志位
        boolean changeFlag;
        //对于每个表格，检索不同
        switch (tabbedPannel1_index) {
            case "0":
                table8Model = (DefaultTableModel) table8.getModel(); //保存原模型
                changeFlag = change(table8);
                break;
            case "1":
                table1Model = (DefaultTableModel) table1.getModel(); //保存原模型
                changeFlag = change(table1);
                break;
            case "2":
                table2Model = (DefaultTableModel) table2.getModel(); //保存原模型
                changeFlag = change(table2);
                break;
            case "3":
                table3Model = (DefaultTableModel) table3.getModel(); //保存原模型
                changeFlag = change(table3);
                break;
            case "4":
                table4Model = (DefaultTableModel) table4.getModel(); //保存原模型
                changeFlag = change(table4);
                break;
            case "5":
                table5Model = (DefaultTableModel) table5.getModel(); //保存原模型
                changeFlag = change(table5);
                break;
            case "6":
                table6Model = (DefaultTableModel) table6.getModel(); //保存原模型
                changeFlag = change(table6);
                break;
            case "7":
                table7Model = (DefaultTableModel) table7.getModel(); //保存原模型
                changeFlag = change(table7);
                break;
            case "8":
                table9Model = (DefaultTableModel) table9.getModel(); //保存原模型
                changeFlag = change(table9);
                break;
            default:
                changeFlag = false;
                break;
        }

        if (changeFlag) {
            //去使能其他按钮
            button23.setEnabled(false); //刷新
            button20.setEnabled(false); //添加
            button9.setEnabled(false); //删除
            button22.setEnabled(false); //修改
            button21.setEnabled(false); //搜索
            button24.setEnabled(false); //返回1
            //开使能
            button5.setEnabled(true); //确定
            button19.setEnabled(true); //返回2
        }

    }

    //修改 主方法
    private boolean change(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选中 所要修改的行！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(this, "您已可以 编辑 所选行！编辑完成后，请点击确认！");

        //获取 原列名
        ArrayList<String> colsName_list = new ArrayList<>();
        for (int i = 0; i < table.getColumnCount(); i++) {
            colsName_list.add(table.getColumnName(i));
        }
        Object[] colsName = colsName_list.toArray();

        //获取，所选行数，记录所选行数的id
        Object id = table.getValueAt(selectedRow, 0);

        //存储原数据（被选中的这一行）
        ArrayList<Object> data_list = new ArrayList<>();
        for (int i = 0; i < table.getColumnCount(); i++) {
            data_list.add(table.getValueAt(selectedRow, i));
        }
        Object[] data = data_list.toArray();

        //通过新模型，加载这一行，并使其可编辑
        //设置新model
        table.setModel(new DefaultTableModel(
                new Object[][] {
                },
                colsName
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }
        });
        //设置新模型的列宽格式
        setTableForm(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);
        //加载原数据
        ((DefaultTableModel) table.getModel()).addRow(data);

        return true;
    }

    //确定 按钮
    private void button5ActionPerformed(ActionEvent e) {
        boolean addResultFlag;
        //对于每个表格，检索不同
        switch (tabbedPannel1_index) {
            case "0":
                addResultFlag = enter_main(table8, table8Model, "expert_production", expert_production_mapsList);
                break;
            case "1":
                addResultFlag = enter_main(table1, table1Model, "expert_base_metal", expert_base_metal_mapsList);
                break;
            case "2":
                addResultFlag = enter_main(table2, table2Model, "expert_weld_method", expert_weld_method_mapsList);
                break;
            case "3":
                addResultFlag = enter_main(table3, table3Model, "expert_weld_metal", expert_weld_metal_mapsList);
                break;
            case "4":
                addResultFlag = enter_main(table4, table4Model,"expert_auxiliary_materials", expert_auxiliary_materials_mapsList);
                break;
            case "5":
                addResultFlag = enter_main(table5, table5Model, "expert_workpiece_thickness", expert_workpiece_thickness_mapsList);
                break;
            case "6":
                addResultFlag = enter_main(table6, table6Model, "expert_weld_joint", expert_weld_joint_mapsList);
                break;
            case "7":
                addResultFlag = enter_main(table7, table7Model, "expert_thermal_process", expert_thermal_process_mapsList);
                break;
            case "8":
                addResultFlag = enter_main(table9, table9Model, "expert_process_parameters", expert_process_parameters_mapsList);
                break;
            default:
                addResultFlag = true;
                break;
        }

        if (addResultFlag) {
            //关闭 使能
            button5.setEnabled(false); //确定
            button19.setEnabled(false); //返回2
            //开启 使能
            button23.setEnabled(true); //刷新
            button20.setEnabled(true); //添加
            button9.setEnabled(true); //删除
            button22.setEnabled(true); //修改
            button21.setEnabled(true); //搜索
            button24.setEnabled(true); //返回1
        }

    }

    //确定 主方法
    private boolean enter_main(JTable table, DefaultTableModel tableModel, String DB_tableName, List<Map<String, Object>> expert_mapsList) {
        if (addFlag.equals("1")) {
            //添加数据进数据库
            //对于不同的table，通过识别table中有多少列，确定有多少个参数，再通过for 字符串拼接的形式，添加？至sqlStr中
            //name非空判断
            if (table.getValueAt(0, 1) == null) {
                JOptionPane.showMessageDialog(this, "name不能为空！请确认name或重新输入name！", "提示", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            int colCount = table.getColumnCount();
            List<Object> params = new ArrayList<>();
            for (int i = 0; i < table.getColumnCount(); i++) {
                params.add(table.getValueAt(0, i));
            }
            boolean result = KnowledgeBaseUtil.addData(DB_tableName, colCount, params);
            if (result) {
                JOptionPane.showMessageDialog(this, "添加成功！", "提示", JOptionPane.WARNING_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(this, "添加失败！", "提示", JOptionPane.WARNING_MESSAGE);
            }

            //还原 原model
            table.setModel(tableModel);
            //设置model格式
            setTableForm(table);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setAutoCreateRowSorter(true);

            //刷新表格
            updateData(); //调用刷新主方法，更新表格数据来源
            initTable(); //更新表格

            return true;
        }else if (addFlag.equals("2")){
            int selectedRow = table.getSelectedRow();
            Object id = table.getValueAt(selectedRow, 0); //获得所选数据的id
            boolean result = KnowledgeBaseUtil.deleteData(DB_tableName, id); //删除
            if (result) {
                JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "删除失败！", "提示", JOptionPane.WARNING_MESSAGE);
            }
            //刷新表格
            updateData(); //调用刷新主方法，更新表格数据来源
            initTable(); //更新表格
            return true;
        }else if (addFlag.equals("3")) {
            int colCount = table.getColumnCount();
            ArrayList<Object> data_list = new ArrayList<>(); //列值
            ArrayList<Object> DB_colsName_list = new ArrayList<>(); //列名

            //遍历 列，获得值
            for (int i = 0; i < colCount; i++) {
                data_list.add(table.getValueAt(0, i)); //添加列值
            }

            //通过原数据的mapsList中的第一个map，遍历获得列名
            //如果没有数据，也就不能修改，所以避免了mapsList的空值问题
            Map<String, Object> DB_cols_map = expert_mapsList.get(0);
            Set<String> set = DB_cols_map.keySet();
            for (Object s : set) {
                DB_colsName_list.add(s);
            }

            boolean result = KnowledgeBaseUtil.changeData(DB_tableName, DB_colsName_list.toArray(), data_list.toArray());

            if (result) {
                JOptionPane.showMessageDialog(this, "修改成功！", "提示", JOptionPane.WARNING_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(this, "修改失败！", "提示", JOptionPane.WARNING_MESSAGE);
            }

            //还原 原model
            table.setModel(tableModel);
            //设置model格式
            setTableForm(table);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setAutoCreateRowSorter(true);

            //刷新表格
            updateData(); //调用刷新主方法，更新表格数据来源
            initTable(); //更新表格
            return true;
        }
        return true;
    }

    //返回 按钮
    private void button19ActionPerformed(ActionEvent e) {
        //对于每个表格，检索不同
        switch (tabbedPannel1_index) {
            case "0":
                back_main(table8, table8Model);
                break;
            case "1":
                back_main(table1, table1Model);
                break;
            case "2":
                back_main(table2, table2Model);
                break;
            case "3":
                back_main(table3, table3Model);
                break;
            case "4":
                back_main(table4, table4Model);
                break;
            case "5":
                back_main(table5, table5Model);
                break;
            case "6":
                back_main(table6, table6Model);
                break;
            case "7":
                back_main(table7, table7Model);
                break;
            case "8":
                back_main(table9, table9Model);
                break;
            default:
                break;
        }

        //关闭 使能
        button5.setEnabled(false); //确定
        button19.setEnabled(false); //返回2
        //开启 使能
        button23.setEnabled(true); //刷新
        button20.setEnabled(true); //添加
        button9.setEnabled(true); //删除
        button22.setEnabled(true); //修改
        button21.setEnabled(true); //搜索
        button24.setEnabled(true); //返回1
    }

    //返回 主方法
    private void back_main(JTable table, DefaultTableModel tableModel) {
        if (addFlag.equals("1")) {
            //还原 原model
            table.setModel(tableModel);

            //设置model格式
            setTableForm(table);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setAutoCreateRowSorter(true);

            //刷新表格
            updateData(); //调用刷新主方法，更新表格数据来源
            initTable(); //更新表格
        } else if (addFlag.equals("2")) {
            //刷新表格
            updateData(); //调用刷新主方法，更新表格数据来源
            initTable(); //更新表格
        } else if (addFlag.equals("3")) {
            //还原 原model
            table.setModel(tableModel);

            //设置model格式
            setTableForm(table);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setAutoCreateRowSorter(true);

            //刷新表格
            updateData(); //调用刷新主方法，更新表格数据来源
            initTable(); //更新表格
        }
    }



    /**
     * JFormDesigner自带，定义自生成
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel13 = new JPanel();
        panel1 = new JPanel();
        label2 = new JLabel();
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        label3 = new JLabel();
        button4 = new JButton();
        button3 = new JButton();
        button2 = new JButton();
        button1 = new JButton();
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
        button6 = new JButton();
        label20 = new JLabel();
        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        textField4 = new JTextField();
        button7 = new JButton();
        label22 = new JLabel();
        comboBox17 = new JComboBox();
        label23 = new JLabel();
        textField6 = new JTextField();
        button10 = new JButton();
        button11 = new JButton();
        button12 = new JButton();
        button13 = new JButton();
        button14 = new JButton();
        button15 = new JButton();
        button16 = new JButton();
        label21 = new JLabel();
        button17 = new JButton();
        button18 = new JButton();
        panel2 = new JPanel();
        tabbedPane1 = new JTabbedPane();
        panel11 = new JPanel();
        scrollPane8 = new JScrollPane();
        table8 = new JTable();
        panel3 = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel5 = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        panel6 = new JPanel();
        scrollPane3 = new JScrollPane();
        table3 = new JTable();
        panel7 = new JPanel();
        scrollPane4 = new JScrollPane();
        table4 = new JTable();
        panel8 = new JPanel();
        scrollPane5 = new JScrollPane();
        table5 = new JTable();
        panel9 = new JPanel();
        scrollPane6 = new JScrollPane();
        table6 = new JTable();
        panel10 = new JPanel();
        scrollPane7 = new JScrollPane();
        table7 = new JTable();
        panel12 = new JPanel();
        scrollPane9 = new JScrollPane();
        table9 = new JTable();
        textField11 = new JTextField();
        button21 = new JButton();
        label35 = new JLabel();
        button20 = new JButton();
        button22 = new JButton();
        button23 = new JButton();
        button24 = new JButton();
        label24 = new JLabel();
        button5 = new JButton();
        button9 = new JButton();
        button19 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u4e13\u5bb6\u7cfb\u7edf");
        setAlwaysOnTop(true);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getResource("/img/system(big).png")).getImage());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                thisWindowClosed(e);
            }
            @Override
            public void windowOpened(WindowEvent e) {
                thisWindowOpened(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel13 ========
        {
            panel13.setBackground(new Color(238, 238, 238));
            panel13.setLayout(null);

            //======== panel1 ========
            {
                panel1.setForeground(Color.black);
                panel1.setBackground(new Color(238, 238, 238));
                panel1.setLayout(null);

                //---- label2 ----
                label2.setText("\u5f53\u524d\uff1a");
                label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 2f));
                panel1.add(label2);
                label2.setBounds(5, 0, 45, 30);

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
                menuBar1.setBounds(150, 0, 86, 30);

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
            panel13.add(panel1);
            panel1.setBounds(750, 0, 236, 50);

            //---- button4 ----
            button4.setText("\u7ba1\u7406\u4e0e\u8bbe\u7f6e");
            button4.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 15));
            button4.setForeground(new Color(51, 51, 51));
            button4.addActionListener(e -> button4ActionPerformed(e));
            panel13.add(button4);
            button4.setBounds(790, 50, 135, 40);

            //---- button3 ----
            button3.setText("\u4e13\u5bb6\u7cfb\u7edf");
            button3.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 15));
            button3.setForeground(new Color(51, 51, 51));
            panel13.add(button3);
            button3.setBounds(535, 50, 135, 40);

            //---- button2 ----
            button2.setText("\u5386\u53f2\u7edf\u8ba1\u4e0e\u67e5\u8be2");
            button2.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 15));
            button2.setForeground(new Color(51, 51, 51));
            button2.addActionListener(e -> button2ActionPerformed(e));
            panel13.add(button2);
            button2.setBounds(290, 50, 135, 40);

            //---- button1 ----
            button1.setText("\u5b9e\u65f6\u76d1\u6d4b");
            button1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 15));
            button1.setForeground(new Color(51, 51, 51));
            button1.addActionListener(e -> button1ActionPerformed(e));
            panel13.add(button1);
            button1.setBounds(50, 50, 135, 40);
            panel13.add(separator4);
            separator4.setBounds(5, 90, 965, 10);

            //======== tabbedPane2 ========
            {
                tabbedPane2.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 15));
                tabbedPane2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        tabbedPane2MouseClicked(e);
                    }
                });

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
                    label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 1f));
                    label1.setForeground(new Color(60, 60, 60));
                    panel4.add(label1);
                    label1.setBounds(25, 30, 85, 30);

                    //---- label4 ----
                    label4.setText("\u710a\u63a5\u65b9\u6cd5");
                    label4.setBackground(new Color(204, 204, 204));
                    label4.setOpaque(true);
                    label4.setHorizontalTextPosition(SwingConstants.CENTER);
                    label4.setHorizontalAlignment(SwingConstants.CENTER);
                    label4.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label4.setFont(label4.getFont().deriveFont(label4.getFont().getStyle() | Font.BOLD, label4.getFont().getSize() + 1f));
                    label4.setForeground(new Color(61, 61, 61));
                    panel4.add(label4);
                    label4.setBounds(25, 75, 85, 30);

                    //---- label5 ----
                    label5.setText("\u710a\u63a5\u6750\u6599");
                    label5.setBackground(new Color(204, 204, 204));
                    label5.setOpaque(true);
                    label5.setHorizontalTextPosition(SwingConstants.CENTER);
                    label5.setHorizontalAlignment(SwingConstants.CENTER);
                    label5.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label5.setFont(label5.getFont().deriveFont(label5.getFont().getStyle() | Font.BOLD, label5.getFont().getSize() + 1f));
                    label5.setForeground(new Color(61, 61, 61));
                    panel4.add(label5);
                    label5.setBounds(25, 120, 85, 30);

                    //---- label6 ----
                    label6.setText("\u8f85\u6750");
                    label6.setBackground(new Color(204, 204, 204));
                    label6.setOpaque(true);
                    label6.setHorizontalTextPosition(SwingConstants.CENTER);
                    label6.setHorizontalAlignment(SwingConstants.CENTER);
                    label6.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label6.setFont(label6.getFont().deriveFont(label6.getFont().getStyle() | Font.BOLD, label6.getFont().getSize() + 1f));
                    label6.setForeground(new Color(61, 61, 61));
                    panel4.add(label6);
                    label6.setBounds(25, 165, 85, 30);

                    //---- label7 ----
                    label7.setText("\u5de5\u4ef6\u539a\u5ea6");
                    label7.setBackground(new Color(204, 204, 204));
                    label7.setOpaque(true);
                    label7.setHorizontalTextPosition(SwingConstants.CENTER);
                    label7.setHorizontalAlignment(SwingConstants.CENTER);
                    label7.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label7.setFont(label7.getFont().deriveFont(label7.getFont().getStyle() | Font.BOLD, label7.getFont().getSize() + 1f));
                    label7.setForeground(new Color(61, 61, 61));
                    panel4.add(label7);
                    label7.setBounds(25, 210, 85, 30);

                    //---- label8 ----
                    label8.setText("\u710a\u63a5\u4f4d\u7f6e");
                    label8.setBackground(new Color(204, 204, 204));
                    label8.setOpaque(true);
                    label8.setHorizontalTextPosition(SwingConstants.CENTER);
                    label8.setHorizontalAlignment(SwingConstants.CENTER);
                    label8.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label8.setFont(label8.getFont().deriveFont(label8.getFont().getStyle() | Font.BOLD, label8.getFont().getSize() + 1f));
                    label8.setForeground(new Color(61, 61, 61));
                    panel4.add(label8);
                    label8.setBounds(25, 255, 85, 30);

                    //---- label9 ----
                    label9.setText("\u63a5\u5934\u3001\u5761\u53e3");
                    label9.setBackground(new Color(204, 204, 204));
                    label9.setOpaque(true);
                    label9.setHorizontalTextPosition(SwingConstants.CENTER);
                    label9.setHorizontalAlignment(SwingConstants.CENTER);
                    label9.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label9.setFont(label9.getFont().deriveFont(label9.getFont().getStyle() | Font.BOLD, label9.getFont().getSize() + 1f));
                    label9.setForeground(new Color(61, 61, 61));
                    panel4.add(label9);
                    label9.setBounds(25, 300, 85, 30);

                    //---- label10 ----
                    label10.setText("\u70ed\u5de5\u827a");
                    label10.setBackground(new Color(204, 204, 204));
                    label10.setOpaque(true);
                    label10.setHorizontalTextPosition(SwingConstants.CENTER);
                    label10.setHorizontalAlignment(SwingConstants.CENTER);
                    label10.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label10.setFont(label10.getFont().deriveFont(label10.getFont().getStyle() | Font.BOLD, label10.getFont().getSize() + 1f));
                    label10.setForeground(new Color(61, 61, 61));
                    panel4.add(label10);
                    label10.setBounds(25, 345, 85, 30);

                    //---- label11 ----
                    label11.setText("\u5176\u4ed6");
                    label11.setBackground(new Color(204, 204, 204));
                    label11.setOpaque(true);
                    label11.setHorizontalTextPosition(SwingConstants.CENTER);
                    label11.setHorizontalAlignment(SwingConstants.CENTER);
                    label11.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label11.setFont(label11.getFont().deriveFont(label11.getFont().getStyle() | Font.BOLD, label11.getFont().getSize() + 1f));
                    label11.setForeground(new Color(61, 61, 61));
                    panel4.add(label11);
                    label11.setBounds(25, 390, 85, 30);

                    //---- label12 ----
                    label12.setText("A");
                    panel4.add(label12);
                    label12.setBounds(120, 30, 10, 30);

                    //---- label13 ----
                    label13.setText("B");
                    panel4.add(label13);
                    label13.setBounds(285, 30, 10, 30);

                    //---- comboBox1 ----
                    comboBox1.setSelectedIndex(-1);
                    comboBox1.setEnabled(false);
                    comboBox1.addItemListener(e -> comboBox1ItemStateChanged(e));
                    panel4.add(comboBox1);
                    comboBox1.setBounds(135, 30, 135, 30);

                    //---- comboBox2 ----
                    comboBox2.setSelectedIndex(-1);
                    comboBox2.setEnabled(false);
                    comboBox2.addItemListener(e -> comboBox2ItemStateChanged(e));
                    panel4.add(comboBox2);
                    comboBox2.setBounds(300, 30, 135, 30);

                    //---- comboBox3 ----
                    comboBox3.setSelectedIndex(-1);
                    comboBox3.setEnabled(false);
                    comboBox3.addItemListener(e -> comboBox3ItemStateChanged(e));
                    panel4.add(comboBox3);
                    comboBox3.setBounds(135, 75, 300, 30);

                    //---- comboBox4 ----
                    comboBox4.setSelectedIndex(-1);
                    comboBox4.setEnabled(false);
                    comboBox4.addItemListener(e -> comboBox4ItemStateChanged(e));
                    panel4.add(comboBox4);
                    comboBox4.setBounds(135, 120, 300, 30);

                    //---- comboBox5 ----
                    comboBox5.setSelectedIndex(-1);
                    comboBox5.setEnabled(false);
                    comboBox5.addItemListener(e -> comboBox5ItemStateChanged(e));
                    panel4.add(comboBox5);
                    comboBox5.setBounds(135, 165, 300, 30);

                    //---- comboBox6 ----
                    comboBox6.setSelectedIndex(-1);
                    comboBox6.setEnabled(false);
                    comboBox6.addItemListener(e -> {
			comboBox6ItemStateChanged(e);
			comboBox6_2ItemStateChanged(e);
			comboBox6_3ItemStateChanged(e);
		});
                    panel4.add(comboBox6);
                    comboBox6.setBounds(135, 210, 300, 30);

                    //---- comboBox7 ----
                    comboBox7.setSelectedIndex(-1);
                    comboBox7.setEnabled(false);
                    panel4.add(comboBox7);
                    comboBox7.setBounds(135, 255, 300, 30);

                    //---- comboBox8 ----
                    comboBox8.setSelectedIndex(-1);
                    comboBox8.setEnabled(false);
                    panel4.add(comboBox8);
                    comboBox8.setBounds(135, 300, 135, 30);

                    //---- comboBox9 ----
                    comboBox9.setSelectedIndex(-1);
                    comboBox9.setEnabled(false);
                    comboBox9.addItemListener(e -> comboBox9ItemStateChanged(e));
                    panel4.add(comboBox9);
                    comboBox9.setBounds(300, 300, 135, 30);

                    //---- comboBox10 ----
                    comboBox10.setSelectedIndex(-1);
                    comboBox10.setEnabled(false);
                    panel4.add(comboBox10);
                    comboBox10.setBounds(135, 345, 300, 30);

                    //---- comboBox11 ----
                    comboBox11.setEditable(true);
                    comboBox11.setSelectedIndex(-1);
                    comboBox11.setEnabled(false);
                    panel4.add(comboBox11);
                    comboBox11.setBounds(135, 390, 300, 30);

                    //---- label14 ----
                    label14.setText("\u710a\u63a5\u53c2\u6570 ");
                    label14.setBackground(new Color(204, 204, 204));
                    label14.setOpaque(true);
                    label14.setHorizontalTextPosition(SwingConstants.CENTER);
                    label14.setHorizontalAlignment(SwingConstants.CENTER);
                    label14.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label14.setFont(label14.getFont().deriveFont(label14.getFont().getStyle() | Font.BOLD, label14.getFont().getSize() + 2f));
                    panel4.add(label14);
                    label14.setBounds(520, 120, 100, 30);

                    //---- label15 ----
                    label15.setText("\u710a\u63a5\u7535\u6d41");
                    label15.setBackground(new Color(204, 204, 204));
                    label15.setOpaque(true);
                    label15.setHorizontalTextPosition(SwingConstants.CENTER);
                    label15.setHorizontalAlignment(SwingConstants.CENTER);
                    label15.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label15.setFont(label15.getFont().deriveFont(label15.getFont().getStyle() | Font.BOLD, label15.getFont().getSize() + 1f));
                    label15.setForeground(new Color(61, 61, 61));
                    panel4.add(label15);
                    label15.setBounds(520, 165, 85, 30);

                    //---- label16 ----
                    label16.setText("\u710a\u63a5\u901f\u5ea6");
                    label16.setBackground(new Color(204, 204, 204));
                    label16.setOpaque(true);
                    label16.setHorizontalTextPosition(SwingConstants.CENTER);
                    label16.setHorizontalAlignment(SwingConstants.CENTER);
                    label16.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label16.setFont(label16.getFont().deriveFont(label16.getFont().getStyle() | Font.BOLD, label16.getFont().getSize() + 1f));
                    label16.setForeground(new Color(61, 61, 61));
                    panel4.add(label16);
                    label16.setBounds(520, 255, 85, 30);

                    //---- label17 ----
                    label17.setText("\u710a\u63a5\u7535\u538b");
                    label17.setBackground(new Color(204, 204, 204));
                    label17.setOpaque(true);
                    label17.setHorizontalTextPosition(SwingConstants.CENTER);
                    label17.setHorizontalAlignment(SwingConstants.CENTER);
                    label17.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label17.setFont(label17.getFont().deriveFont(label17.getFont().getStyle() | Font.BOLD, label17.getFont().getSize() + 1f));
                    label17.setForeground(new Color(61, 61, 61));
                    panel4.add(label17);
                    label17.setBounds(520, 210, 85, 30);

                    //---- label18 ----
                    label18.setText("\u5e72\u4f38\u51fa\u91cf");
                    label18.setBackground(new Color(204, 204, 204));
                    label18.setOpaque(true);
                    label18.setHorizontalTextPosition(SwingConstants.CENTER);
                    label18.setHorizontalAlignment(SwingConstants.CENTER);
                    label18.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label18.setFont(label18.getFont().deriveFont(label18.getFont().getStyle() | Font.BOLD, label18.getFont().getSize() + 1f));
                    label18.setForeground(new Color(61, 61, 61));
                    panel4.add(label18);
                    label18.setBounds(520, 300, 85, 30);

                    //---- label19 ----
                    label19.setText("\u9608\u503climit");
                    label19.setBackground(new Color(204, 204, 204));
                    label19.setOpaque(true);
                    label19.setHorizontalTextPosition(SwingConstants.CENTER);
                    label19.setHorizontalAlignment(SwingConstants.CENTER);
                    label19.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label19.setFont(label19.getFont().deriveFont(label19.getFont().getStyle() | Font.BOLD, label19.getFont().getSize() + 1f));
                    label19.setForeground(new Color(61, 61, 61));
                    panel4.add(label19);
                    label19.setBounds(520, 345, 85, 30);

                    //---- comboBox12 ----
                    comboBox12.setSelectedIndex(-1);
                    comboBox12.setEnabled(false);
                    panel4.add(comboBox12);
                    comboBox12.setBounds(620, 165, 170, 30);

                    //---- comboBox13 ----
                    comboBox13.setSelectedIndex(-1);
                    comboBox13.setEnabled(false);
                    panel4.add(comboBox13);
                    comboBox13.setBounds(620, 210, 170, 30);

                    //---- comboBox14 ----
                    comboBox14.setSelectedIndex(-1);
                    comboBox14.setEnabled(false);
                    panel4.add(comboBox14);
                    comboBox14.setBounds(620, 255, 170, 30);

                    //---- comboBox15 ----
                    comboBox15.setSelectedIndex(-1);
                    comboBox15.setEnabled(false);
                    panel4.add(comboBox15);
                    comboBox15.setBounds(620, 300, 170, 30);

                    //---- comboBox16 ----
                    comboBox16.setSelectedIndex(-1);
                    comboBox16.setEnabled(false);
                    comboBox16.setEditable(true);
                    panel4.add(comboBox16);
                    comboBox16.setBounds(620, 345, 170, 30);

                    //---- button6 ----
                    button6.setText("\u91cd\u8bbe");
                    button6.setFont(button6.getFont().deriveFont(button6.getFont().getStyle() | Font.BOLD, button6.getFont().getSize() - 1f));
                    button6.setForeground(new Color(51, 51, 51));
                    button6.addActionListener(e -> button6ActionPerformed(e));
                    panel4.add(button6);
                    button6.setBounds(760, 30, 60, 30);

                    //---- label20 ----
                    label20.setText("\u5efa\u8bae\u503c");
                    label20.setBackground(new Color(204, 204, 204));
                    label20.setOpaque(true);
                    label20.setHorizontalTextPosition(SwingConstants.CENTER);
                    label20.setHorizontalAlignment(SwingConstants.CENTER);
                    label20.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label20.setFont(label20.getFont().deriveFont(label20.getFont().getStyle() | Font.BOLD, label20.getFont().getSize() + 2f));
                    label20.setForeground(new Color(61, 61, 61));
                    panel4.add(label20);
                    label20.setBounds(620, 120, 170, 30);

                    //---- textField1 ----
                    textField1.setEnabled(false);
                    panel4.add(textField1);
                    textField1.setBounds(805, 165, 145, 30);

                    //---- textField2 ----
                    textField2.setEnabled(false);
                    panel4.add(textField2);
                    textField2.setBounds(805, 210, 145, 30);

                    //---- textField3 ----
                    textField3.setEnabled(false);
                    panel4.add(textField3);
                    textField3.setBounds(805, 255, 145, 30);

                    //---- textField4 ----
                    textField4.setEnabled(false);
                    panel4.add(textField4);
                    textField4.setBounds(805, 300, 145, 30);

                    //---- button7 ----
                    button7.setText("\u81ea\u5b9a\u4e49");
                    button7.setFont(button7.getFont().deriveFont(button7.getFont().getStyle() | Font.BOLD, button7.getFont().getSize() - 1f));
                    button7.setForeground(new Color(51, 51, 51));
                    button7.addActionListener(e -> button7ActionPerformed(e));
                    panel4.add(button7);
                    button7.setBounds(780, 390, 85, 30);

                    //---- label22 ----
                    label22.setText("\u4ea7\u54c1\u9009\u62e9");
                    label22.setBackground(new Color(204, 204, 204));
                    label22.setOpaque(true);
                    label22.setHorizontalTextPosition(SwingConstants.CENTER);
                    label22.setHorizontalAlignment(SwingConstants.CENTER);
                    label22.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label22.setFont(label22.getFont().deriveFont(label22.getFont().getStyle() | Font.BOLD, label22.getFont().getSize() + 1f));
                    label22.setForeground(new Color(61, 61, 61));
                    panel4.add(label22);
                    label22.setBounds(520, 30, 85, 30);

                    //---- comboBox17 ----
                    comboBox17.setSelectedIndex(-1);
                    comboBox17.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            comboBox17MouseClicked(e);
                        }
                    });
                    comboBox17.addItemListener(e -> comboBox17ItemStateChanged(e));
                    panel4.add(comboBox17);
                    comboBox17.setBounds(620, 30, 135, 30);

                    //---- label23 ----
                    label23.setText("\u6dfb\u52a0\u4ea7\u54c1");
                    label23.setBackground(new Color(204, 204, 204));
                    label23.setOpaque(true);
                    label23.setHorizontalTextPosition(SwingConstants.CENTER);
                    label23.setHorizontalAlignment(SwingConstants.CENTER);
                    label23.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label23.setFont(label23.getFont().deriveFont(label23.getFont().getStyle() | Font.BOLD, label23.getFont().getSize() + 1f));
                    label23.setForeground(new Color(61, 61, 61));
                    panel4.add(label23);
                    label23.setBounds(520, 75, 85, 30);
                    panel4.add(textField6);
                    textField6.setBounds(620, 75, 135, 30);

                    //---- button10 ----
                    button10.setText("\u8bbe\u8ba1");
                    button10.setFont(button10.getFont().deriveFont(button10.getFont().getStyle() | Font.BOLD, button10.getFont().getSize() - 1f));
                    button10.setForeground(new Color(51, 51, 51));
                    button10.addActionListener(e -> button10ActionPerformed(e));
                    panel4.add(button10);
                    button10.setBounds(760, 75, 60, 30);

                    //---- button11 ----
                    button11.setText("\u4fdd\u5b58");
                    button11.setFont(button11.getFont().deriveFont(button11.getFont().getStyle() | Font.BOLD, button11.getFont().getSize() - 1f));
                    button11.setEnabled(false);
                    button11.setForeground(new Color(51, 51, 51));
                    button11.addActionListener(e -> button11ActionPerformed(e));
                    panel4.add(button11);
                    button11.setBounds(890, 30, 60, 30);

                    //---- button12 ----
                    button12.setText("\u51fa\u53c2");
                    button12.setFont(button12.getFont().deriveFont(button12.getFont().getStyle() | Font.BOLD, button12.getFont().getSize() - 1f));
                    button12.setEnabled(false);
                    button12.setForeground(new Color(51, 51, 51));
                    button12.addActionListener(e -> button12ActionPerformed(e));
                    panel4.add(button12);
                    button12.setBounds(825, 30, 60, 30);

                    //---- button13 ----
                    button13.setText("\u51fa\u53c2");
                    button13.setFont(button13.getFont().deriveFont(button13.getFont().getStyle() | Font.BOLD, button13.getFont().getSize() - 1f));
                    button13.setEnabled(false);
                    button13.setForeground(new Color(51, 51, 51));
                    button13.addActionListener(e -> button13ActionPerformed(e));
                    panel4.add(button13);
                    button13.setBounds(825, 75, 60, 30);

                    //---- button14 ----
                    button14.setText("\u4fdd\u5b58");
                    button14.setFont(button14.getFont().deriveFont(button14.getFont().getStyle() | Font.BOLD, button14.getFont().getSize() - 1f));
                    button14.setEnabled(false);
                    button14.setForeground(new Color(51, 51, 51));
                    button14.addActionListener(e -> button14ActionPerformed(e));
                    panel4.add(button14);
                    button14.setBounds(890, 75, 60, 30);

                    //---- button15 ----
                    button15.setText("\u51fa\u53c2");
                    button15.setFont(button15.getFont().deriveFont(button15.getFont().getStyle() | Font.BOLD, button15.getFont().getSize() - 1f));
                    button15.setForeground(new Color(51, 51, 51));
                    button15.addActionListener(e -> button15ActionPerformed(e));
                    panel4.add(button15);
                    button15.setBounds(870, 390, 80, 30);

                    //---- button16 ----
                    button16.setText("\u4ec5\u8c03\u53c2");
                    button16.setFont(button16.getFont().deriveFont(button16.getFont().getStyle() | Font.BOLD, button16.getFont().getSize() - 1f));
                    button16.setForeground(new Color(51, 51, 51));
                    button16.addActionListener(e -> button16ActionPerformed(e));
                    panel4.add(button16);
                    button16.setBounds(805, 345, 70, 30);

                    //---- label21 ----
                    label21.setText("\u5b9e\u9645\u503c");
                    label21.setBackground(new Color(204, 204, 204));
                    label21.setOpaque(true);
                    label21.setHorizontalTextPosition(SwingConstants.CENTER);
                    label21.setHorizontalAlignment(SwingConstants.CENTER);
                    label21.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    label21.setFont(label21.getFont().deriveFont(label21.getFont().getStyle() | Font.BOLD, label21.getFont().getSize() + 2f));
                    label21.setForeground(new Color(61, 61, 61));
                    panel4.add(label21);
                    label21.setBounds(790, 120, 160, 30);

                    //---- button17 ----
                    button17.setText("\u4fdd\u5b58");
                    button17.setFont(button17.getFont().deriveFont(button17.getFont().getStyle() | Font.BOLD, button17.getFont().getSize() - 1f));
                    button17.setEnabled(false);
                    button17.setForeground(new Color(51, 51, 51));
                    button17.addActionListener(e -> button17ActionPerformed(e));
                    panel4.add(button17);
                    button17.setBounds(885, 345, 65, 30);

                    //---- button18 ----
                    button18.setText("\u53d6\u6d88");
                    button18.setFont(button18.getFont().deriveFont(button18.getFont().getStyle() | Font.BOLD, button18.getFont().getSize() - 1f));
                    button18.setForeground(new Color(51, 51, 51));
                    button18.addActionListener(e -> button18ActionPerformed(e));
                    panel4.add(button18);
                    button18.setBounds(520, 390, 85, 30);

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

                    //======== tabbedPane1 ========
                    {
                        tabbedPane1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 13));

                        //======== panel11 ========
                        {
                            panel11.setLayout(null);

                            //======== scrollPane8 ========
                            {

                                //---- table8 ----
                                table8.setModel(new DefaultTableModel(
                                    new Object[][] {
                                    },
                                    new String[] {
                                        "id", "name", "\u6bcd\u6750A", "\u6bcd\u6750B", "\u710a\u63a5\u65b9\u6cd5", "\u710a\u6750\u724c\u53f7", "\u8f85\u6750", "\u5de5\u4ef6\u539a\u5ea6", "\u710a\u63a5\u4f4d\u7f6e", "\u710a\u63a5\u63a5\u5934", "\u5761\u53e3\u5f62\u5f0f", "\u70ed\u5904\u7406\u7c7b\u578b", "\u5176\u4ed61", "\u710a\u63a5\u7535\u6d41\uff08\u5efa\u8bae\u503c\uff09", "\u7535\u5f27\u7535\u538b\uff08\u5efa\u8bae\u503c\uff09", "\u710a\u63a5\u901f\u5ea6\uff08\u5efa\u8bae\u503c\uff09", "\u5e72\u4f38\u51fa\u91cf\uff08\u5efa\u8bae\u503c\uff09", "\u9608\u503climit", "\u710a\u63a5\u7535\u6d41\uff08\u5b9e\u9645\u503c\uff09", "\u710a\u63a5\u7535\u538b\uff08\u5b9e\u9645\u503c\uff09", "\u710a\u63a5\u901f\u5ea6\uff08\u5b9e\u9645\u503c\uff09", "\u5e72\u4f38\u51fa\u91cf\uff08\u5b9e\u9645\u503c\uff09"
                                    }
                                ) {
                                    Class<?>[] columnTypes = new Class<?>[] {
                                        Integer.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                {
                                    TableColumnModel cm = table8.getColumnModel();
                                    cm.getColumn(0).setPreferredWidth(50);
                                    cm.getColumn(1).setPreferredWidth(100);
                                    cm.getColumn(2).setPreferredWidth(180);
                                    cm.getColumn(3).setPreferredWidth(180);
                                    cm.getColumn(4).setPreferredWidth(180);
                                    cm.getColumn(5).setPreferredWidth(180);
                                    cm.getColumn(6).setPreferredWidth(180);
                                    cm.getColumn(7).setPreferredWidth(180);
                                    cm.getColumn(8).setPreferredWidth(180);
                                    cm.getColumn(9).setPreferredWidth(180);
                                    cm.getColumn(10).setPreferredWidth(180);
                                    cm.getColumn(11).setPreferredWidth(180);
                                    cm.getColumn(12).setPreferredWidth(180);
                                    cm.getColumn(13).setPreferredWidth(180);
                                    cm.getColumn(14).setPreferredWidth(180);
                                    cm.getColumn(15).setPreferredWidth(180);
                                    cm.getColumn(16).setPreferredWidth(180);
                                    cm.getColumn(17).setPreferredWidth(180);
                                    cm.getColumn(18).setPreferredWidth(180);
                                    cm.getColumn(19).setPreferredWidth(180);
                                    cm.getColumn(20).setPreferredWidth(180);
                                    cm.getColumn(21).setPreferredWidth(180);
                                }
                                table8.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table8.setAutoCreateRowSorter(true);
                                table8.setRowHeight(20);
                                scrollPane8.setViewportView(table8);
                            }
                            panel11.add(scrollPane8);
                            scrollPane8.setBounds(0, 0, 960, 395);

                            {
                                // compute preferred size
                                Dimension preferredSize = new Dimension();
                                for(int i = 0; i < panel11.getComponentCount(); i++) {
                                    Rectangle bounds = panel11.getComponent(i).getBounds();
                                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                                }
                                Insets insets = panel11.getInsets();
                                preferredSize.width += insets.right;
                                preferredSize.height += insets.bottom;
                                panel11.setMinimumSize(preferredSize);
                                panel11.setPreferredSize(preferredSize);
                            }
                        }
                        tabbedPane1.addTab("\u4ea7\u54c1", panel11);

                        //======== panel3 ========
                        {
                            panel3.setFont(panel3.getFont().deriveFont(panel3.getFont().getSize() - 1f));
                            panel3.setLayout(null);

                            //======== scrollPane1 ========
                            {

                                //---- table1 ----
                                table1.setModel(new DefaultTableModel(
                                    new Object[][] {
                                    },
                                    new String[] {
                                        "id", "name", "seq", "\u5339\u914d\uff1a\u6bcd\u6750", "\u5339\u914d\uff1a\u710a\u63a5\u65b9\u6cd5", "\u6e29\u5ea6\u7b49\u7ea7", "\u710a\u63a5\u6027\u80fd", "\u673a\u68b0\u6027\u80fd", "\u89c4\u683c", "\u5316\u5b66\u6210\u5206", "\u5de5\u827a\u8981\u70b9"
                                    }
                                ) {
                                    Class<?>[] columnTypes = new Class<?>[] {
                                        Integer.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false, false, false, false, false, false, false, false, false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                {
                                    TableColumnModel cm = table1.getColumnModel();
                                    cm.getColumn(0).setPreferredWidth(50);
                                    cm.getColumn(1).setPreferredWidth(100);
                                    cm.getColumn(2).setPreferredWidth(100);
                                    cm.getColumn(3).setPreferredWidth(180);
                                    cm.getColumn(4).setPreferredWidth(180);
                                    cm.getColumn(5).setPreferredWidth(180);
                                    cm.getColumn(6).setPreferredWidth(180);
                                    cm.getColumn(7).setPreferredWidth(180);
                                    cm.getColumn(8).setPreferredWidth(180);
                                    cm.getColumn(9).setPreferredWidth(180);
                                    cm.getColumn(10).setPreferredWidth(180);
                                }
                                table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table1.setAutoCreateRowSorter(true);
                                table1.setRowHeight(20);
                                scrollPane1.setViewportView(table1);
                            }
                            panel3.add(scrollPane1);
                            scrollPane1.setBounds(0, 0, 960, 395);

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
                        tabbedPane1.addTab("\u6bcd\u6750", panel3);

                        //======== panel5 ========
                        {
                            panel5.setLayout(null);

                            //======== scrollPane2 ========
                            {

                                //---- table2 ----
                                table2.setModel(new DefaultTableModel(
                                    new Object[][] {
                                    },
                                    new String[] {
                                        "id", "name", "seq", "\u5339\u914d\uff1a\u710a\u63a5\u6750\u6599", "\u5339\u914d\uff1a\u8f85\u6750", "\u65b9\u6cd5\u7279\u70b9", "\u8bbe\u5907\u578b\u53f7", "\u8bbe\u5907\u53c2\u6570", "\u5e94\u7528\u8303\u56f4"
                                    }
                                ) {
                                    Class<?>[] columnTypes = new Class<?>[] {
                                        Integer.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false, false, false, false, false, false, false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                {
                                    TableColumnModel cm = table2.getColumnModel();
                                    cm.getColumn(0).setPreferredWidth(50);
                                    cm.getColumn(1).setPreferredWidth(100);
                                    cm.getColumn(2).setPreferredWidth(100);
                                    cm.getColumn(3).setPreferredWidth(180);
                                    cm.getColumn(4).setPreferredWidth(180);
                                    cm.getColumn(5).setPreferredWidth(180);
                                    cm.getColumn(6).setPreferredWidth(180);
                                    cm.getColumn(7).setPreferredWidth(180);
                                    cm.getColumn(8).setPreferredWidth(180);
                                }
                                table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table2.setAutoCreateRowSorter(true);
                                table2.setRowHeight(20);
                                scrollPane2.setViewportView(table2);
                            }
                            panel5.add(scrollPane2);
                            scrollPane2.setBounds(0, 0, 960, 395);

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
                        tabbedPane1.addTab("\u710a\u63a5\u65b9\u6cd5", panel5);

                        //======== panel6 ========
                        {
                            panel6.setLayout(null);

                            //======== scrollPane3 ========
                            {

                                //---- table3 ----
                                table3.setModel(new DefaultTableModel(
                                    new Object[][] {
                                    },
                                    new String[] {
                                        "id", "name", "seq", "\u5339\u914d\uff1a\u8f85\u6750", "\u710a\u6750\u7c7b\u578b", "\u5316\u5b66\u6210\u5206", "\u529b\u5b66\u6027\u80fd", "\u4e3b\u8981\u7528\u9014"
                                    }
                                ) {
                                    Class<?>[] columnTypes = new Class<?>[] {
                                        Integer.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false, false, false, false, false, false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                {
                                    TableColumnModel cm = table3.getColumnModel();
                                    cm.getColumn(0).setPreferredWidth(50);
                                    cm.getColumn(1).setPreferredWidth(100);
                                    cm.getColumn(2).setPreferredWidth(100);
                                    cm.getColumn(3).setPreferredWidth(180);
                                    cm.getColumn(4).setPreferredWidth(180);
                                    cm.getColumn(5).setPreferredWidth(180);
                                    cm.getColumn(6).setPreferredWidth(180);
                                    cm.getColumn(7).setPreferredWidth(180);
                                }
                                table3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table3.setAutoCreateRowSorter(true);
                                table3.setRowHeight(20);
                                scrollPane3.setViewportView(table3);
                            }
                            panel6.add(scrollPane3);
                            scrollPane3.setBounds(0, 0, 960, 400);

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
                        tabbedPane1.addTab("\u710a\u63a5\u6750\u6599", panel6);

                        //======== panel7 ========
                        {
                            panel7.setLayout(null);

                            //======== scrollPane4 ========
                            {

                                //---- table4 ----
                                table4.setModel(new DefaultTableModel(
                                    new Object[][] {
                                    },
                                    new String[] {
                                        "id", "name", "seq", "app", "\u53c2\u6570"
                                    }
                                ) {
                                    Class<?>[] columnTypes = new Class<?>[] {
                                        Integer.class, Object.class, Object.class, Object.class, Object.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false, false, false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                {
                                    TableColumnModel cm = table4.getColumnModel();
                                    cm.getColumn(0).setPreferredWidth(50);
                                    cm.getColumn(1).setPreferredWidth(100);
                                    cm.getColumn(2).setPreferredWidth(100);
                                    cm.getColumn(3).setPreferredWidth(300);
                                    cm.getColumn(4).setPreferredWidth(430);
                                }
                                table4.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table4.setAutoCreateRowSorter(true);
                                table4.setRowHeight(20);
                                scrollPane4.setViewportView(table4);
                            }
                            panel7.add(scrollPane4);
                            scrollPane4.setBounds(0, 0, 960, 395);

                            {
                                // compute preferred size
                                Dimension preferredSize = new Dimension();
                                for(int i = 0; i < panel7.getComponentCount(); i++) {
                                    Rectangle bounds = panel7.getComponent(i).getBounds();
                                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                                }
                                Insets insets = panel7.getInsets();
                                preferredSize.width += insets.right;
                                preferredSize.height += insets.bottom;
                                panel7.setMinimumSize(preferredSize);
                                panel7.setPreferredSize(preferredSize);
                            }
                        }
                        tabbedPane1.addTab("\u8f85\u6750", panel7);

                        //======== panel8 ========
                        {
                            panel8.setLayout(null);

                            //======== scrollPane5 ========
                            {

                                //---- table5 ----
                                table5.setModel(new DefaultTableModel(
                                    new Object[][] {
                                    },
                                    new String[] {
                                        "id", "name", "seq", "\u5339\u914d\uff1a\u63a5\u5934\u5f62\u5f0f\u3001\u5761\u53e3\u5f62\u5f0f\u3001\u710a\u63a5\u4f4d\u7f6e", "\u53c2\u6570"
                                    }
                                ) {
                                    Class<?>[] columnTypes = new Class<?>[] {
                                        Integer.class, Object.class, Object.class, Object.class, Object.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false, false, false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                {
                                    TableColumnModel cm = table5.getColumnModel();
                                    cm.getColumn(0).setPreferredWidth(50);
                                    cm.getColumn(1).setPreferredWidth(100);
                                    cm.getColumn(2).setPreferredWidth(100);
                                    cm.getColumn(3).setPreferredWidth(370);
                                    cm.getColumn(4).setPreferredWidth(350);
                                }
                                table5.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table5.setAutoCreateRowSorter(true);
                                table5.setRowHeight(20);
                                scrollPane5.setViewportView(table5);
                            }
                            panel8.add(scrollPane5);
                            scrollPane5.setBounds(0, 0, 960, 395);

                            {
                                // compute preferred size
                                Dimension preferredSize = new Dimension();
                                for(int i = 0; i < panel8.getComponentCount(); i++) {
                                    Rectangle bounds = panel8.getComponent(i).getBounds();
                                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                                }
                                Insets insets = panel8.getInsets();
                                preferredSize.width += insets.right;
                                preferredSize.height += insets.bottom;
                                panel8.setMinimumSize(preferredSize);
                                panel8.setPreferredSize(preferredSize);
                            }
                        }
                        tabbedPane1.addTab("\u5de5\u4ef6\u539a\u5ea6", panel8);

                        //======== panel9 ========
                        {
                            panel9.setLayout(null);

                            //======== scrollPane6 ========
                            {

                                //---- table6 ----
                                table6.setModel(new DefaultTableModel(
                                    new Object[][] {
                                    },
                                    new String[] {
                                        "id", "name", "seq", "\u5339\u914d\uff1a", "\u63a5\u5934\u5f62\u5f0f", "\u5761\u53e3\u5f62\u5f0f", "\u5761\u53e3\u53c2\u6570", "\u710a\u63a5\u4f4d\u7f6e"
                                    }
                                ) {
                                    Class<?>[] columnTypes = new Class<?>[] {
                                        Integer.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false, false, false, false, false, false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                {
                                    TableColumnModel cm = table6.getColumnModel();
                                    cm.getColumn(0).setPreferredWidth(50);
                                    cm.getColumn(1).setPreferredWidth(100);
                                    cm.getColumn(2).setPreferredWidth(100);
                                    cm.getColumn(3).setPreferredWidth(180);
                                    cm.getColumn(4).setPreferredWidth(180);
                                    cm.getColumn(5).setPreferredWidth(180);
                                    cm.getColumn(6).setPreferredWidth(180);
                                    cm.getColumn(7).setPreferredWidth(180);
                                }
                                table6.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table6.setAutoCreateRowSorter(true);
                                table6.setRowHeight(20);
                                scrollPane6.setViewportView(table6);
                            }
                            panel9.add(scrollPane6);
                            scrollPane6.setBounds(0, 0, 960, 395);

                            {
                                // compute preferred size
                                Dimension preferredSize = new Dimension();
                                for(int i = 0; i < panel9.getComponentCount(); i++) {
                                    Rectangle bounds = panel9.getComponent(i).getBounds();
                                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                                }
                                Insets insets = panel9.getInsets();
                                preferredSize.width += insets.right;
                                preferredSize.height += insets.bottom;
                                panel9.setMinimumSize(preferredSize);
                                panel9.setPreferredSize(preferredSize);
                            }
                        }
                        tabbedPane1.addTab("\u710a\u63a5\u63a5\u5934\u3001\u4f4d\u7f6e\u3001\u5761\u53e3", panel9);

                        //======== panel10 ========
                        {
                            panel10.setLayout(null);

                            //======== scrollPane7 ========
                            {

                                //---- table7 ----
                                table7.setModel(new DefaultTableModel(
                                    new Object[][] {
                                    },
                                    new String[] {
                                        "id", "name", "seq", "\u5339\u914d\uff1a", "\u70ed\u5904\u7406\u7c7b\u578b", "\u9884\u70ed\u6e29\u5ea6", "\u9884\u70ed\u65f6\u95f4", "\u5c42\u95f4\u6e29\u5ea6", "\u70ed\u5904\u7406\u6e29\u5ea6", "\u70ed\u5904\u7406\u4fdd\u6e29\u65f6\u95f4"
                                    }
                                ) {
                                    Class<?>[] columnTypes = new Class<?>[] {
                                        Integer.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false, false, false, false, false, false, false, false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                {
                                    TableColumnModel cm = table7.getColumnModel();
                                    cm.getColumn(0).setPreferredWidth(50);
                                    cm.getColumn(1).setPreferredWidth(100);
                                    cm.getColumn(2).setPreferredWidth(100);
                                    cm.getColumn(3).setPreferredWidth(180);
                                    cm.getColumn(4).setPreferredWidth(180);
                                    cm.getColumn(5).setPreferredWidth(180);
                                    cm.getColumn(6).setPreferredWidth(180);
                                    cm.getColumn(7).setPreferredWidth(180);
                                    cm.getColumn(8).setPreferredWidth(180);
                                    cm.getColumn(9).setPreferredWidth(180);
                                }
                                table7.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table7.setAutoCreateRowSorter(true);
                                table7.setRowHeight(20);
                                scrollPane7.setViewportView(table7);
                            }
                            panel10.add(scrollPane7);
                            scrollPane7.setBounds(0, 0, 960, 400);

                            {
                                // compute preferred size
                                Dimension preferredSize = new Dimension();
                                for(int i = 0; i < panel10.getComponentCount(); i++) {
                                    Rectangle bounds = panel10.getComponent(i).getBounds();
                                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                                }
                                Insets insets = panel10.getInsets();
                                preferredSize.width += insets.right;
                                preferredSize.height += insets.bottom;
                                panel10.setMinimumSize(preferredSize);
                                panel10.setPreferredSize(preferredSize);
                            }
                        }
                        tabbedPane1.addTab("\u70ed\u5de5\u827a", panel10);

                        //======== panel12 ========
                        {
                            panel12.setLayout(null);

                            //======== scrollPane9 ========
                            {

                                //---- table9 ----
                                table9.setModel(new DefaultTableModel(
                                    new Object[][] {
                                    },
                                    new String[] {
                                        "id", "name", "seq", "\u5339\u914d\uff1a", "\u710a\u63a5\u7535\u6d41", "\u7535\u6d41\u79cd\u7c7b", "\u7535\u6d41\u6781\u6027", "\u7535\u5f27\u7535\u538b", "\u7ebf\u80fd\u91cf", "\u710a\u63a5\u901f\u5ea6", "\u5e72\u4f38\u51fa\u91cf"
                                    }
                                ) {
                                    Class<?>[] columnTypes = new Class<?>[] {
                                        Integer.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false, false, false, false, false, false, false, false, false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                {
                                    TableColumnModel cm = table9.getColumnModel();
                                    cm.getColumn(0).setPreferredWidth(50);
                                    cm.getColumn(1).setPreferredWidth(100);
                                    cm.getColumn(2).setPreferredWidth(100);
                                    cm.getColumn(3).setPreferredWidth(180);
                                    cm.getColumn(4).setPreferredWidth(180);
                                    cm.getColumn(5).setPreferredWidth(180);
                                    cm.getColumn(6).setPreferredWidth(180);
                                    cm.getColumn(7).setPreferredWidth(180);
                                    cm.getColumn(8).setPreferredWidth(180);
                                    cm.getColumn(9).setPreferredWidth(180);
                                    cm.getColumn(10).setPreferredWidth(180);
                                }
                                table9.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table9.setAutoCreateRowSorter(true);
                                table9.setRowHeight(20);
                                scrollPane9.setViewportView(table9);
                            }
                            panel12.add(scrollPane9);
                            scrollPane9.setBounds(0, 0, 960, 400);

                            {
                                // compute preferred size
                                Dimension preferredSize = new Dimension();
                                for(int i = 0; i < panel12.getComponentCount(); i++) {
                                    Rectangle bounds = panel12.getComponent(i).getBounds();
                                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                                }
                                Insets insets = panel12.getInsets();
                                preferredSize.width += insets.right;
                                preferredSize.height += insets.bottom;
                                panel12.setMinimumSize(preferredSize);
                                panel12.setPreferredSize(preferredSize);
                            }
                        }
                        tabbedPane1.addTab("\u710a\u63a5\u53c2\u6570", panel12);
                    }
                    panel2.add(tabbedPane1);
                    tabbedPane1.setBounds(0, 35, 965, 425);

                    //---- textField11 ----
                    textField11.setBackground(Color.white);
                    panel2.add(textField11);
                    textField11.setBounds(70, 4, 125, 30);

                    //---- button21 ----
                    button21.setText("\u641c\u7d22");
                    button21.setForeground(new Color(51, 51, 51));
                    button21.setFont(button21.getFont().deriveFont(button21.getFont().getStyle() | Font.BOLD));
                    button21.addActionListener(e -> button21ActionPerformed(e));
                    panel2.add(button21);
                    button21.setBounds(195, 4, 70, 30);

                    //---- label35 ----
                    label35.setText("\uff08\u8bf7\u5148\u8df3\u8f6c\u5230\u6240\u5c5e\u5b50\u9875\uff09");
                    label35.setFont(label35.getFont().deriveFont(label35.getFont().getSize() - 2f));
                    panel2.add(label35);
                    label35.setBounds(330, 7, 155, 23);

                    //---- button20 ----
                    button20.setText("\u6dfb\u52a0");
                    button20.setFont(button20.getFont().deriveFont(button20.getFont().getStyle() | Font.BOLD));
                    button20.setForeground(new Color(51, 51, 51));
                    button20.addActionListener(e -> button20ActionPerformed(e));
                    panel2.add(button20);
                    button20.setBounds(590, 4, 70, 30);

                    //---- button22 ----
                    button22.setText("\u4fee\u6539");
                    button22.setFont(button22.getFont().deriveFont(button22.getFont().getStyle() | Font.BOLD));
                    button22.setForeground(new Color(51, 51, 51));
                    button22.addActionListener(e -> button22ActionPerformed(e));
                    panel2.add(button22);
                    button22.setBounds(740, 4, 70, 30);

                    //---- button23 ----
                    button23.setText("\u5237\u65b0");
                    button23.setFont(button23.getFont().deriveFont(button23.getFont().getStyle() | Font.BOLD));
                    button23.setForeground(new Color(51, 51, 51));
                    button23.addActionListener(e -> button42ActionPerformed(e));
                    panel2.add(button23);
                    button23.setBounds(505, 4, 70, 30);

                    //---- button24 ----
                    button24.setText("\u8fd4\u56de");
                    button24.setFont(button24.getFont().deriveFont(button24.getFont().getStyle() | Font.BOLD));
                    button24.setForeground(new Color(51, 51, 51));
                    button24.addActionListener(e -> button24ActionPerformed(e));
                    panel2.add(button24);
                    button24.setBounds(265, 4, 70, 30);

                    //---- label24 ----
                    label24.setText("\u8868\u5185\u641c\u7d22");
                    label24.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 13));
                    label24.setForeground(new Color(51, 51, 51));
                    panel2.add(label24);
                    label24.setBounds(10, 8, 58, 23);

                    //---- button5 ----
                    button5.setText("\u786e\u5b9a");
                    button5.setEnabled(false);
                    button5.setFont(button5.getFont().deriveFont(button5.getFont().getStyle() | Font.BOLD));
                    button5.setForeground(new Color(51, 51, 51));
                    button5.addActionListener(e -> button5ActionPerformed(e));
                    panel2.add(button5);
                    button5.setBounds(820, 4, 70, 30);

                    //---- button9 ----
                    button9.setText("\u5220\u9664");
                    button9.setFont(button9.getFont().deriveFont(button9.getFont().getStyle() | Font.BOLD));
                    button9.setForeground(new Color(51, 51, 51));
                    button9.addActionListener(e -> button9ActionPerformed(e));
                    panel2.add(button9);
                    button9.setBounds(665, 4, 70, 30);

                    //---- button19 ----
                    button19.setText("\u8fd4\u56de");
                    button19.setEnabled(false);
                    button19.setFont(button19.getFont().deriveFont(button19.getFont().getStyle() | Font.BOLD));
                    button19.setForeground(new Color(51, 51, 51));
                    button19.addActionListener(e -> button19ActionPerformed(e));
                    panel2.add(button19);
                    button19.setBounds(895, 4, 70, 30);

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
            panel13.add(tabbedPane2);
            tabbedPane2.setBounds(2, 95, 970, 490);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel13.getComponentCount(); i++) {
                    Rectangle bounds = panel13.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel13.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel13.setMinimumSize(preferredSize);
                panel13.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel13);
        panel13.setBounds(0, 0, 985, 595);

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
    private JPanel panel13;
    private JPanel panel1;
    private JLabel label2;
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JLabel label3;
    private JButton button4;
    private JButton button3;
    private JButton button2;
    private JButton button1;
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
    private JButton button6;
    private JLabel label20;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton button7;
    private JLabel label22;
    private JComboBox comboBox17;
    private JLabel label23;
    private JTextField textField6;
    private JButton button10;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button14;
    private JButton button15;
    private JButton button16;
    private JLabel label21;
    private JButton button17;
    private JButton button18;
    private JPanel panel2;
    private JTabbedPane tabbedPane1;
    private JPanel panel11;
    private JScrollPane scrollPane8;
    private JTable table8;
    private JPanel panel3;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel5;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JPanel panel6;
    private JScrollPane scrollPane3;
    private JTable table3;
    private JPanel panel7;
    private JScrollPane scrollPane4;
    private JTable table4;
    private JPanel panel8;
    private JScrollPane scrollPane5;
    private JTable table5;
    private JPanel panel9;
    private JScrollPane scrollPane6;
    private JTable table6;
    private JPanel panel10;
    private JScrollPane scrollPane7;
    private JTable table7;
    private JPanel panel12;
    private JScrollPane scrollPane9;
    private JTable table9;
    private JTextField textField11;
    private JButton button21;
    private JLabel label35;
    private JButton button20;
    private JButton button22;
    private JButton button23;
    private JButton button24;
    private JLabel label24;
    private JButton button5;
    private JButton button9;
    private JButton button19;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
