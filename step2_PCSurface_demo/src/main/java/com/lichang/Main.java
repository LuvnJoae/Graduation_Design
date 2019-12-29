package com.lichang;

import com.alee.laf.WebLookAndFeel;
import com.lichang.ui.Login;
import com.lichang.ui.RealTimeMonitoring;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        new Login();
    new RealTimeMonitoring();
    }





}
