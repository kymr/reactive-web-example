/**
 * @(#) FutureSpringApplication.class $version 2016. 12. 20
 * <p>
 * Copyright 2007 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kymr.github.io.future;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

/**
 * FutureSpringApplication 
 */
@Slf4j
//@SpringBootApplication
@EnableAsync
public class FutureSpringApplication {
	@Component
	public static class MyService {
		@Async				// Don't use only this at real environment. It creates new thread at every call.
		public Future<String> hello() throws InterruptedException {
			log.info("hello()");
			Thread.sleep(2000);
			return new AsyncResult<>("Hello");
		}

		@Async
		public ListenableFuture<String> hello2() throws InterruptedException {
			log.info("hello()");
			Thread.sleep(2000);
			return new AsyncResult<>("Hello");
		}
	}

	/*
	if any of Executor / ExecutorService / ThreadPoolTaskExecutor is registered as @Bean, then Spring uses it as default threadpool.
	@Async(value = "tp") can be used, when it needs to use specific threadpool explicitly,
	 */
	@Bean
	ThreadPoolTaskExecutor tp() {
		ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
		te.setCorePoolSize(10);			// default core thread pool
		te.setQueueCapacity(200);
		te.setMaxPoolSize(100);			// after core pool is full -> after queue capacity (generally, unlimited) is full -> then, max pool size works.
		te.setThreadNamePrefix("mythread");
		te.initialize();
		//te.setAllowCoreThreadTimeOut();
		//te.getKeepAliveSeconds()
		//te.setTaskDecorator();		// for task monitoring

		return te;
	}


	public static void main(String[] args) {
		try(ConfigurableApplicationContext c = SpringApplication.run(FutureSpringApplication.class, args)) {
		}
	}

	@Autowired MyService myService;

	@Bean
	ApplicationRunner run() {
		/*
		return args -> {
			log.info("run()");
			Future<String> f = myService.hello();
			log.info("exit : {}", f.isDone());
			log.info("result : {}", f.get());
		};
		*/
		return args -> {
			log.info("run()");
			ListenableFuture<String> f = myService.hello2();
			f.addCallback(
					s -> System.out.println(s),
					e -> System.out.println(e)
			);

			log.info("exit");
		};
	}

}