/**
 * @(#) IntervalEx.class $version 2016. 12. 13
 * <p>
 * Copyright 2007 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kymr.github.io.training.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * IntervalEx 
 *
 * @author yongmaroo.kim (yongmaroo.kim@navercorp.com)
 */
@Slf4j
public class IntervalEx {
	public static void main(String[] args) {
		Publisher<Integer> pub = sub -> {
			sub.onSubscribe(new Subscription() {
				int no = 0;
				boolean cancelled = false;

				@Override
				public void request(long n) {
					ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
					exec.scheduleAtFixedRate(() -> {
						if (cancelled == true) {
							exec.shutdown();
							return;
						}
						sub.onNext(no++);
					}, 0, 300, TimeUnit.MILLISECONDS);
				}

				@Override
				public void cancel() {
					cancelled = true;
				}
			});
		};

		Publisher<Integer> takePub = sub -> {
			pub.subscribe(new Subscriber<Integer>() {
				int count = 0;
				Subscription subscription;

				@Override
				public void onSubscribe(Subscription s) {
					subscription = s;
					sub.onSubscribe(s);
				}

				@Override
				public void onNext(Integer integer) {
					sub.onNext(integer);
					if (++count >= 5) {
						subscription.cancel();
					}
				}

				@Override
				public void onError(Throwable t) {
					sub.onError(t);
				}

				@Override
				public void onComplete() {
					sub.onComplete();
				}
			});
		};

		takePub.subscribe(new Subscriber<Integer>() {
			@Override
			public void onSubscribe(Subscription s) {
				log.debug("onSubscribe");
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer integer) {
				log.debug("onNext : {}", integer);
			}

			@Override
			public void onError(Throwable t) {
				log.debug("onError : {}", t);
			}

			@Override
			public void onComplete() {
				log.debug("onComplete");
			}
		});
	}
}