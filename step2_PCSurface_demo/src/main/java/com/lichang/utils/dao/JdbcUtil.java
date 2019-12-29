package com.lichang.utils.dao;

import com.lichang.utils.IOUtil;
import com.lichang.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

public class JdbcUtil {

    private static Logger log = LoggerUtil.getLogger(); // 加载日志管理类

    private static DataSource ds; // 数据库连接池 对象

    private static Connection connection; // 数据库连接 对象
    private static Statement statement; // Sql执行 对象
    private static ResultSet resultSet; // 结果集 对象

    /**
     * 加载 数据库连接池 配置
     */
    static {
        log.debug("加载数据库连接池配置");
        try {
            //加载配置文件
            Properties dataSourceProperties = new Properties();

            //获取真实路径
            String druid_properties_path = IOUtil.getPath("conf/druid.properties", "\\conf\\druid.properties");

            InputStream DruidPathStream = new FileInputStream(druid_properties_path);
            dataSourceProperties.load(DruidPathStream);
            //获取DataSource
            ds = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(dataSourceProperties);
        } catch (Exception e) {
            log.error(e);
        }

    }

    /**
     * 获取连接池对象
     */
    public static DataSource getDataSource(){
        log.debug("获取连接池对象");
        return ds;
    }

    /**
     * 获取数据库连接
     *      (在使用JDBCTemplate时，不需要通过此方法实现 连接
     *      JDBCTemplate支持自动连接)
     * @return Connection对象
     */
    public static Connection getConnection() {
        log.debug("获取数据库连接");
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            log.error(e);
        }
        return connection;
    }

    /**
     * 释放数据库连接
     *      (在使用JDBCTemplate时，不需要通过此方法实现 连接释放
     *      JDBCTemplate支持自动释放连接)
     */
    public static void closeConnection() {
        log.debug("释放数据库连接");
        if(resultSet != null){
            try{
                resultSet.close();
            }catch(SQLException e){
                log.error(e);
            }finally {
                resultSet = null;
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error(e);
            }finally {
                statement = null;
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e);
            }finally {
                connection = null;
            }
        }
    }
}
