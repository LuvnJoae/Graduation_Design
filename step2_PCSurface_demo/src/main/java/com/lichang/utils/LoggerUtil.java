package com.lichang.utils;
import com.lichang.utils.dao.JdbcUtil;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.io.InputStream;

/**
 * 通过log4j ，进行日志管理
 */
public class LoggerUtil {

    /**
     * 使用方法：
     *      直接通过LoggerUtils的静态类，生成一个logger对象。
     * @return
     */
    public static Logger getLogger(){
        ConfigurationSource source = null;
        InputStream is = null;
        try {
            is = JdbcUtil.class.getClassLoader().getResourceAsStream("conf/log4j2.xml");

            source = new ConfigurationSource(is);
            Configurator.initialize(null, source);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Logger logger = LogManager.getLogger();

        return logger;

    }

}
