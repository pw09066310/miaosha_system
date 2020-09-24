package com.cas.controller;

import com.cas.bean.User;
import com.cas.service.CasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @ClassName : CasContoller
 * @Description :
 * @Author : pw
 * @Date: 2020-09-23 14:09
 */
@RestController
@RequestMapping("cas")
@Slf4j
public class CasContoller {
    @Resource
    private CasService casService;
    @GetMapping("testcas")
    public void testCas(){
        CyclicBarrier barrier = new CyclicBarrier(100);
        List<Integer> list=new ArrayList();
        IntStream.range(0,1000000).forEach(list::add);
        log.info("开始时间={}", LocalDateTime.now());
        list.parallelStream().forEach(o->{
            int finalI = o;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    User user=new User();
                    user.setUserId(finalI);
                    casService.testCasMulti(user);
                }
            });
        });
        log.info("结束时间={}", LocalDateTime.now());

    }
    ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100,
            60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new RejectedExecutionHandler() 	{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //定义线程池的拒绝策略 直接丢弃  不做处理
            //log.info("discard:{}",r);
        }
    });




}
