package com.cas.controller;

import com.cas.bean.User;
import com.cas.service.CasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
        for(int i=0;i<100;i++){
            int finalI = i;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    User user=new User();
                    user.setUserId(finalI);
                    casService.testCasMulti(user);

                }
            });
        }
    }
    ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100,
            60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new RejectedExecutionHandler() 	{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.info("discard:{}",r);
        }
    });




}
