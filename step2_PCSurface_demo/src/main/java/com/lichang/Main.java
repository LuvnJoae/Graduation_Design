package com.lichang;

import com.lichang.ui.Login;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        /**
         * 发布包 请将resource/conf/selectPath 中的内容改为 release, 正常调试请改为 debug
         */

        //设置整体UI风格
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //启动界面
        new Login();
    }





}
