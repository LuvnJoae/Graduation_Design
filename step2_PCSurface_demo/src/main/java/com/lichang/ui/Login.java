/*
 * Created by JFormDesigner on Wed Nov 06 14:53:44 CST 2019
 */

package com.lichang.ui;

import com.lichang.DBbeans.Admin;
import com.lichang.DBbeans.Employee;
import com.lichang.utils.LoginUtils.Login_Logout;
import com.lichang.utils.LoggerUtil;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author unknown
 */
public class Login extends JFrame {

    private static Logger log = LoggerUtil.getLogger(); // 加载日志管理类

    private boolean adminFlag; // 区分用户身份，true：管理员  false：y员工

    public Login() {
        log.debug("载入登录界面");
        initComponents();
        setVisible(true);

    }

    /**
     * 登录按钮 事件监听
     * @param e
     */
    private void button1ActionPerformed(ActionEvent e) {
        log.debug("登录按钮事件监听");
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

        log.debug("username: " + username + "  " + "password: " + password);

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
        log.debug(loginResult);

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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        usernameTextField1 = new JTextField();
        passwordField1 = new JPasswordField();
        button1 = new JButton();
        empRadioButton = new JRadioButton();
        admRadioButton = new JRadioButton();
        label1 = new JLabel();

        //======== this ========
        setBackground(Color.white);
        setAlwaysOnTop(true);
        setForeground(SystemColor.windowText);
        setTitle("\u767b\u5f55");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label2 ----
        label2.setText("\u710a\u63a5\u6570\u636e\u76d1\u6d4b\u4e0e\u7ba1\u7406\u7cfb\u7edf");
        label2.setFont(new Font("\u5fae\u8edf\u6b63\u9ed1\u9ad4", Font.BOLD, 22));
        contentPane.add(label2);
        label2.setBounds(115, 45, 255, 60);

        //---- label3 ----
        label3.setIcon(new ImageIcon(getClass().getResource("/img/\u8c6a\u4e50logo\uff08\u4ec5\u56fe\uff09.png")));
        contentPane.add(label3);
        label3.setBounds(40, 45, 80, 60);

        //---- label4 ----
        label4.setText("\u7528\u6237\u540d\uff1a");
        label4.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 16));
        contentPane.add(label4);
        label4.setBounds(45, 115, 80, 30);

        //---- label5 ----
        label5.setText("\u5bc6\u7801\uff1a");
        label5.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 16));
        contentPane.add(label5);
        label5.setBounds(61, 150, 50, 30);

        //---- usernameTextField1 ----
        usernameTextField1.setFocusTraversalPolicyProvider(true);
        usernameTextField1.setNextFocusableComponent(passwordField1);
        contentPane.add(usernameTextField1);
        usernameTextField1.setBounds(115, 120, 170, 26);

        //---- passwordField1 ----
        passwordField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                passwordField1KeyPressed(e);
            }
        });
        contentPane.add(passwordField1);
        passwordField1.setBounds(115, 155, 170, 25);

        //---- button1 ----
        button1.setText("\u767b\u5f55");
        button1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 16));
        button1.addActionListener(e -> button1ActionPerformed(e));
        contentPane.add(button1);
        button1.setBounds(290, 120, 70, 60);

        //---- empRadioButton ----
        empRadioButton.setText("\u5458\u5de5");
        empRadioButton.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
        empRadioButton.setSelected(true);
        contentPane.add(empRadioButton);
        empRadioButton.setBounds(new Rectangle(new Point(115, 190), empRadioButton.getPreferredSize()));

        //---- admRadioButton ----
        admRadioButton.setText("\u7ba1\u7406\u5458");
        admRadioButton.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
        contentPane.add(admRadioButton);
        admRadioButton.setBounds(210, 190, 75, 25);

        //---- label1 ----
        label1.setText("v1.0");
        contentPane.add(label1);
        label1.setBounds(360, 225, 31, 20);

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
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(empRadioButton);
        buttonGroup1.add(admRadioButton);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JTextField usernameTextField1;
    private JPasswordField passwordField1;
    private JButton button1;
    private JRadioButton empRadioButton;
    private JRadioButton admRadioButton;
    private JLabel label1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
