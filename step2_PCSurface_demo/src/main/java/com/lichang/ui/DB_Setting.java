/*
 * Created by JFormDesigner on Thu Dec 26 21:29:29 CST 2019
 */

package com.lichang.ui;

import java.awt.event.*;
import com.lichang.utils.IOUtil;
import com.lichang.utils.dao.JdbcTemplateUtil;

import java.awt.*;
import javax.swing.*;

/**
 * @author Brainrain
 */
public class DB_Setting extends JDialog {

    public DB_Setting(Window owner) {
        super(owner);

        initComponents();
        this.setSize(415,285);
        this.setVisible(true);
    }

    public DB_Setting(Frame owner, String title, boolean modal) {
        super(owner, title, modal);

        initComponents();
        this.setSize(415,285);
        this.setVisible(true);
    }

    //数据库设置
    private void initDB() {
        String url = textField1.getText();
        String username = textField2.getText();
        String password = textField3.getText();

        String[] DB_pro = {
                "driverClassName=com.mysql.jdbc.Driver",
                "url=jdbc:mysql://" + url,
                "username=" + username,
                "password=" + password,
                "initialSize=10",
                "maxActive=15",
                "maxWait=3000"
        };
        //获取真实路径
        String druid_properties_path = IOUtil.getPath("conf/druid.properties", "\\conf\\druid.properties");

        IOUtil.write(druid_properties_path, DB_pro);

        JdbcTemplateUtil.regainTemplate(); //重新生成数据库连接。
    }

    //确定 按钮
    private void button1ActionPerformed(ActionEvent e) {
        initDB();
        this.dispose();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        button1 = new JButton();
        textField3 = new JTextField();
        textField2 = new JTextField();
        textField1 = new JTextField();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();

        //======== this ========
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel1 ========
        {
            panel1.setBackground(new Color(238, 238, 238));
            panel1.setLayout(null);

            //---- button1 ----
            button1.setText("\u786e\u5b9a");
            button1.setFont(button1.getFont().deriveFont(button1.getFont().getStyle() | Font.BOLD, button1.getFont().getSize() + 2f));
            button1.setForeground(new Color(51, 51, 51));
            button1.addActionListener(e -> button1ActionPerformed(e));
            panel1.add(button1);
            button1.setBounds(315, 195, 62, 35);
            panel1.add(textField3);
            textField3.setBounds(145, 140, 205, 30);
            panel1.add(textField2);
            textField2.setBounds(145, 95, 205, 30);
            panel1.add(textField1);
            textField1.setBounds(145, 50, 205, 30);

            //---- label1 ----
            label1.setText("\u6570\u636e\u5e93\u5730\u5740\uff1a");
            label1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 16));
            panel1.add(label1);
            label1.setBounds(45, 55, 105, 18);

            //---- label2 ----
            label2.setText("\u7528\u6237\u540d\uff1a");
            label2.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 16));
            panel1.add(label2);
            label2.setBounds(45, 100, 75, 18);

            //---- label3 ----
            label3.setText("\u5bc6\u7801\uff1a");
            label3.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 16));
            panel1.add(label3);
            label3.setBounds(45, 145, 65, 18);

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
        panel1.setBounds(0, 0, 412, 270);

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
    private JButton button1;
    private JTextField textField3;
    private JTextField textField2;
    private JTextField textField1;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
