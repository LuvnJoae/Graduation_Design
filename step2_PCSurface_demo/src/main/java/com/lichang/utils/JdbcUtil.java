package com.lichang.utils;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.InputStream;
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
        log.info("加载数据库连接池配置");
        try {
            //加载配置文件
            Properties dataSourceProperties = new Properties();
            InputStream DruidPathStream = JdbcUtil.class.getClassLoader().getResourceAsStream("conf/druid.properties");
            dataSourceProperties.load(DruidPathStream);
            //获取DataSource
            ds = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(dataSourceProperties);
        } catch (Exception e) {
            log.error("", e);
            e.printStackTrace();
        }

    }

    /**
     * 获取连接池对象
     */
    public static DataSource getDataSource(){
        log.info("获取连接池对象");
        return ds;
    }

    /**
     * 获取数据库连接
     *      (在使用JDBCTemplate时，不需要通过此方法实现 连接
     *      JDBCTemplate支持自动连接)
     * @return Connection对象
     */
    public static Connection getConnection() {
        log.info("获取数据库连接");
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            log.error("", e);
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 释放数据库连接
     *      (在使用JDBCTemplate时，不需要通过此方法实现 连接释放
     *      JDBCTemplate支持自动释放连接)
     */
    public static void closeConnection() {
        log.info("释放数据库连接");
        if(resultSet != null){
            try{
                resultSet.close();
            }catch(SQLException e){
                e.printStackTrace();
            }finally {
                resultSet = null;
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                statement = null;
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                connection = null;
            }
        }
    }
}
