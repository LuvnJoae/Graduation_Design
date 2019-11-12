package com.lichang.utils;

import com.lichang.DBbeans.Admin;
import com.lichang.DBbeans.Employee;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class JdbcTemplateUtil {

    private static Logger log = LoggerUtil.getLogger();
    private static JdbcTemplate template = new JdbcTemplate(JdbcUtil.getDataSource()); // 获取JdbcTemplate对象

    /**
     * 查询 返回单条记录
     * @param sqlStr
     * @return Map
     */
    public static Map<String, Object> querySingle(String sqlStr) {
        log.info("查询 返回单条记录");
        Map<String, Object> map = template.queryForMap(sqlStr);
        return map;
    }

    /**
     * 查询 返回多条记录
     * @param sqlStr
     * @return list<Map>
     */
    public static List<Map<String, Object>> queryMult(String sqlStr) {
        log.info("查询 返回多条记录");
        List<Map<String, Object>> maps = template.queryForList(sqlStr);
        return maps;
    }

    /**
     * 查询 多条记录， 返回Bean对象List
     * @return List<Bean>
     */
    public static List<?> queryMultForBean(String sqlStr, Object obj) {
        log.info("查询 多条记录， 返回 Admin 的 Bean对象");
        List<?> listBean = template.query(sqlStr, new BeanPropertyRowMapper<>(obj.getClass()));
        return listBean;
    }

//    /**
//     * 查询 多条记录， 返回 Employee 的 Bean对象
//     * @return List<Employee></Employee>
//     */
//    public static List<Employee> queryForEmployee(String sqlStr) {
//        log.info("查询 多条记录， 返回 Admin 的 Bean对象");
//        List<Employee> listEmployee = template.query(sqlStr, new BeanPropertyRowMapper<>(Employee.class));
//        return listEmployee;
//    }

    /**
     * 插入、 修改、 删除
     * @param sqlStr
     * @return
     */
    public static boolean insertSingle(String sqlStr) {
        log.info("插入、 修改、 删除");
        int insertCount = template.update(sqlStr);
        if (insertCount > 0) {
            return true;
        }else {
            return false;
        }
    }

}
