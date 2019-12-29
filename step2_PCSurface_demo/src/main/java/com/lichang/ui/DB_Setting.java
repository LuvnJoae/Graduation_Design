/*
 * Created by JFormDesigner on Thu Dec 26 21:29:29 CST 2019
 */

package com.lichang.ui;

import java.awt.event.*;
import com.lichang.utils.IOUtil;
import com.lichang.utils.LoggerUtil;

import java.awt.*;
import java.io.InputStream;
import javax.swing.*;

/**
 * @author Brainrain
 */
public class DB_Setting extends JDialog {
    public DB_Setting(Window owner) {
        super(owner);

        this.setVisible(true);
        initComponents();
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
    }

    //确定 按钮
    private void button1ActionPerformed(ActionEvent e) {
        initDB();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        label3 = new JLabel();
        textField2 = new JTextField();
        textField3 = new JTextField();
        button1 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label1 ----
        label1.setText("\u6570\u636e\u5e93\u5730\u5740");
        contentPane.add(label1);
        label1.setBounds(30, 35, 105, label1.getPreferredSize().height);
        contentPane.add(textField1);
        textField1.setBounds(165, 35, 145, textField1.getPreferredSize().height);

        //---- label2 ----
        label2.setText("\u7528\u6237\u540d");
        contentPane.add(label2);
        label2.setBounds(35, 80, 75, label2.getPreferredSize().height);

        //---- label3 ----
        label3.setText("\u5bc6\u7801");
        contentPane.add(label3);
        label3.setBounds(35, 125, 65, label3.getPreferredSize().height);
        contentPane.add(textField2);
        textField2.setBounds(165, 80, 145, textField2.getPreferredSize().height);
        contentPane.add(textField3);
        textField3.setBounds(165, 120, 145, textField3.getPreferredSize().height);

        //---- button1 ----
        button1.setText("\u786e\u5b9a");
        button1.addActionListener(e -> button1ActionPerformed(e));
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(310, 210), button1.getPreferredSize()));

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
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JLabel label3;
    private JTextField textField2;
    private JTextField textField3;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
