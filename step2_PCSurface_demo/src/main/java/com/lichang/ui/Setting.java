/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import com.lichang.utils.ChangePasswordUtil;
import com.lichang.utils.IOUtil;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.SettingUtils.SettingUtil;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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

    private List<Map<String, Object>> employee_mapList; //员工用户表
    private List<Map<String, Object>> admin_mapList; //管理员用户表
    private JDialog jDialog3; //员工表 添加
    private JDialog jDialog4; //员工表 删除

    //无参（预设账户信息）
    public Setting() {
        username = "admin";
        adminFlag = true;

        initComponents();

        this.setBounds(273, 95, 990, 625);
        setVisible(true);
    }

    //有参（接收登录账户信息）
    public Setting(String username, Boolean adminFlag) {
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
        log.debug("MenuItem 用户设置： 更改密码");

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
        //焊机管理
        updateComboBox1(); //当前焊机：焊机名称
        displayNowMachine(); //当前焊机：显示当前状态
        updateComboBox2(); //修改焊机：焊机名称
        //用户信息管理
        updateTable1(); //员工表格
        updateTable2(); //管理员表格
        //数据库设置
        initDBSetting(); //初始化数据库设置
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
            JOptionPane.showMessageDialog(this, "修改失败!", "提示", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "请输入焊机状态!", "提示", JOptionPane.WARNING_MESSAGE);
        } else if (comboBox2.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "请选择焊机名称!", "提示", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "修改失败!", "提示", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "添加失败!", "提示", JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * 用户管理
     */
    //员工用户: 加载表格
    private void updateTable1() {
        //设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        table1.setDefaultRenderer(Object.class, r);

        employee_mapList = SettingUtil.getData("employee"); //获得表内容
        //非空判断
        if (employee_mapList == null || employee_mapList.size() == 0) {
            return;
        }

        DefaultTableModel table1Model = (DefaultTableModel) table1.getModel(); //获取model
        table1Model.setRowCount(0); //先清空，再加载新数据

        for (Map<String, Object> map : employee_mapList) {
            Object[] newRowData = {
                    map.get("id"),
                    map.get("username"),
                    map.get("password")
            };

            table1Model.addRow(newRowData);
        }
    }

    //员工用户：修改
    private void button6ActionPerformed(ActionEvent e) {
        //开使能
        table1.setEnabled(true); //表格
        button11.setEnabled(true); //确定
        //去使能
        button6.setEnabled(false); //修改
        button7.setEnabled(false); //删除
        button10.setEnabled(false); //添加

        JOptionPane.showMessageDialog(this, "用户表已可进行编辑，修改完成后，请点击 确定\n" +
                "用户名格式： 2~10长度，可使用 字母、数字、下划线，需以字母开头\n" +
                "密码格式：2~10长度，可使用 字母、数字、下划线");
    }

    //员工用户：确定
    private void button11ActionPerformed(ActionEvent e) {
        boolean updateResult = true;
        for (int i = 0; i < table1.getRowCount(); i++) {
            int id = (int) table1.getValueAt(i, 0);
            String username = (String) table1.getValueAt(i, 1);
            String password = (String) table1.getValueAt(i, 2);

            //用户名 与 密码 合规验证
            if (!Pattern.matches("^[a-zA-Z][a-zA-Z0-9_]{1,10}$", username)) {
                JOptionPane.showMessageDialog(this, "用户名格式错误，请重新输入", "提示", JOptionPane.WARNING_MESSAGE);
                updateTable1(); //修改有问题，更新，重新修改
                return;
            }
            if (!Pattern.matches("[a-zA-Z0-9_]{2,10}$", password)) {
                JOptionPane.showMessageDialog(this, "密码格式错误，请重新输入", "提示", JOptionPane.WARNING_MESSAGE);
                updateTable1(); //修改有问题，更新，重新修改
                return;
            }

            boolean result = SettingUtil.updateUser("employee", id, username, password);
            if (!result) {
                updateResult = false;
            }
        }
        if (!updateResult) {
            JOptionPane.showMessageDialog(this, "修改失败！");
        } else {
            JOptionPane.showMessageDialog(this, "修改成功！");
        }
        updateTable1(); //修改后，再次更新一下
        //去使能
        table1.setEnabled(false); //表格
        button11.setEnabled(false); //确定
        //开使能
        button6.setEnabled(true); //修改
        button7.setEnabled(true); //删除
        button10.setEnabled(true); //添加
    }

    //员工用户：添加
    private void button10ActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
                "用户名格式： 2~10长度，可使用 字母、数字、下划线，需以字母开头\n" +
                        "密码格式：2~10长度，可使用 字母、数字、下划线");

        jDialog3 = new JDialog(this, "添加用户", true);
        JPanel addUserPanel = new JPanel();

        addUserPanel.setLayout(null);

        //用户名Label
        JLabel usernameLabel = new JLabel("用户名： ");
        addUserPanel.add(usernameLabel);
        usernameLabel.setBounds(40, 60, 110, 30);
        usernameLabel.setFont(new Font("", Font.BOLD, 15));


        //密码Label
        JLabel passwordLabel = new JLabel("密码:  ");
        addUserPanel.add(passwordLabel);
        passwordLabel.setBounds(40, 120, 110, 30);
        passwordLabel.setFont(new Font("", Font.BOLD, 15));

        //用户名
        JTextField usernameField = new JTextField();
        addUserPanel.add(usernameField);
        usernameField.setBounds(120, 60, 140, 30);
        usernameField.setColumns(10);
        usernameField.setFont(new Font("黑体", Font.PLAIN, 15));

        //密码
        JTextField passwordField = new JTextField();
        addUserPanel.add(passwordField);
        passwordField.setBounds(120, 120, 140, 30);
        passwordField.setColumns(10);
        passwordField.setFont(new Font("黑体", Font.PLAIN, 15));

        //确定 按钮
        JButton enterButton = new JButton("确定");
        addUserPanel.add(enterButton);
        enterButton.setBounds(280, 60, 80, 90);
        enterButton.setFont(new Font("黑体", Font.PLAIN, 18));

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                //用户名 与 密码 合规验证
                if (!Pattern.matches("^[a-zA-Z][a-zA-Z0-9_]{1,10}$", username)) {
                    JOptionPane.showMessageDialog(jDialog3, "用户名格式错误，请重新输入", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!Pattern.matches("[a-zA-Z0-9_]{2,10}$", password)) {
                    JOptionPane.showMessageDialog(jDialog3, "密码格式错误，请重新输入", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //已有用户名验证
                for (Map<String, Object> map : employee_mapList) {
                    if (map.get("username").equals(username)) {
                        JOptionPane.showMessageDialog(jDialog3, "该用户名已存在！请检查后重新输入", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                boolean result = SettingUtil.insertNewUser("employee", username, password);
                if (result) {
                    JOptionPane.showMessageDialog(jDialog3, "添加成功！");
                } else {
                    JOptionPane.showMessageDialog(jDialog3, "添加失败！", "提示", JOptionPane.WARNING_MESSAGE);
                }

                updateTable1(); //添加后，更新员工表格
            }
        });

        jDialog3.setSize(400, 250);
        jDialog3.setAlwaysOnTop(true);
        jDialog3.setLocationRelativeTo(null);
        jDialog3.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jDialog3.add(addUserPanel);
        jDialog3.setVisible(true);
    }

    //员工用户：删除
    private void button7ActionPerformed(ActionEvent e) {
        jDialog4 = new JDialog(this, "添加用户", true);
        JPanel deleteUserPanel = new JPanel();

        deleteUserPanel.setLayout(null);

        //用户名Label
        JLabel usernameLabel = new JLabel("用户名： ");
        deleteUserPanel.add(usernameLabel);
        usernameLabel.setBounds(40, 60, 110, 30);
        usernameLabel.setFont(new Font("", Font.BOLD, 15));

        //用户名
        JTextField usernameField = new JTextField();
        deleteUserPanel.add(usernameField);
        usernameField.setBounds(120, 60, 140, 30);
        usernameField.setColumns(10);
        usernameField.setFont(new Font("黑体", Font.PLAIN, 15));

        //确定 按钮
        JButton enterButton = new JButton("确定");
        deleteUserPanel.add(enterButton);
        enterButton.setBounds(280, 60, 80, 30);
        enterButton.setFont(new Font("黑体", Font.PLAIN, 16));

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                //用户名合规判断
                if (username == null || username.equals("")) {
                    JOptionPane.showMessageDialog(jDialog3, "请输入用户名！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean userExistFlag = false;
                for (Map<String, Object> map : employee_mapList) {
                    if (map.get("username").equals(username)) {
                        userExistFlag = true;
                    }
                }
                if (!userExistFlag) {
                    JOptionPane.showMessageDialog(jDialog3, "该用户名不存在！请检查后重新输入", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean result = SettingUtil.deleteUser("employee", username);
                if (result) {
                    JOptionPane.showMessageDialog(jDialog3, "删除成功!", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(jDialog3, "删除失败！", "提示", JOptionPane.WARNING_MESSAGE);
                }

                updateTable1(); //删除后，更新员工表格
            }
        });

        jDialog4.setSize(400, 200);
        jDialog4.setAlwaysOnTop(true);
        jDialog4.setLocationRelativeTo(null);
        jDialog4.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jDialog4.add(deleteUserPanel);
        jDialog4.setVisible(true);
    }

    //管理员用户：加载表格
    private void updateTable2() {
        //设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        table2.setDefaultRenderer(Object.class, r);

        admin_mapList = SettingUtil.getData("admin"); //获得表内容
        //非空判断
        if (admin_mapList == null || admin_mapList.size() == 0) {
            return;
        }

        DefaultTableModel table2Model = (DefaultTableModel) table2.getModel(); //获取model
        table2Model.setRowCount(0); //先清空，再加载新数据

        for (Map<String, Object> map : admin_mapList) {
            Object[] newRowData = {
                    map.get("id"),
                    map.get("username")
            };

            table2Model.addRow(newRowData);
        }
    }

    /**
     * 数据库设置
     */
    //初始化 配置
    private void initDBSetting() {
        //获取真实路径
        String druid_properties_path = IOUtil.getPath("conf/druid.properties", "\\conf\\druid.properties");

        ArrayList<String> DB_druidPro = IOUtil.read(druid_properties_path);

        String url = DB_druidPro.get(1).split("mysql://")[1]; //数据库url
        String username = DB_druidPro.get(2).split("=")[1]; //用户名
        String password = DB_druidPro.get(3).split("=")[1]; //密码

        String initialSize = DB_druidPro.get(4).split("=")[1]; //初始化连接数
        String maxActive = DB_druidPro.get(5).split("=")[1]; //最大连接数
        String maxWait = DB_druidPro.get(6).split("=")[1]; //最大等待时间

        textField4.setText(url);
        textField5.setText(username);
        textField6.setText(password);
        textField7.setText(initialSize);
        textField8.setText(maxActive);
        textField9.setText(maxWait);

    }

    //修改 按钮
    private void button12ActionPerformed(ActionEvent e) {
        button13.setEnabled(true);

        textField4.setEditable(true);
        textField5.setEditable(true);
        textField6.setEditable(true);
        textField7.setEditable(true);
        textField8.setEditable(true);
        textField9.setEditable(true);
    }

    //确认 按钮
    private void button13ActionPerformed(ActionEvent e) {
        String url = textField4.getText();
        String username = textField5.getText();
        String password = textField6.getText();

        String initialSize = textField7.getText();
        String maxActive = textField8.getText();
        String maxWait = textField9.getText();

        String[] DB_pro = {
                "driverClassName=com.mysql.jdbc.Driver",
                "url=jdbc:mysql://" + url,
                "username=" + username,
                "password=" + password,
                "initialSize=" + initialSize,
                "maxActive=" + maxActive,
                "maxWait=" + maxWait
        };
        //获取真实路径
        String druid_properties_path = IOUtil.getPath("conf/druid.properties", "\\conf\\druid.properties");
        IOUtil.write(druid_properties_path, DB_pro);

        //关 可编辑
        textField4.setEditable(false);
        textField5.setEditable(false);
        textField6.setEditable(false);
        textField7.setEditable(false);
        textField8.setEditable(false);
        textField9.setEditable(false);

        System.exit(0);
    }

    //恢复默认 按钮
    private void button14ActionPerformed(ActionEvent e) {
        String[] DB_pro = {
                "driverClassName=com.mysql.jdbc.Driver",
                "url=jdbc:mysql://localhost:3306/haolesystem",
                "username=root",
                "password=root",
                "initialSize=10",
                "maxActive=15",
                "maxWait=3000"
        };
        //获取真实路径h
        String druid_properties_path = IOUtil.getPath("conf/druid.properties", "\\conf\\druid.properties");
        IOUtil.write(druid_properties_path, DB_pro);

        //关 可编辑
        textField4.setEditable(false);
        textField5.setEditable(false);
        textField6.setEditable(false);
        textField7.setEditable(false);
        textField8.setEditable(false);
        textField9.setEditable(false);
    }


    /**
     * JFormDesigner自带，定义自生成
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel7 = new JPanel();
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
        table1 = new JTable();
        button6 = new JButton();
        button7 = new JButton();
        button10 = new JButton();
        button11 = new JButton();
        panel6 = new JPanel();
        scrollPane3 = new JScrollPane();
        table2 = new JTable();
        panel4 = new JPanel();
        label14 = new JLabel();
        label18 = new JLabel();
        label19 = new JLabel();
        label20 = new JLabel();
        label21 = new JLabel();
        label22 = new JLabel();
        label23 = new JLabel();
        label24 = new JLabel();
        label25 = new JLabel();
        textField4 = new JTextField();
        textField5 = new JTextField();
        textField6 = new JTextField();
        textField7 = new JTextField();
        textField8 = new JTextField();
        textField9 = new JTextField();
        button12 = new JButton();
        button13 = new JButton();
        label42 = new JLabel();
        button14 = new JButton();
        label43 = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u7ba1\u7406\u4e0e\u8bbe\u7f6e");
        setAlwaysOnTop(true);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getResource("/img/system(big).png")).getImage());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                thisWindowOpened(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel7 ========
        {
            panel7.setBackground(new Color(238, 238, 238));
            panel7.setLayout(null);

            //======== panel1 ========
            {
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
            panel7.add(panel1);
            panel1.setBounds(750, 0, 236, 50);

            //---- button4 ----
            button4.setText("\u7ba1\u7406\u4e0e\u8bbe\u7f6e");
            button4.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 15));
            button4.setForeground(new Color(51, 51, 51));
            panel7.add(button4);
            button4.setBounds(790, 50, 135, 40);

            //---- button3 ----
            button3.setText("\u4e13\u5bb6\u7cfb\u7edf");
            button3.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 15));
            button3.setForeground(new Color(51, 51, 51));
            button3.addActionListener(e -> button3ActionPerformed(e));
            panel7.add(button3);
            button3.setBounds(535, 50, 135, 40);

            //---- button2 ----
            button2.setText("\u5386\u53f2\u7edf\u8ba1\u4e0e\u67e5\u8be2");
            button2.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 15));
            button2.setForeground(new Color(51, 51, 51));
            button2.addActionListener(e -> button2ActionPerformed(e));
            panel7.add(button2);
            button2.setBounds(290, 50, 135, 40);

            //---- button1 ----
            button1.setText("\u5b9e\u65f6\u76d1\u6d4b");
            button1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 15));
            button1.setForeground(new Color(51, 51, 51));
            button1.addActionListener(e -> button1ActionPerformed(e));
            panel7.add(button1);
            button1.setBounds(50, 50, 135, 40);
            panel7.add(separator4);
            separator4.setBounds(5, 90, 965, 10);

            //======== panel2 ========
            {
                panel2.setBackground(new Color(214, 217, 223));
                panel2.setLayout(null);

                //---- label1 ----
                label1.setText("\u710a\u673a\u7ba1\u7406");
                label1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 17));
                panel2.add(label1);
                label1.setBounds(new Rectangle(new Point(105, 8), label1.getPreferredSize()));

                //---- label17 ----
                label17.setText("\u5f53\u524d\u710a\u673a");
                label17.setFont(label17.getFont().deriveFont(label17.getFont().getStyle() | Font.BOLD, label17.getFont().getSize() + 2f));
                label17.setForeground(new Color(71, 71, 71));
                panel2.add(label17);
                label17.setBounds(180, 45, 65, 20);

                //---- label12 ----
                label12.setText("\u710a\u673a\u540d\u79f0\uff1a");
                label12.setFont(label12.getFont().deriveFont(label12.getFont().getStyle() | Font.BOLD, label12.getFont().getSize() + 2f));
                label12.setForeground(new Color(71, 71, 71));
                panel2.add(label12);
                label12.setBounds(35, 70, 80, 20);

                //---- label13 ----
                label13.setText("\u710a\u673a\u72b6\u6001\uff1a");
                label13.setFont(label13.getFont().deriveFont(label13.getFont().getStyle() | Font.BOLD, label13.getFont().getSize() + 2f));
                label13.setForeground(new Color(71, 71, 71));
                panel2.add(label13);
                label13.setBounds(35, 105, 85, 20);

                //---- label15 ----
                label15.setText("\u5f53\u524d\u72b6\u6001");
                label15.setFont(label15.getFont().deriveFont(label15.getFont().getSize() + 2f));
                label15.setForeground(new Color(0, 153, 204));
                panel2.add(label15);
                label15.setBounds(115, 105, 125, 20);

                //---- label16 ----
                label16.setText("\u4fee\u6539\u710a\u673a");
                label16.setFont(label16.getFont().deriveFont(label16.getFont().getStyle() | Font.BOLD, label16.getFont().getSize() + 2f));
                label16.setForeground(new Color(71, 71, 71));
                panel2.add(label16);
                label16.setBounds(180, 175, 65, 20);

                //---- comboBox1 ----
                comboBox1.setSelectedIndex(-1);
                comboBox1.addItemListener(e -> comboBox1ItemStateChanged(e));
                panel2.add(comboBox1);
                comboBox1.setBounds(115, 65, 125, 30);

                //---- label6 ----
                label6.setText("\u710a\u673a\u540d\u79f0\uff1a");
                label6.setFont(label6.getFont().deriveFont(label6.getFont().getStyle() | Font.BOLD, label6.getFont().getSize() + 2f));
                label6.setForeground(new Color(71, 71, 71));
                panel2.add(label6);
                label6.setBounds(35, 200, 80, label6.getPreferredSize().height);

                //---- label7 ----
                label7.setText("\u710a\u673a\u72b6\u6001\uff1a");
                label7.setFont(label7.getFont().deriveFont(label7.getFont().getStyle() | Font.BOLD, label7.getFont().getSize() + 2f));
                label7.setForeground(new Color(71, 71, 71));
                panel2.add(label7);
                label7.setBounds(35, 240, 80, 20);
                panel2.add(textField3);
                textField3.setBounds(115, 235, 125, 30);

                //---- label9 ----
                label9.setText("\u6dfb\u52a0\u710a\u673a");
                label9.setFont(label9.getFont().deriveFont(label9.getFont().getStyle() | Font.BOLD, label9.getFont().getSize() + 2f));
                label9.setForeground(new Color(71, 71, 71));
                panel2.add(label9);
                label9.setBounds(180, 320, 65, 20);
                panel2.add(textField1);
                textField1.setBounds(115, 340, 125, 30);

                //---- label10 ----
                label10.setText("\u710a\u673a\u540d\u79f0:");
                label10.setFont(label10.getFont().deriveFont(label10.getFont().getStyle() | Font.BOLD, label10.getFont().getSize() + 2f));
                label10.setForeground(new Color(71, 71, 71));
                panel2.add(label10);
                label10.setBounds(35, 345, 75, 18);

                //---- label11 ----
                label11.setText("\u710a\u673a\u72b6\u6001:");
                label11.setFont(label11.getFont().deriveFont(label11.getFont().getStyle() | Font.BOLD, label11.getFont().getSize() + 2f));
                label11.setForeground(new Color(71, 71, 71));
                panel2.add(label11);
                label11.setBounds(35, 385, 75, 18);
                panel2.add(textField2);
                textField2.setBounds(115, 380, 125, 30);

                //---- button5 ----
                button5.setText("\u786e\u5b9a");
                button5.setFont(button5.getFont().deriveFont(button5.getFont().getStyle() | Font.BOLD));
                button5.setForeground(new Color(51, 51, 51));
                button5.addActionListener(e -> button5ActionPerformed(e));
                panel2.add(button5);
                button5.setBounds(175, 415, 63, button5.getPreferredSize().height);

                //---- label41 ----
                label41.setText("Tips: \u4fee\u6539\u540e\uff0c\u5207\u6362\u4e00\u4e0b\u9875\u9762\u5373\u53ef\u6b63\u5e38\u663e\u793a");
                label41.setFont(label41.getFont().deriveFont(label41.getFont().getSize() - 1f));
                panel2.add(label41);
                label41.setBounds(0, 460, 220, 20);

                //---- button8 ----
                button8.setText("\u786e\u5b9a");
                button8.setFont(button8.getFont().deriveFont(button8.getFont().getStyle() | Font.BOLD));
                button8.setForeground(new Color(51, 51, 51));
                button8.addActionListener(e -> button8ActionPerformed(e));
                panel2.add(button8);
                button8.setBounds(175, 270, 63, 28);

                //---- button9 ----
                button9.setText("\u786e\u5b9a");
                button9.setFont(button9.getFont().deriveFont(button9.getFont().getStyle() | Font.BOLD));
                button9.setForeground(new Color(51, 51, 51));
                button9.addActionListener(e -> button9ActionPerformed(e));
                panel2.add(button9);
                button9.setBounds(175, 125, 63, 28);

                //---- comboBox2 ----
                comboBox2.setSelectedIndex(-1);
                comboBox2.addItemListener(e -> comboBox2ItemStateChanged(e));
                panel2.add(comboBox2);
                comboBox2.setBounds(115, 195, 125, 30);

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
            panel7.add(panel2);
            panel2.setBounds(5, 100, 295, 480);

            //======== panel3 ========
            {
                panel3.setBackground(new Color(214, 217, 223));
                panel3.setLayout(null);

                //---- label8 ----
                label8.setText("\u7528\u6237\u4fe1\u606f\u7ba1\u7406");
                label8.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 17));
                panel3.add(label8);
                label8.setBounds(new Rectangle(new Point(100, 8), label8.getPreferredSize()));

                //======== tabbedPane1 ========
                {
                    tabbedPane1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 13));

                    //======== panel5 ========
                    {
                        panel5.setLayout(null);

                        //======== scrollPane2 ========
                        {

                            //---- table1 ----
                            table1.setRowHeight(20);
                            table1.setModel(new DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                    "id", "\u7528\u6237\u540d", "\u5bc6\u7801"
                                }
                            ) {
                                Class<?>[] columnTypes = new Class<?>[] {
                                    Integer.class, Object.class, Object.class
                                };
                                @Override
                                public Class<?> getColumnClass(int columnIndex) {
                                    return columnTypes[columnIndex];
                                }
                            });
                            table1.setEnabled(false);
                            scrollPane2.setViewportView(table1);
                        }
                        panel5.add(scrollPane2);
                        scrollPane2.setBounds(0, 50, 290, 355);

                        //---- button6 ----
                        button6.setText("\u4fee\u6539");
                        button6.setFont(button6.getFont().deriveFont(button6.getFont().getStyle() | Font.BOLD));
                        button6.setForeground(new Color(51, 51, 51));
                        button6.addActionListener(e -> button6ActionPerformed(e));
                        panel5.add(button6);
                        button6.setBounds(5, 10, 65, 28);

                        //---- button7 ----
                        button7.setText("\u5220\u9664");
                        button7.setFont(button7.getFont().deriveFont(button7.getFont().getStyle() | Font.BOLD));
                        button7.setForeground(new Color(51, 51, 51));
                        button7.addActionListener(e -> button7ActionPerformed(e));
                        panel5.add(button7);
                        button7.setBounds(220, 10, 65, 28);

                        //---- button10 ----
                        button10.setText("\u6dfb\u52a0");
                        button10.setFont(button10.getFont().deriveFont(button10.getFont().getStyle() | Font.BOLD));
                        button10.setForeground(new Color(51, 51, 51));
                        button10.addActionListener(e -> button10ActionPerformed(e));
                        panel5.add(button10);
                        button10.setBounds(150, 10, 65, 28);

                        //---- button11 ----
                        button11.setText("\u786e\u5b9a");
                        button11.setEnabled(false);
                        button11.setFont(button11.getFont().deriveFont(button11.getFont().getStyle() | Font.BOLD));
                        button11.setForeground(new Color(51, 51, 51));
                        button11.addActionListener(e -> button11ActionPerformed(e));
                        panel5.add(button11);
                        button11.setBounds(75, 10, 65, 28);

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
                    tabbedPane1.addTab("\u5458\u5de5\u7528\u6237", panel5);

                    //======== panel6 ========
                    {
                        panel6.setLayout(null);

                        //======== scrollPane3 ========
                        {

                            //---- table2 ----
                            table2.setRowHeight(20);
                            table2.setModel(new DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                    "id", "\u7528\u6237\u540d"
                                }
                            ) {
                                Class<?>[] columnTypes = new Class<?>[] {
                                    Integer.class, Object.class
                                };
                                boolean[] columnEditable = new boolean[] {
                                    false, false
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
                            table2.setEnabled(false);
                            scrollPane3.setViewportView(table2);
                        }
                        panel6.add(scrollPane3);
                        scrollPane3.setBounds(0, 0, 290, 405);

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
            panel7.add(panel3);
            panel3.setBounds(340, 100, 295, 480);

            //======== panel4 ========
            {
                panel4.setBackground(new Color(214, 217, 223));
                panel4.setLayout(null);

                //---- label14 ----
                label14.setText("\u6570\u636e\u5e93\u8bbe\u7f6e");
                label14.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 17));
                panel4.add(label14);
                label14.setBounds(100, 8, 106, 25);

                //---- label18 ----
                label18.setText("\u6570\u636e\u5e93\u5730\u5740\uff1a");
                label18.setFont(label18.getFont().deriveFont(label18.getFont().getStyle() | Font.BOLD, label18.getFont().getSize() + 2f));
                label18.setForeground(new Color(71, 71, 71));
                panel4.add(label18);
                label18.setBounds(10, 80, 95, 20);

                //---- label19 ----
                label19.setText("\u767b\u5f55\u540d\uff1a");
                label19.setFont(label19.getFont().deriveFont(label19.getFont().getStyle() | Font.BOLD, label19.getFont().getSize() + 2f));
                label19.setForeground(new Color(71, 71, 71));
                panel4.add(label19);
                label19.setBounds(10, 120, 70, 20);

                //---- label20 ----
                label20.setText("\u5bc6\u7801\uff1a");
                label20.setFont(label20.getFont().deriveFont(label20.getFont().getStyle() | Font.BOLD, label20.getFont().getSize() + 2f));
                label20.setForeground(new Color(71, 71, 71));
                panel4.add(label20);
                label20.setBounds(10, 160, 80, 20);

                //---- label21 ----
                label21.setText("\u57fa\u672c\u914d\u7f6e");
                label21.setFont(label21.getFont().deriveFont(label21.getFont().getStyle() | Font.BOLD, label21.getFont().getSize() + 2f));
                label21.setForeground(new Color(71, 71, 71));
                panel4.add(label21);
                label21.setBounds(220, 45, 70, 20);

                //---- label22 ----
                label22.setText("\u9ad8\u7ea7\u914d\u7f6e");
                label22.setFont(label22.getFont().deriveFont(label22.getFont().getStyle() | Font.BOLD, label22.getFont().getSize() + 2f));
                label22.setForeground(new Color(71, 71, 71));
                panel4.add(label22);
                label22.setBounds(220, 210, 70, 20);

                //---- label23 ----
                label23.setText("\u521d\u59cb\u5316\u8fde\u63a5\u6570\u91cf\uff1a");
                label23.setFont(label23.getFont().deriveFont(label23.getFont().getStyle() | Font.BOLD, label23.getFont().getSize() + 2f));
                label23.setForeground(new Color(71, 71, 71));
                panel4.add(label23);
                label23.setBounds(10, 245, 125, 20);

                //---- label24 ----
                label24.setText("\u6700\u5927\u8fde\u63a5\u6570\uff1a");
                label24.setFont(label24.getFont().deriveFont(label24.getFont().getStyle() | Font.BOLD, label24.getFont().getSize() + 2f));
                label24.setForeground(new Color(71, 71, 71));
                panel4.add(label24);
                label24.setBounds(10, 285, 105, 20);

                //---- label25 ----
                label25.setText("\u6700\u5927\u7b49\u5f85\u65f6\u95f4\uff1a");
                label25.setFont(label25.getFont().deriveFont(label25.getFont().getStyle() | Font.BOLD, label25.getFont().getSize() + 2f));
                label25.setForeground(new Color(71, 71, 71));
                panel4.add(label25);
                label25.setBounds(10, 325, 110, 20);

                //---- textField4 ----
                textField4.setEditable(false);
                panel4.add(textField4);
                textField4.setBounds(90, 75, 195, 28);

                //---- textField5 ----
                textField5.setEditable(false);
                panel4.add(textField5);
                textField5.setBounds(90, 115, 195, 28);

                //---- textField6 ----
                textField6.setEditable(false);
                panel4.add(textField6);
                textField6.setBounds(90, 155, 195, 28);

                //---- textField7 ----
                textField7.setEditable(false);
                panel4.add(textField7);
                textField7.setBounds(150, 240, 130, 28);

                //---- textField8 ----
                textField8.setEditable(false);
                panel4.add(textField8);
                textField8.setBounds(150, 280, 130, 28);

                //---- textField9 ----
                textField9.setEditable(false);
                panel4.add(textField9);
                textField9.setBounds(150, 320, 130, 28);

                //---- button12 ----
                button12.setText("\u4fee\u6539");
                button12.setFont(button12.getFont().deriveFont(button12.getFont().getStyle() | Font.BOLD));
                button12.setForeground(new Color(51, 51, 51));
                button12.addActionListener(e -> button12ActionPerformed(e));
                panel4.add(button12);
                button12.setBounds(150, 415, 65, 28);

                //---- button13 ----
                button13.setText("\u4fdd\u5b58");
                button13.setEnabled(false);
                button13.setFont(button13.getFont().deriveFont(button13.getFont().getStyle() | Font.BOLD));
                button13.setForeground(new Color(51, 51, 51));
                button13.addActionListener(e -> button13ActionPerformed(e));
                panel4.add(button13);
                button13.setBounds(215, 415, 65, 28);

                //---- label42 ----
                label42.setText("Tips: \u5982\u65e0\u9700\u8981\uff0c\u4e0d\u8981\u4fee\u6539\u6b64\u914d\u7f6e\u3002");
                label42.setFont(label42.getFont().deriveFont(label42.getFont().getSize() - 1f));
                panel4.add(label42);
                label42.setBounds(5, 460, 290, 20);

                //---- button14 ----
                button14.setText("\u6062\u590d\u9ed8\u8ba4");
                button14.setFont(button14.getFont().deriveFont(button14.getFont().getStyle() | Font.BOLD));
                button14.setForeground(new Color(51, 51, 51));
                button14.addActionListener(e -> button14ActionPerformed(e));
                panel4.add(button14);
                button14.setBounds(10, 415, button14.getPreferredSize().width, 28);

                //---- label43 ----
                label43.setText("Tips:\u4fee\u6539\u540e\uff0c\u5c06\u81ea\u52a8\u5173\u95ed\u3002\u8bf7\u91cd\u65b0\u6253\u5f00\u8f6f\u4ef6");
                label43.setForeground(new Color(255, 51, 51));
                panel4.add(label43);
                label43.setBounds(5, 375, 250, 20);

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
            panel7.add(panel4);
            panel4.setBounds(675, 100, 295, 480);

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
        contentPane.add(panel7);
        panel7.setBounds(0, 0, 985, 595);

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
    private JPanel panel7;
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
    private JTable table1;
    private JButton button6;
    private JButton button7;
    private JButton button10;
    private JButton button11;
    private JPanel panel6;
    private JScrollPane scrollPane3;
    private JTable table2;
    private JPanel panel4;
    private JLabel label14;
    private JLabel label18;
    private JLabel label19;
    private JLabel label20;
    private JLabel label21;
    private JLabel label22;
    private JLabel label23;
    private JLabel label24;
    private JLabel label25;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JButton button12;
    private JButton button13;
    private JLabel label42;
    private JButton button14;
    private JLabel label43;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
