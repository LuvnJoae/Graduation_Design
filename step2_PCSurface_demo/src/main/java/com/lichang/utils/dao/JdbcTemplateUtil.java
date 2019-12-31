package com.lichang.utils.dao;

import com.lichang.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class JdbcTemplateUtil {

    private static Logger log = LoggerUtil.getLogger();
    private static JdbcTemplate template = new JdbcTemplate(JdbcUtil.getDataSource()); // 获取JdbcTemplate对象

    /**
     * 用于当 Druid 配置文件更改后，重新连接数据库。
     */
    public static void regainTemplate() {
        JdbcUtil.regainDS(); //重新生成ds
        template = new JdbcTemplate(JdbcUtil.getDataSource());
    }

    /**
     * 用于判断是否成功连接到数据库
     *      Druid不能直接给出是否成功连接数据库，所以先通过查询admin表的方式，验证是否连接成功数据库
     */
    public static boolean validate() {
        log.debug("验证 数据库是否已连接");
        try {
            String sqlStr = "select * from admin";
            template.queryForList(sqlStr);
            return true;
        } catch (DataAccessException e) {
            log.error("数据库连接失败！");
            return false;
        }
    }


    /**
     * 查询 返回单条记录
     *      数据库相应表已做 唯一约束，确保不重名
     * @param sqlStr
     * @return Map
     */

    public static Map<String, Object> querySingle(String sqlStr, List<Object> paramsList) {
        log.debug("查询 返回单条记录");
        try {
            Map<String, Object> map = template.queryForMap(sqlStr, paramsList.toArray());
            return map;
        } catch (DataAccessException e) {
            log.error("查询不到该信息");
            return null;
        }
    }

    /**
     * 查询 返回单条记录 (无参复用)
     *      数据库相应表已做 唯一约束，确保不重名
     * @param sqlStr
     * @return Map
     */

    public static Map<String, Object> querySingle(String sqlStr) {
        log.debug("查询 返回单条记录");
        try {
            Map<String, Object> map = template.queryForMap(sqlStr);
            return map;
        } catch (DataAccessException e) {
            log.error("查询不到该信息");
            return null;
        }
    }

    /**
     * 查询 返回多条记录
     * @param sqlStr
     * @return list<Map>
     */
    public static List<Map<String, Object>> queryMult(String sqlStr, List<Object> paramsList) {
        log.debug("查询 返回多条记录");
        try {
            List<Map<String, Object>> mapsList = template.queryForList(sqlStr, paramsList.toArray());
            return mapsList;
        } catch (DataAccessException e) {
            log.error("查询不到该信息");
            return null;
        }
    }

    /**
     * 查询 返回多条记录 (无参复用)
     * @param sqlStr
     * @return list<Map>
     */
    public static List<Map<String, Object>> queryMult(String sqlStr) {
        log.debug("查询 返回多条记录");
        try {
            List<Map<String, Object>> mapsList = template.queryForList(sqlStr);
            return mapsList;
        } catch (DataAccessException e) {
            log.error("查询不到该信息");
            return null;
        }
    }

    /**
     * 查询 多条记录， 返回Bean对象List
     * @return List<Bean>
     */
    public static List<?> queryMultForBean(String sqlStr, Class<?> cla, List<Object> paramsList) {
        log.debug("查询 多条记录， 返回 Admin 的 Bean对象");
        try {
            List<?> listBean = template.query(sqlStr, paramsList.toArray(), new BeanPropertyRowMapper<>(cla));
            return listBean;
        } catch (DataAccessException e) {
            log.error("查询不到该信息");
            return null;
        }
    }

    /**
     * 查询 多条记录， 返回Bean对象List (无参复用)
     * @return List<Bean>
     */
    public static List<?> queryMultForBean(String sqlStr, Class<?> cla) {
        log.debug("查询 多条记录， 返回 Admin 的 Bean对象");
        try {
            List<?> listBean = template.query(sqlStr, new BeanPropertyRowMapper<>(cla));
            return listBean;
        } catch (DataAccessException e) {
            log.error("查询不到该信息");
            return null;
        }
    }


    /**
     * 查询 总记录数
     * 注意，这里的传参只能用数组，而不能用list
     * @return
     */
    public static Long queryCount(String sqlStr, Object[] params) {
        log.debug("查询 总记录数");
        Long numLong = null;
        try {
            numLong = template.queryForObject(sqlStr, params, Long.class);
        } catch (DataAccessException e) {
            log.error("查询总记录数 出错");
        }
        return numLong;
    }

    /**
     * 查询 总记录数 无参重载
     * @return
     */
    public static Long queryCount(String sqlStr) {
        log.debug("查询 总记录数");
        Long numLong = null;
        try {
            numLong = template.queryForObject(sqlStr, Long.class);
        } catch (DataAccessException e) {
            log.error("查询总记录数 出错");
        }
        return numLong;
    }

    /**
     * 插入、 修改、 删除
     * @param sqlStr
     * @return
     */
    public static boolean update(String sqlStr, List<Object> paramsList) {
        log.debug("插入、 修改、 删除");

        int updateCount = 0;

        try {
            updateCount = template.update(sqlStr, paramsList.toArray());
        } catch (DataAccessException e) {
            log.error("插入、 修改、 删除出错", e);
        }

        if (updateCount > 0) {
            return true;
        }else {
            return false;
        }
    }
    /**
     * 插入、 修改、 删除 无参重载
     * @param sqlStr
     * @return
     */
    public static boolean update(String sqlStr) {
        log.debug("插入、 修改、 删除");

        int updateCount = 0;

        try {
            updateCount = template.update(sqlStr);
        } catch (DataAccessException e) {
            log.error("插入、 修改、 删除出错", e);
        }

        if (updateCount > 0) {
            return true;
        }else {
            return false;
        }
    }



}
