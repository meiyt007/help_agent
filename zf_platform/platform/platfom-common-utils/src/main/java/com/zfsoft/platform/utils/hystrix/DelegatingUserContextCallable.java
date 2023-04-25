package com.zfsoft.platform.utils.hystrix;

import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019-10-15 15:23
 */
public class DelegatingUserContextCallable<V> implements Callable<V>{

    private final Logger logger = LoggerFactory.getLogger(DelegatingUserContextCallable.class);

    private Callable<V> delegate;

    private CurrentLoginUser originalUserInfo;

    public DelegatingUserContextCallable(Callable<V> delegate,CurrentLoginUser userInfo){
        Assert.notNull(delegate, "delegate cannot be null");
        Assert.notNull(userInfo, "userContext cannot be null");

        this.delegate = delegate;
        this.originalUserInfo = userInfo;
    }

    public DelegatingUserContextCallable(Callable<V> delegate){
        this(delegate, CurrentLoginUserHolder.getCurrentLoginUser());
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public V call() throws Exception {
        CurrentLoginUserHolder.setCurrentLoginUser(originalUserInfo);
        try {
            return delegate.call();
        }finally {
            this.originalUserInfo = null;
        }
    }
}
