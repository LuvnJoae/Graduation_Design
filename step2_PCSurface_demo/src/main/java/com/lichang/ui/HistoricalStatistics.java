/*
 * Created by JFormDesigner on Wed Nov 13 15:26:17 CST 2019
 */

package com.lichang.ui;

import com.lichang.utils.LoggerUtil;
import com.lichang.utils.RealTimeMonitoringUtil.ChangePassword;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;

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
     *  JFormDesigner自带，定义自生成
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
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
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        label11 = new JLabel();
        button5 = new JButton();
        button6 = new JButton();
        textField1 = new JTextField();
        button7 = new JButton();
        label1 = new JLabel();
        label12 = new JLabel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u754c\u9762");
        setAlwaysOnTop(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel1 ========
        {
            panel1.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder (
            0, 0 ,0 , 0) ,  "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder
            . BOTTOM, new java. awt .Font ( "D\u0069al\u006fg", java .awt . Font. BOLD ,12 ) ,java . awt. Color .
            red ) ,panel1. getBorder () ) ); panel1. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java .
            beans. PropertyChangeEvent e) { if( "\u0062or\u0064er" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
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

        //======== scrollPane1 ========
        {

            //---- table1 ----
            table1.setModel(new DefaultTableModel(
                new Object[][] {
                    {"2019-01-01 16\uff1a00\uff1a00", "1", "v+", "11", "\u6b21\u54c1", "\u70b9\u51fb\u67e5\u770b"},
                    {"2019-01-01 17\uff1a00\uff1a00", "2", "v+", "10", "\u6b21\u54c1", "\u70b9\u51fb\u67e5\u770b"},
                    {"2019-01-01 18\uff1a00\uff1a00", "3", "v+", "12", "\u6b21\u54c1", "\u70b9\u51fb\u67e5\u770b"},
                },
                new String[] {
                    "\u6545\u969c\u65f6\u95f4", "\u7f16\u53f7", "\u6545\u969c\u8868\u73b0", "\u6700\u5927\u9891\u6b21", "\u5224\u5b9a", "\u8be6\u60c5"
                }
            ));
            {
                TableColumnModel cm = table1.getColumnModel();
                cm.getColumn(0).setPreferredWidth(110);
                cm.getColumn(1).setPreferredWidth(40);
            }
            table1.setEnabled(false);
            table1.setRowHeight(30);
            table1.setRowMargin(3);
            scrollPane1.setViewportView(table1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 365, 985, 210);

        //---- label11 ----
        label11.setText("\u6545\u969c\u67e5\u8be2");
        label11.setBackground(new Color(204, 255, 204));
        label11.setFont(label11.getFont().deriveFont(label11.getFont().getStyle() | Font.BOLD, label11.getFont().getSize() + 7f));
        label11.setLabelFor(table1);
        contentPane.add(label11);
        label11.setBounds(1, 335, 94, 34);

        //---- button5 ----
        button5.setText("\u6309\u65f6\u95f4");
        contentPane.add(button5);
        button5.setBounds(585, 335, 85, 28);

        //---- button6 ----
        button6.setText("\u6309\u5224\u5b9a");
        contentPane.add(button6);
        button6.setBounds(680, 335, 85, 28);

        //---- textField1 ----
        textField1.setText("\u8bf7\u8f93\u5165\u65f6\u95f4/\u5224\u5b9a");
        contentPane.add(textField1);
        textField1.setBounds(770, 335, 145, 27);

        //---- button7 ----
        button7.setText("\u67e5\u8be2");
        contentPane.add(button7);
        button7.setBounds(920, 335, 65, 28);

        //---- label1 ----
        label1.setText("\u6545\u969c\u5206\u5e03\u7edf\u8ba1\u56fe");
        contentPane.add(label1);
        label1.setBounds(725, 110, 95, 25);

        //---- label12 ----
        label12.setText("\u6545\u969c\u7edf\u8ba1");
        label12.setBackground(new Color(204, 255, 204));
        label12.setFont(label12.getFont().deriveFont(label12.getFont().getStyle() | Font.BOLD, label12.getFont().getSize() + 7f));
        label12.setLabelFor(table1);
        contentPane.add(label12);
        label12.setBounds(5, 105, 94, 34);

        //======== scrollPane2 ========
        {

            //---- table2 ----
            table2.setModel(new DefaultTableModel(
                new Object[][] {
                    {"\u6b21\u54c1", "v+", "10"},
                    {"\u6b21\u54c1", "c+", "5"},
                    {"\u9690\u60a3", "c+\uff0cp+", "2"},
                    {"\u9690\u60a3", "v+\uff0cs+", "1"},
                },
                new String[] {
                    "\u6545\u969c\u5224\u5b9a", "\u6545\u969c\u8868\u73b0", "\u603b\u6b21\u6570"
                }
            ));
            table2.setPreferredSize(new Dimension(225, 150));
            table2.setRowHeight(30);
            table2.setRowMargin(3);
            table2.setEnabled(false);
            scrollPane2.setViewportView(table2);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(0, 145, 440, 180);

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
    // Generated using JFormDesigner Evaluation license - unknown
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
    private JScrollPane scrollPane1;
    private JTable table1;
    private JLabel label11;
    private JButton button5;
    private JButton button6;
    private JTextField textField1;
    private JButton button7;
    private JLabel label1;
    private JLabel label12;
    private JScrollPane scrollPane2;
    private JTable table2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
