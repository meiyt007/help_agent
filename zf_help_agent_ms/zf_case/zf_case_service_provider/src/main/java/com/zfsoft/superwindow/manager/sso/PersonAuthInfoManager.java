package com.zfsoft.superwindow.manager.sso;

import com.zfsoft.superwindow.dbaccess.dao.DbPersonAuthInfoMapper;
import com.zfsoft.superwindow.dbaccess.data.DbPersonAuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonAuthInfoManager {

    @Resource
    private DbPersonAuthInfoMapper dbPersonAuthInfoMapper;

    public int saveOrUpdatePersonAuthInfo(DbPersonAuthInfo dbPersonAuthInfo) {
        return dbPersonAuthInfoMapper.insert(dbPersonAuthInfo);
    }
}
