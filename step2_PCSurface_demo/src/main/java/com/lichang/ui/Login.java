/*
 * Created by JFormDesigner on Wed Nov 06 14:53:44 CST 2019
 */

package com.lichang.ui;

import com.lichang.DBbeans.Admin;
import com.lichang.DBbeans.Employee;
import com.lichang.utils.LoginUtils.Login_Logout;
import com.lichang.utils.LoggerUtil;

import com.lichang.utils.dao.JdbcTemplateUtil;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author unknown
 */
public class Login extends JFrame {


    private static Logger log = LoggerUtil.getLogger();; // 加载日志管理类

    private boolean adminFlag; // 区分用户身份，true：管理员  false：y员工

    public Login() {
        log.debug("载入登录界面");
        initComponents();
        this.setSize(405,285);
        setVisible(true);

    }

    /**
     * 页面监听
     */
    //第一次打开该页面时
    private void thisWindowOpened(WindowEvent e) {
        DB_validate(); //验证数据库是否已成功连接
    }

    /**
     * 登录按钮 事件监听
     * @param e
     */
    private void button1ActionPerformed(ActionEvent e) {
        /*
            空用户名与密码 检测
         */
        if (( usernameTextField1.getText() == null || usernameTextField1.getText().length() <= 0 ) &&
                ( passwordField1.getPassword() == null || passwordField1.getPassword().length <= 0 )) {
            JOptionPane.showMessageDialog(this, "请输入用户名与密码！","提示",JOptionPane.WARNING_MESSAGE);
            return;
        }else if (usernameTextField1.getText() == null || usernameTextField1.getText().length() <= 0) {
            JOptionPane.showMessageDialog(this, "请输入用户名！","提示",JOptionPane.WARNING_MESSAGE);
            return;
        }else if (passwordField1.getPassword() == null || passwordField1.getPassword().length <= 0 ) {
            JOptionPane.showMessageDialog(this, "请输入密码！","提示",JOptionPane.WARNING_MESSAGE);
            return;
        }

        /*
            将输入的账户信息，转存为Bean对象
         */
        // 登录信息预留
        String username = usernameTextField1.getText().trim();
        String password = new String(passwordField1.getPassword());
        Object userInfo = null;


        if (empRadioButton.isSelected()) {
            userInfo = new Employee(username, password);
            adminFlag = false;
        } else if (admRadioButton.isSelected()) {
            userInfo = new Admin(username, password);
            adminFlag = true;
        }
        /*
            账户信息认证
         */
        String loginResult = Login_Logout.login(userInfo);

        /*
            账户认证后的界面跳转
         */
        if (!loginResult.equals("登录成功！")) {
            JOptionPane.showMessageDialog(this, loginResult, "提示", JOptionPane.WARNING_MESSAGE);
        }else {
            new RealTimeMonitoring(username, adminFlag);
            this.dispose();
        }

    }

