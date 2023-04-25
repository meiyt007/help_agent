package com.zfsoft.outer.inter.util;

import cn.hutool.core.lang.Snowflake;
import com.zfsoft.outer.inter.pojo.HaInterRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/1/6 10:52
 */
@Component
public class InterRecordUtil {

//    @Resource
    private static JdbcTemplate jdbcTemplate;

    @Autowired
    private InterRecordUtil(JdbcTemplate jdbcTemplate) {
        InterRecordUtil.jdbcTemplate = jdbcTemplate;
    }
    /**
     * 雪花算法生成器
     */
    public static Snowflake snowflake = new Snowflake(0, 0);

    /**
     * 日志记录
     *
     * @param interRecord
     */
    public static void interRecord(HaInterRecord interRecord) {
        interRecord.setId(snowflake.nextId());
        String sql = "insert into t_ha_inter_record (ID, NAME, URL, SOURCE_IP, METHOD, PARAM, RESULT_STATUS, RESULT, " +
                "TOTAL_TIME, CREATE_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, interRecord.getId(), interRecord.getName(), interRecord.getUrl(),
                interRecord.getSourceIp(), interRecord.getMethod(), interRecord.getParam(),
                interRecord.getResultStatus(), interRecord.getResult(), interRecord.getTotalTime(), new Date());
    }
}
