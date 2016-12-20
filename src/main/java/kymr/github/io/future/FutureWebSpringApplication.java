/**
 * @(#) FutureWebSpringApplication.class $version 2016. 12. 21
 * <p>
 * Copyright 2007 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kymr.github.io.future;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * FutureWebSpringApplication 
 */
//@SpringBootApplication
@Slf4j
@EnableAsync
public class FutureWebSpringApplication {

    @RestController
    public static class MyController {
        @GetMapping("/async")
        public String async() throws InterruptedException {
            Thread.sleep(2000);
            return "hello";
        }

        @GetMapping("/callable")
        public Callable<String> callable() throws InterruptedException {
            /*
            log.info("callable");
            return CompletableFuture.supplyAsync(() -> {
                log.info("async");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "hello";
            });
            */
            log.info("callable");

            return () -> {
                log.info("async");
                Thread.sleep(2000);
                return "hello";
            };

        }

    }

    public static void main(String[] args) {
        SpringApplication.run(FutureWebSpringApplication.class, args);
    }
}

/*
1           ST1 - req   -> blocking IO(DB, API) -> res (html)
2           ST2 - req   -> WorkThread           -> res (html)
3   NIO     ST3
4           ST4
5           ST5

servlet 3.0
 - async servlet
servlet 3.1
 - nonblocking io
 */