    /**
     * Enter 按键 监听，等效于点击登录按钮
     *      在密码栏
     * @param e
     */
    private void passwordField1KeyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_ENTER) {
            button1ActionPerformed(null);
        }
    }

    /**
     * 数据库设置
     * @param e
     */
    private void button2ActionPerformed(ActionEvent e) {
        new DB_Setting(this, "数据库设置", true);
        DB_validate(); //设置数据库连接参数后，重新验证连接结果
    }

    /**
     * 验证数据库连接状态
     */
    private void DB_validate() {
        boolean DB_stastus = false;
        try {
            DB_stastus = JdbcTemplateUtil.validate();
        } catch (Exception e) {
            log.error("数据库连接失败！");
        }
        if (DB_stastus) {
            label7.setText("<html>" + "<font color='green'>" + "连接成功！" + "</font>" + "</html>");
        } else {
            label7.setText("<html>" + "<font color='red'>" + "连接失败！" + "</font>" + "</html>");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        label3 = new JLabel();
        label2 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label1 = new JLabel();
        empRadioButton = new JRadioButton();
        passwordField1 = new JPasswordField();
        usernameTextField1 = new JTextField();
        admRadioButton = new JRadioButton();
        button1 = new JButton();
        button2 = new JButton();
        label6 = new JLabel();
        label7 = new JLabel();

        //======== this ========
        setBackground(new Color(238, 238, 238));
        setAlwaysOnTop(true);
        setForeground(SystemColor.windowText);
        setTitle("\u767b\u5f55");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setAutoRequestFocus(false);
        setIconImage(new ImageIcon(getClass().getResource("/img/system(big).png")).getImage());
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

            //---- label3 ----
            label3.setIcon(new ImageIcon(getClass().getResource("/img/system2(72).png")));
            panel1.add(label3);
            label3.setBounds(35, 30, 70, 60);

            //---- label2 ----
            label2.setText("\u710a\u63a5\u6570\u636e\u76d1\u6d4b\u4e0e\u4e13\u5bb6\u7cfb\u7edf");
            label2.setFont(new Font("\u5fae\u8edf\u6b63\u9ed1\u9ad4", Font.BOLD, 22));
            panel1.add(label2);
            label2.setBounds(115, 25, 255, 60);

            //---- label4 ----
            label4.setText("\u7528\u6237\u540d\uff1a");
            label4.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 16));
            label4.setForeground(new Color(51, 51, 51));
            panel1.add(label4);
            label4.setBounds(40, 100, 70, 30);

            //---- label5 ----
            label5.setText("\u5bc6\u7801\uff1a");
            label5.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 16));
            label5.setForeground(new Color(51, 51, 51));
            panel1.add(label5);
            label5.setBounds(55, 140, 54, 30);

            //---- label1 ----
            label1.setText("v1.1");
            panel1.add(label1);
            label1.setBounds(5, 225, 31, 20);

            //---- empRadioButton ----
            empRadioButton.setText("\u5458\u5de5");
            empRadioButton.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            empRadioButton.setSelected(true);
            panel1.add(empRadioButton);
            empRadioButton.setBounds(110, 175, 46, 25);

            //---- passwordField1 ----
            passwordField1.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    passwordField1KeyPressed(e);
                }
            });
            panel1.add(passwordField1);
            passwordField1.setBounds(110, 140, 170, 30);

            //---- usernameTextField1 ----
            usernameTextField1.setFocusTraversalPolicyProvider(true);
            usernameTextField1.setNextFocusableComponent(passwordField1);
            panel1.add(usernameTextField1);
            usernameTextField1.setBounds(110, 100, 170, 31);

            //---- admRadioButton ----
            admRadioButton.setText("\u7ba1\u7406\u5458");
            admRadioButton.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            panel1.add(admRadioButton);
            admRadioButton.setBounds(205, 175, 75, 25);

            //---- button1 ----
            button1.setText("\u767b \u5f55");
            button1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 16));
            button1.setForeground(new Color(51, 51, 51));
            button1.addActionListener(e -> button1ActionPerformed(e));
            panel1.add(button1);
            button1.setBounds(295, 100, 70, 70);

            //---- button2 ----
            button2.setText("\u6570\u636e\u5e93\u8bbe\u7f6e");
            button2.addActionListener(e -> button2ActionPerformed(e));
            panel1.add(button2);
            button2.setBounds(300, 215, 88, 30);

            //---- label6 ----
            label6.setText("\u6570\u636e\u5e93\u8fde\u63a5\u72b6\u6001\uff1a");
            label6.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            label6.setForeground(new Color(51, 51, 51));
            panel1.add(label6);
            label6.setBounds(110, 220, 100, 23);

            //---- label7 ----
            label7.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            label7.setText("\u68c0\u6d4b\u4e2d\u00b7\u00b7\u00b7");
            label7.setForeground(new Color(51, 51, 51));
            panel1.add(label7);
            label7.setBounds(215, 220, 60, 22);

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
        panel1.setBounds(0, 0, 400, 255);

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
        setSize(405, 285);
        setLocationRelativeTo(null);

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(empRadioButton);
        buttonGroup1.add(admRadioButton);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JLabel label3;
    private JLabel label2;
    private JLabel label4;
    private JLabel label5;
    private JLabel label1;
    private JRadioButton empRadioButton;
    private JPasswordField passwordField1;
    private JTextField usernameTextField1;
    private JRadioButton admRadioButton;
    private JButton button1;
    private JButton button2;
    private JLabel label6;
    private JLabel label7;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
