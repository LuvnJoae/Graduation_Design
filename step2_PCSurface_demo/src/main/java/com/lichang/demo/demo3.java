package com.lichang.demo;

import javax.swing.*;
import java.awt.*;

public class demo3 extends JFrame {

    public demo3() {
        this.setBounds(50,50,300,300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);

        UIManager.put("TextField.inactiveForeground", Color.red);
        JTextField text = new JTextField(15);
        text.setText("ABCDEF");
        text.setEnabled(false);
        this.getContentPane().add(text);
        text.setBounds(50,50,100,30);

        UIManager.put("ComboBox.disabledForeground", Color.red);
        JComboBox<Object> box = new JComboBox<>();
        box.addItem("1");
        box.addItem("2");
        box.addItem("3");
        box.setSelectedIndex(1);
        box.setBounds(50,100,100,30);
        box.setEnabled(false);
        this.getContentPane().add(box);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        new demo3();
    }
}
