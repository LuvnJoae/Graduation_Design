package com.lichang.demo;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class demo2 extends JFrame {
    public demo2() throws HeadlessException {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 200, 500, 500);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        JButton jButton = new JButton("1111");
        jButton.setBounds(200, 200, 50, 30);
        contentPane.add(jButton);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test();
            }
        });

        setVisible(true);
    }

    private void test() {
        Container contentPane = getContentPane();
        JButton jButton = new JButton("1111");
        jButton.setBounds(200, 300, 50, 30);
        contentPane.add(jButton);
        contentPane.validate();
        repaint();

        System.out.println("1");
    }

    public static void main(String[] args) {
        new demo2();
    }

}
