/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import com.lichang.DBbeans.Machine_data_now;
import com.lichang.DBbeans.Machine_fault_data;
import com.lichang.utils.LoggerUtil;
import com.lichang.utils.RealTimeMonitoringUtils.ChangePasswordUtil;
import com.lichang.utils.RealTimeMonitoringUtils.LabelUpdateTextUtil;
import com.lichang.utils.RealTimeMonitoringUtils.LineChart;
import com.lichang.utils.RealTimeMonitoringUtils.Table;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


//TODO: 整体待解决问题（低优先级）
//标记时间：2019/11/20 17:22  预解决时间:
//10. 改变chartPanel的刷新规则，改为定时查询最新产品的id，只有当id改变时，才刷新chartPanel（暂不考虑，调试需要在不改变num的前提下改变值）
//4. 当前焊机、工作状态的动态绑定（暂不考虑）
//9. 刷新时，chartPanel的闪 问题（低优先级）
//10. 数据频率增加后，要改表格内容的判断标准（红、蓝字体）

/**
 * @author unknown
 */
public class RealTimeMonitoring_old extends JFrame {
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
    private int faultNum = 0; //表格1，用来判断是否为新的故障记录

    List<Map<String, Object>> machine_data_now_mapsList;
    List<Machine_data_now> machine_data_now_BeansList;
    int last_num; //上一次刷新时，machine_data_now中的产品编号num

    //无参（预设账户信息）
    public RealTimeMonitoring_old() {
        log.debug("无参构造");

        //TEST: 测试用，直接打开该页面时，暂时给username和flag一个值
        //标记时间：2019/11/21 15:56  预解决时间：
        username = "admin";
        adminFlag = true;

        initComponents();

        initTable(); //加载表格设置

        scheduledExecutor(); //定时执行数据刷新

        this.setBounds(273, 95, 990, 625);
        setVisible(true);
    }

    //有参（接收登录账户信息）
    public RealTimeMonitoring_old(String username, Boolean adminFlag) {
        log.debug("有参构造");
        this.username = username;
        this.adminFlag = adminFlag;

        initComponents();

        initTable(); //加载表格设置

        scheduledExecutor(); //定时执行数据刷新

        label3Bind(username); //显示当前用户信息

        this.setBounds(273, 95, 990, 625);
        setVisible(true);
    }

    /**
     * 定时器 1：用于自动更新部分需要连接数据库的组件的内容
     *      table2：
     *      chartPanel：
     *      faultNumberLabel1:
     */
    private void scheduledExecutor() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateTable1(); //加载表格1（故障监测）
                updateTable2(); //加载表格2（参数监测）
                updateFaultNumberLabel1(); //更新故障工件内容
                updateCompletedNumberLabel1(); //更新已完成工件内容
                updateNumberLabel(); // 更新当前工件编号
                updateResultLabel(); // 更新当前工件检测结果
                updateChartPanel(); //加载折线图

