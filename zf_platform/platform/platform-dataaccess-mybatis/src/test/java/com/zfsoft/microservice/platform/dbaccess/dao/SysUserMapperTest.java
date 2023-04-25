package com.zfsoft.microservice.platform.dbaccess.dao;

import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysUserMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysUser;
import groovy.util.logging.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest()
@ActiveProfiles({"shdtest"})
@Slf4j
public class SysUserMapperTest {
    @Autowired
    private DbSysUserMapper dbSysUserMapper;
    @Test
    void contextLoads() {
    }

    @Test
    public void isNotNullOfSysUserMapper(){
        Assertions.assertNotNull(dbSysUserMapper);
    }
    @Test
    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public void getAllSysUser(){
        List<DbSysUser> userList = dbSysUserMapper.selectByExample(null);
        Assertions.assertTrue(userList.size()>0);
    }
}
