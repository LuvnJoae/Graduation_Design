package com.lichang.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 通过log4j ，进行日志管理
 */
public class LoggerUtil {

    /**
     * 使用方法：
     *      直接通过LoggerUtils的静态类，生成一个logger对象。
     * @return
     */
    public static Logger getLogger() {
        Logger logger = Logger.getLogger(LoggerUtil.class);
        // 通过类加载器获得配置文件路径
        String path = LoggerUtil.class.getClassLoader().getResource("conf/log4j.properties").getPath();
        PropertyConfigurator.configure(path);
        return logger;
    }

}