                chartPanel.revalidate();
                chartPanel.repaint();
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> scheduledFuture = service.scheduleAtFixedRate(runnable, 1, 5, TimeUnit.SECONDS);
    }

    /**
     * 折线图
     */
    //折线图： 用于生成 折线图 的 ChartPanel（包括刷新）
    private void updateChartPanel() {
        log.debug("折线图： 用于生成 折线图 的 ChartPanel（包括刷新）");

        realTimeLineChart = LineChart.getRealTimeLineChart(); // 获得充满数据的chart模型
        if (chartPanel != null) {
            this.getContentPane().remove(chartPanel);
            this.getContentPane().repaint();

            chartPanel = new ChartPanel(realTimeLineChart); // 通过chart创建ChartPanel面板
        } else {
            chartPanel = new ChartPanel(realTimeLineChart); // 通过chart创建ChartPanel面板
        }

        chartPanel.setLayout(null);
        chartPanel.setBounds(500, 124, 470, 200);
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

    //事件（折线图）：用于点击 折线图后的 放大操作。
    private void chartPanelMouseClicked(MouseEvent e, JFreeChart realTimeLineChart) {
        log.debug("事件（折线图）：用于点击 折线图后的 放大操作");
        // 新建 用于展示的JDialog
        jDialog = new JDialog(this, "",true);

        //给这个JDialog中，新建一个ChartPanel。
        chartPanel2 = new ChartPanel(realTimeLineChart);

        //添加并设置相应属性
        jDialog.add(chartPanel2);
        jDialog.setSize(800,600);
        jDialog.setLocationRelativeTo(null);
        jDialog.setAlwaysOnTop(true);
        jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jDialog.setVisible(true);
    }

    /**
     * 表格
     */
    //表格1、2：设置表格格式、表头的内容与格式等
    private void initTable() {
        log.debug("表格1、2：设置表格格式、表头的内容与格式等");
        /*
            修改表头1、2
         */
        DefaultTableModel table1model = (DefaultTableModel) table1.getModel();
        table1.getTableHeader().setPreferredSize(new Dimension(1, 30)); // 设置表头高度
        table1.getTableHeader().setFont(new Font("", Font.BOLD, 15)); //设置表头字体
        //表头内容、列宽度，已在JFormDesigner中设置
//        String[] table1HeaderName = {"故障时间", "编号","故障表现", "最大频次", "判定"};
//        table1model.setColumnIdentifiers(table1HeaderName);

        DefaultTableModel table2model = (DefaultTableModel) table2.getModel();
        table2.getTableHeader().setPreferredSize(new Dimension(1, 30)); // 设置表头高度
        table2.getTableHeader().setFont(new Font("", Font.BOLD, 15)); //设置表头字体
        // 表头内容、列宽度，已在JFormDesigner中设置
//        String[] table2HeaderName = {"序号","电压 实时值", "电流 实时值", "送丝速度"};
//        table2model.setColumnIdentifiers(table2HeaderName);

        /*
            修改表格内容居中
         */
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        table1.setDefaultRenderer(Object.class, r);
        table2.setDefaultRenderer(Object.class, r);

    }

    //TODO: 添加查看具体选项的按钮
    //标记时间：2019/11/20 17:07  预解决时间：11.20

    //表格1： 数据更新->  若有新的故障数据，则添加到表格中
    private void updateTable1() {
        log.debug("表格1： 更新最后一条数据");
        List<Machine_fault_data> last_machine_fault_data_BeansList = Table.getLastRecord("machine_fault_data");

        if (last_machine_fault_data_BeansList.get(0).getNum() != faultNum) {
            faultNum = last_machine_fault_data_BeansList.get(0).getNum();
            addTable1(last_machine_fault_data_BeansList);
        } else {
            return;
        }
    }

    //表格1： 添加数据
    private void addTable1(List<Machine_fault_data> last_machine_fault_data_BeansList) {
        log.debug("表格1： 添加数据");
        DefaultTableModel table1Model = (DefaultTableModel)table1.getModel(); //获取当前模型

        // 向模型中添加数据
        String time = last_machine_fault_data_BeansList.get(0).getTime().toString().split("\\.")[0]; // 去除timestamp后面的 .0
        int production_num = last_machine_fault_data_BeansList.get(0).getNum();
        String fault_type = last_machine_fault_data_BeansList.get(0).getFault_type();
        int fault_maxnum = last_machine_fault_data_BeansList.get(0).getFault_maxNum();
        String result = last_machine_fault_data_BeansList.get(0).getResult();

        Object[] newRow = {time, "11",production_num, fault_type, fault_maxnum, result}; //定义行内容
        table1Model.addRow(newRow); // 新增加一行
    }

    //表格2： 添加数据
    private void updateTable2() {
        log.debug("表格2： 添加数据");
        machine_data_now_BeansList = Table.getDataBeans_now(); //获取当前工件数据
        int size = machine_data_now_BeansList.size(); // 获取一个过程的数据总数
        DefaultTableModel table2Model = (DefaultTableModel)table2.getModel(); //获取当前模型

        /*
            当数据多于模型所能容纳行数后，自动添加行数
            因为对于table2 ，每一次的添加数据，都是先清空原数据，再增加新数据
            所以，使用setValueAt方法，相当于修改，与table1的直接添加新记录不同
            因此需要有行数不够进行增加的一个步骤
         */
        String[] row = {"", "", "", ""}; // 定义空行 （row）
        while (true) {
            if (table2Model.getRowCount() < size) {
                table2Model.addRow(row);
            } else {
                break;
            }
        }

        String voltageStr;
        String currentStr;
        String speedStr;

        // 向模型中添加数据
        for (int i = 0; i < size; i++) {
            int seq = machine_data_now_BeansList.get(i).getSeq();
            double voltage = machine_data_now_BeansList.get(i).getVoltage();
            double current = machine_data_now_BeansList.get(i).getCurrent();
            double speed = machine_data_now_BeansList.get(i).getSpeed();

            //判断 voltage 数据是否超限
            if ( (seq == 1 && voltage > 28.0)
                    || (seq == 2 && voltage > 24.0)
                    || (seq == 3 && voltage > 22.0)
                    || (seq > 3  && voltage > 21.6) ) {
                voltageStr = "<html>" + "<font color='red'>" + voltage + "</font>" + "</html>";
            } else if ( (seq == 1 && voltage < 27.0)
                    || (seq == 2 && voltage < 22.0)
                    || (seq == 3 && voltage < 21.5)
                    || (seq > 3  && voltage < 21.0) ) {
                voltageStr = "<html>" + "<font color='blue'>" + voltage + "</font>" + "</html>";
            } else {
                voltageStr = voltage + "";
            }

            //判断 current 数据是否超限
            if ( (seq == 1 && current > 105.0)
                    || (seq == 2 && current > 102.0)
                    || (seq == 3 && current > 130.0)
                    || (seq == 4 && current > 135.0)
                    || (seq > 4  && current > 140.0) ) {
                currentStr = "<html>" + "<font color='red'>" + current + "</font>" + "</html>";
            } else if ( (seq == 1 && current < 100.0)
                    || (seq == 2 && current < 96.0)
                    || (seq == 3 && current < 124.0)
                    || (seq == 4 && current < 130.0)
                    || (seq > 4  && current < 135.0) ) {
                currentStr = "<html>" + "<font color='blue'>" + current + "</font>" + "</html>";
            } else {
                currentStr = current + "";
            }

            //判断 speed 数据是否超限
            if (speed > 3.5) {
                speedStr = "<html>" + "<font color='red'>" + speed + "</font>" + "</html>";
            } else if (speed < 3.45) {
                speedStr = "<html>" + "<font color='blue'>" + speed + "</font>" + "</html>";
            } else {
                speedStr = speed + "";
            }

            table2Model.setValueAt(seq,i,0);
            table2Model.setValueAt(voltageStr,i,1);
            table2Model.setValueAt(currentStr,i,2);
            table2Model.setValueAt(speedStr,i,3);
        }
    }

    //清空 按钮：清空 表格1 的内容
    private void button6ActionPerformed(ActionEvent e) {
        log.debug("清空 按钮：清空 表格1 的内容");

        DefaultTableModel table1Model = (DefaultTableModel) table1.getModel();
        table1Model.setRowCount(0);
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

        jDialog2 = new JDialog(this, "",true);
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
        newPasswordField.setFont(new Font("黑体", Font.PLAIN,15));

        newPasswordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = newPasswordField.getText();

                if (!Pattern.matches("^[a-zA-Z][a-zA-Z0-9_]{4,15}$", newPassword)) {
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

                        ChangePasswordUtil.newPassword(table, username, password);
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
     * 跳转 按钮
     */
    //历史统计与查询 按钮： 点击跳转
    private void button2ActionPerformed(ActionEvent e) {
        new HistoricalStatistics(username, adminFlag);
    }

    //故障校验 按钮： 点击跳转
    private void button3ActionPerformed(ActionEvent e) {
        new ExpertSystem(username, adminFlag);
    }

    /**
     * Label 内容更新
     */
    //故障工件 FaultNumberLabel1: 内容刷新
    private void updateFaultNumberLabel1() {
        Long faultNumber = LabelUpdateTextUtil.faultNumberLabel1();
        faultNumberLabel1.setText(String.valueOf(faultNumber));
    }

    //已完成工件 CompletedNumberLabel1: 内容刷新
    private void updateCompletedNumberLabel1() {
        Long completedNumber = LabelUpdateTextUtil.CompletedNumberLabel1();
        completedNumberLabel1.setText(String.valueOf(completedNumber));
    }

    //当前工件编号 NumberLabel： 内容刷新
    private void updateNumberLabel() {
        int num = LabelUpdateTextUtil.numberLabel();
        NumberLabel.setText(String.valueOf(num));
    }

    //当前工件检测结果  ResultLabel； 内容刷新
    private void updateResultLabel() {
        String result = LabelUpdateTextUtil.resultLabel();
        resultLabel.setText(result);
    }

    //Lable3 账户信息: 显示当前登录用户
    private void label3Bind(String username) {
        log.debug("Lable3 账户信息: 显示当前登录用户");
        label3.setText(username);
    }

    /**
     * 其他按钮
     */
    //手动刷新 按钮： 刷新 表格2、折线图 的内容
    private void button8ActionPerformed(ActionEvent e) {
        log.debug("手动刷新 按钮： 刷新 表格2、折线图 的内容");

        updateTable1(); //加载表格1（故障监测）
        updateTable2(); //加载表格2（参数监测）
        updateFaultNumberLabel1(); //更新故障工件内容
        updateCompletedNumberLabel1(); //更新已完成工件内容
        updateNumberLabel(); // 更新当前工件编号
        updateResultLabel(); // 更新当前工件检测结果
        updateChartPanel(); //加载折线图

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    /**
     * 测试按钮
     * @param e
     */
    //TEST: 按钮：生成单条故障数据
    //标记时间：2019/11/21 12:53  预解决时间：
    private void button10ActionPerformed(ActionEvent e) {
        List<Machine_fault_data> faultDataBeans = Table.getFaultDataBeans(3);
        addTable1(faultDataBeans);
    }

    //测试
    private void button5ActionPerformed(ActionEvent e) {

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
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        currentMachineLabel1 = new JLabel();
        currentStatuLabel1 = new JLabel();
        completedNumberLabel1 = new JLabel();
        faultNumberLabel1 = new JLabel();
        label9 = new JLabel();
        button6 = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        button8 = new JButton();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        label10 = new JLabel();
        label11 = new JLabel();
        button10 = new JButton();
        label12 = new JLabel();
        label13 = new JLabel();
        NumberLabel = new JLabel();
        resultLabel = new JLabel();
        button5 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
        contentPane.add(button1);
        button1.setBounds(55, 60, 120, 30);

        //---- button2 ----
        button2.setText("\u5386\u53f2\u7edf\u8ba1\u4e0e\u67e5\u8be2");
        button2.addActionListener(e -> button2ActionPerformed(e));
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

        //---- label4 ----
        label4.setText("\u5f53\u524d\u710a\u673a\uff1a");
        label4.setFont(label4.getFont().deriveFont(label4.getFont().getSize() + 2f));
        contentPane.add(label4);
        label4.setBounds(55, 120, 90, 25);

        //---- label5 ----
        label5.setText("\u710a\u673a\u72b6\u6001\uff1a");
        label5.setFont(label5.getFont().deriveFont(label5.getFont().getSize() + 2f));
        contentPane.add(label5);
        label5.setBounds(55, 170, 80, 25);

        //---- label6 ----
        label6.setText("\u5df2\u5b8c\u6210\u5de5\u4ef6\uff1a");
        label6.setFont(label6.getFont().deriveFont(label6.getFont().getSize() + 2f));
        contentPane.add(label6);
        label6.setBounds(55, 270, 95, 25);

        //---- label7 ----
        label7.setText("\u6545\u969c\u5de5\u4ef6\uff1a");
        label7.setFont(label7.getFont().deriveFont(label7.getFont().getSize() + 2f));
        contentPane.add(label7);
        label7.setBounds(55, 220, 90, 25);

        //---- currentMachineLabel1 ----
        currentMachineLabel1.setText("\u710a\u673a1");
        currentMachineLabel1.setFont(currentMachineLabel1.getFont().deriveFont(currentMachineLabel1.getFont().getSize() + 2f));
        contentPane.add(currentMachineLabel1);
        currentMachineLabel1.setBounds(155, 120, 60, 25);

        //---- currentStatuLabel1 ----
        currentStatuLabel1.setText("\u6b63\u5e38");
        currentStatuLabel1.setFont(currentStatuLabel1.getFont().deriveFont(currentStatuLabel1.getFont().getSize() + 2f));
        contentPane.add(currentStatuLabel1);
        currentStatuLabel1.setBounds(155, 170, 60, 25);

        //---- completedNumberLabel1 ----
        completedNumberLabel1.setText("\u65e0\u6570\u636e");
        completedNumberLabel1.setFont(completedNumberLabel1.getFont().deriveFont(completedNumberLabel1.getFont().getSize() + 2f));
        contentPane.add(completedNumberLabel1);
        completedNumberLabel1.setBounds(154, 270, 60, 25);

        //---- faultNumberLabel1 ----
        faultNumberLabel1.setText("\u65e0\u6570\u636e");
        faultNumberLabel1.setFont(faultNumberLabel1.getFont().deriveFont(faultNumberLabel1.getFont().getSize() + 2f));
        contentPane.add(faultNumberLabel1);
        faultNumberLabel1.setBounds(155, 220, 60, 25);

        //---- label9 ----
        label9.setText("\u7535\u538b\uff08V\uff09\u3001\u7535\u6d41\uff08A\uff09\u5b9e\u65f6\u6ce2\u5f62\uff08\u70b9\u51fb\u653e\u5927\uff09");
        label9.setFont(label9.getFont().deriveFont(label9.getFont().getSize() + 2f));
        contentPane.add(label9);
        label9.setBounds(590, 100, 335, 25);

        //---- button6 ----
        button6.setText("\u6e05\u7a7a");
        button6.addActionListener(e -> button6ActionPerformed(e));
        contentPane.add(button6);
        button6.setBounds(425, 335, 60, 30);

        //======== scrollPane1 ========
        {

            //---- table1 ----
            table1.setModel(new DefaultTableModel(
                new Object[][] {
                    {null, "", null, null, null, null},
                },
                new String[] {
                    "\u6545\u969c\u65f6\u95f4", "\u4ea7\u54c1\u540d\u79f0", "\u5de5\u4ef6\u7f16\u53f7", "\u6545\u969c\u7c7b\u578b", "\u6700\u5927\u9891\u6b21", "\u5224\u5b9a"
                }
            ));
            {
                TableColumnModel cm = table1.getColumnModel();
                cm.getColumn(0).setPreferredWidth(110);
                cm.getColumn(2).setPreferredWidth(40);
                cm.getColumn(5).setPreferredWidth(40);
            }
            table1.setEnabled(false);
            table1.setRowHeight(30);
            table1.setRowMargin(3);
            scrollPane1.setViewportView(table1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 365, 485, 210);

        //---- button8 ----
        button8.setText("\u624b\u52a8\u5237\u65b0");
        button8.addActionListener(e -> button8ActionPerformed(e));
        contentPane.add(button8);
        button8.setBounds(885, 335, 90, 30);

        //======== scrollPane2 ========
        {

            //---- table2 ----
            table2.setModel(new DefaultTableModel(
                new Object[][] {
                    {null, null, null, null},
                },
                new String[] {
                    "\u5e8f\u53f7", "\u7535\u538b \u5b9e\u65f6\u503c", "\u7535\u6d41 \u5b9e\u65f6\u503c", "\u9001\u4e1d\u901f\u5ea6"
                }
            ));
            {
                TableColumnModel cm = table2.getColumnModel();
                cm.getColumn(0).setPreferredWidth(40);
                cm.getColumn(1).setPreferredWidth(100);
                cm.getColumn(2).setPreferredWidth(100);
                cm.getColumn(3).setPreferredWidth(100);
            }
            table2.setRowHeight(30);
            table2.setRowMargin(3);
            table2.setEnabled(false);
            scrollPane2.setViewportView(table2);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(500, 365, 475, 210);

        //---- label10 ----
        label10.setText("\u53c2\u6570\u76d1\u6d4b");
        label10.setBackground(new Color(204, 255, 204));
        label10.setFont(label10.getFont().deriveFont(label10.getFont().getStyle() | Font.BOLD, label10.getFont().getSize() + 7f));
        label10.setLabelFor(table2);
        label10.setIcon(null);
        contentPane.add(label10);
        label10.setBounds(500, 335, 85, 34);

        //---- label11 ----
        label11.setText("\u6545\u969c\u76d1\u6d4b");
        label11.setBackground(new Color(204, 255, 204));
        label11.setFont(label11.getFont().deriveFont(label11.getFont().getStyle() | Font.BOLD, label11.getFont().getSize() + 7f));
        label11.setLabelFor(table1);
        contentPane.add(label11);
        label11.setBounds(1, 335, 94, 34);

        //---- button10 ----
        button10.setText("\u6d4b\u8bd5\uff1a\u751f\u6210\u6545\u969c\u6570\u636e");
        button10.addActionListener(e -> button10ActionPerformed(e));
        contentPane.add(button10);
        button10.setBounds(250, 335, 155, button10.getPreferredSize().height);

        //---- label12 ----
        label12.setText("\u5de5\u4ef6\u7f16\u53f7\uff1a");
        label12.setFont(label12.getFont().deriveFont(label12.getFont().getSize() + 2f));
        contentPane.add(label12);
        label12.setBounds(260, 120, 80, 25);

        //---- label13 ----
        label13.setText("\u68c0\u6d4b\u7ed3\u679c\uff1a");
        label13.setFont(label13.getFont().deriveFont(label13.getFont().getSize() + 2f));
        contentPane.add(label13);
        label13.setBounds(260, 170, 80, 25);

        //---- NumberLabel ----
        NumberLabel.setText("0");
        NumberLabel.setFont(NumberLabel.getFont().deriveFont(NumberLabel.getFont().getSize() + 2f));
        contentPane.add(NumberLabel);
        NumberLabel.setBounds(345, 120, 60, 25);

        //---- resultLabel ----
        resultLabel.setText("\u6b63\u5728\u52a0\u8f7d");
        resultLabel.setFont(resultLabel.getFont().deriveFont(resultLabel.getFont().getSize() + 2f));
        contentPane.add(resultLabel);
        resultLabel.setBounds(345, 170, 60, 25);

        //---- button5 ----
        button5.setText("text");
        button5.addActionListener(e -> button5ActionPerformed(e));
        contentPane.add(button5);
        button5.setBounds(new Rectangle(new Point(425, 285), button5.getPreferredSize()));

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
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel currentMachineLabel1;
    private JLabel currentStatuLabel1;
    private JLabel completedNumberLabel1;
    private JLabel faultNumberLabel1;
    private JLabel label9;
    private JButton button6;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JButton button8;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JLabel label10;
    private JLabel label11;
    private JButton button10;
    private JLabel label12;
    private JLabel label13;
    private JLabel NumberLabel;
    private JLabel resultLabel;
    private JButton button5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
