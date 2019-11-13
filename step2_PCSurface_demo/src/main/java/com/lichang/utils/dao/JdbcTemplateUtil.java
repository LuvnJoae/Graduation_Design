package com.lichang.utils.dao;

import com.lichang.utils.LoggerUtil;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class JdbcTemplateUtil {

    private static Logger log = LoggerUtil.getLogger();
    private static JdbcTemplate template = new JdbcTemplate(JdbcUtil.getDataSource()); // 获取JdbcTemplate对象

    /**
     * 查询 返回单条记录
     *      数据库相应表已做 唯一约束，确保不重名
     * @param sqlStr
     * @return Map
     */

    public static Map<String, Object> querySingle(String sqlStr, List<Object> paramsList) {
        log.info("查询 返回单条记录");
        try {
            Map<String, Object> map = template.queryForMap(sqlStr, paramsList.toArray());
            return map;
        } catch (DataAccessException e) {
            log.error("查询不到该信息",e);
            return null;
        }
    }

    /**
     * 查询 返回多条记录
     * @param sqlStr
     * @return list<Map>
     */
    public static List<Map<String, Object>> queryMult(String sqlStr, List<Object> paramsList) {
        log.info("查询 返回多条记录");
        try {
            List<Map<String, Object>> maps = template.queryForList(sqlStr, paramsList.toArray());
            return maps;
        } catch (DataAccessException e) {
            log.error("查询不到该信息", e);
            return null;
        }
    }

    /**
     * 查询 多条记录， 返回Bean对象List
     * @return List<Bean>
     */
    public static List<?> queryMultForBean(String sqlStr, Object obj, List<Object> paramsList) {
        log.info("查询 多条记录， 返回 Admin 的 Bean对象");
        try {
            List<?> listBean = template.query(sqlStr, paramsList.toArray(), new BeanPropertyRowMapper<>(obj.getClass()));
            return listBean;
        } catch (DataAccessException e) {
            log.error("查询不到该信息", e);
            return null;
        }
    }

    /**
     * 插入、 修改、 删除
     * @param sqlStr
     * @return
     */
    public static boolean insertSingle(String sqlStr, List<Object> paramsList) {
        log.info("插入、 修改、 删除");
        int insertCount = template.update(sqlStr, paramsList.toArray());
        if (insertCount > 0) {
            return true;
        }else {
            return false;
        }
    }

}