/**
 * @(#) PubSub2.class $version 2016. 12. 05
 * <p>
 * Copyright 2007 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kymr.github.io.training;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * PubSub2 
 *
 * @author yongmaroo.kim (yongmaroo.kim@navercorp.com)
 */
public class PubSub2 {
	public static void main(String[] args) {
		/*
		pub -> [data1] -> mapPub -> [data2] -> logsub
		<- subscriber(logSub)
		-> onSubscribe(s)
		-> onNext
		-> onNext
		-> onComplete
		 */
		Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));
		Publisher<Integer> mapPub = mapPub(pub, s -> s * 10);
		Publisher<Integer> map2Pub = mapPub(mapPub, s -> -s);
		map2Pub.subscribe(logSub());
	}

	private static Publisher<Integer> mapPub(Publisher<Integer> pub, Function<Integer, Integer> f) {
		return new Publisher<Integer>() {
			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				pub.subscribe(new Subscriber<Integer>() {
					@Override
					public void onSubscribe(Subscription s) {
						sub.onSubscribe(s);
					}

					@Override
					public void onNext(Integer i) {
						sub.onNext(f.apply(i));
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
			}
		};
	}

	private static Subscriber<Integer> logSub() {
		return new Subscriber<Integer>() {
			@Override
			public void onSubscribe(Subscription s) {
				System.out.println("onSubscribe : ");
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer integer) {
				System.out.println("onNext : " + integer);
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("onError : " +  t);
			}

			@Override
			public void onComplete() {
				System.out.println("onComplete");
			}
		};
	}

	private static Publisher<Integer> iterPub(Iterable<Integer> iter) {
		return new Publisher<Integer>() {
			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				sub.onSubscribe(new Subscription() {
					@Override
					public void request(long n) {
						try {
							iter.forEach(s -> sub.onNext(s));
							sub.onComplete();
						} catch (Throwable t) {
							sub.onError(t);
						}
					}

					@Override
					public void cancel() {

					}
				});
			}
		};
	}
}