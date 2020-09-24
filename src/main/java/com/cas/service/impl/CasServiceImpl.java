package com.cas.service.impl;

import com.cas.bean.GlobalParam;
import com.cas.bean.User;
import com.cas.mapper.GlobalParamMapper;
import com.cas.service.CasService;
import com.cas.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.Unsafe;

import javax.annotation.Resource;
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
    //这里相当于库存
    private AtomicInteger initialize= new AtomicInteger(1);

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void testCasMulti(User user) {
        BigDecimal One = new BigDecimal(1);
        while (true) {
            //先检查库存有木有 如果没有就进行下面的操作
            Integer param = (Integer) redisUtil.get("kucun");
            if(param<=0){
                //log.info("停止循环");
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
                    redisUtil.decr("kucun",1L);
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
            //获取unsafe
            U = (Unsafe) f.get(null);
            Class<?> k = CasServiceImpl.class;
            //将 initControl的值初始化给INIT_CONTROL  完成后面的compareAndSwapInt()操作
            INIT_CONTROL = U.objectFieldOffset
                    (k.getDeclaredField("initControl"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
