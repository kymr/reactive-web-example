/**
 * @(#) EmitterApplication.class $version 2016. 12. 21
 * <p>
 * Copyright 2007 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kymr.github.io.future;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;

/**
 * EmitterApplication 
 */
@Slf4j
@SpringBootApplication
@EnableAsync
public class EmitterApplication {
	@RestController
	public static class MyController {
		@GetMapping("/emitter")
		public ResponseBodyEmitter callable() throws InterruptedException {
			ResponseBodyEmitter emitter = new ResponseBodyEmitter();

			Executors.newSingleThreadExecutor().submit(() -> {
				try {
					for (int i = 0; i < 50; i++) {
						emitter.send("<p>Stream " + i + "</p>");
						Thread.sleep(2000);
					}
				} catch (Exception e) {
				}
			});

			return emitter;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(EmitterApplication.class, args);
	}
}