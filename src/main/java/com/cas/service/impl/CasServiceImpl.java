package com.cas.service.impl;

import com.cas.bean.User;
import com.cas.service.CasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @ClassName : CasServiceImpl
 * @Description : 实现类
 * @Author : pw
 * @Date: 2020-09-23 14:40
 */
@Service
@Slf4j
public class CasServiceImpl implements CasService {

    private AtomicInteger initialize;
    @Override
    public void testCasMulti(User user) {
        while (true) {
            if(null!=initialize){
                log.info("停止循环");
                break;
            }
            int initControlLocal = initControl;
            /**
             * 如果已经有线程在进行获取了，则直接放弃cpu
             */
            if (initControlLocal < 0) {
//                log.info("initControlLocal < 0,just yield and wait");
                /**
                 * 这里可以不要这个，可以睡眠一小会。
                 */
//                Thread.yield();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    log.warn("e:{}", e);
                }
                continue;
            }


            /**
             * 争夺控制权
             */
            boolean bGotChanceToInit = U.compareAndSwapInt(this,
                    INIT_CONTROL, initControlLocal, -1);
            if (bGotChanceToInit) {
                try {
                    log.info("用户user={},获取到竞争锁",user.getUserId());
                    initialize=new AtomicInteger(789);
                } finally {
                    initControl = 0;
                }

                break;
            }
        }
    }

    private volatile int initControl;



    // Unsafe mechanics java 留给
    private static final sun.misc.Unsafe U;

    private static final long INIT_CONTROL;
    //静态代码块
    static {
        try {
//            U = sun.misc.Unsafe.getUnsafe();
            //初始化 通过反射
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            U = (Unsafe) f.get(null);
            Class<?> k = CasServiceImpl.class;
            INIT_CONTROL = U.objectFieldOffset
                    (k.getDeclaredField("initControl"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
