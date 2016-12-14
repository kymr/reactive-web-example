/**
 * @(#) HelloController.class $version 2016. 12. 14
 * <p/>
 * Copyright 2007 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kymr.github.io.controller;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * HelloController 
 *
 * @author yongmaroo.kim (yongmaroo.kim@navercorp.com)
 */
@RestController
public class HelloController {
	@RequestMapping("/hello")
	public Publisher<String> hello(String name) {
		return new Publisher<String>() {
			@Override
			public void subscribe(Subscriber<? super String> s) {
				s.onSubscribe(new Subscription() {
					@Override
					public void request(long n) {
						s.onNext("Hello " + name);
						s.onComplete();
					}

					@Override
					public void cancel() {

					}
				});
			}
		};
	}

	@RequestMapping("/hello2")
	public Mono<String> hello2(String name) {
		return Mono.just(name)
					.map(s -> "Hello " + s);
	}
}