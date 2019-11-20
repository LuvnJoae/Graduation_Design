package com.lichang.demo;

import com.lichang.DBbeans.Machine_fault_data;
import com.lichang.ui.util.Table;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

public class demo2 {

    public static void main(String[] args) {
        List<Machine_fault_data> machine_fault_data_BeansList = Table.getFaultDataBeans(2); //获取机器数据
        String time = machine_fault_data_BeansList.get(0).getTime().toString();
        System.out.println(time.split("\\.")[0]);
        System.out.println(time.split("\\.")[1]);
    }
}

