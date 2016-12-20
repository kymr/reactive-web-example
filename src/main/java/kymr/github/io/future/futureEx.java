/**
 * @(#) futureEx.class $version 2016. 12. 20
 * <p>
 * Copyright 2007 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kymr.github.io.future;

import lombok.extern.slf4j.Slf4j;

import java.nio.channels.CompletionHandler;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * futureEx 
 */
@Slf4j
public class futureEx {
	public static void main(String[] args) throws Exception {
		/*
		why use thraedpool? Reuse thread. It is more cost-effective.
		 */

		//execute();
		//submit();
		//futureTask();
		//futureTask2();
		callbackFutureTask();

		// CompletionHandler since. 1.7
	}

	private static void execute() {
		ExecutorService es = Executors.newCachedThreadPool();
		es.execute(() -> {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}
			log.info("Async");
		});

		log.info("Exit");
	}

	private static void submit() throws ExecutionException, InterruptedException {
		ExecutorService es = Executors.newCachedThreadPool();
		Future<String> f = es.submit(() -> {
			Thread.sleep(2000);
			log.info("Async");
			return "Hello";
		});

		log.info("{}", f.isDone());
		Thread.sleep(2100);
		log.info("Exit");
		log.info("{}", f.isDone());
		log.info(f.get());		// Blocking
	}

	private static void futureTask() throws InterruptedException, ExecutionException {
		ExecutorService es = Executors.newCachedThreadPool();

		FutureTask<String> f = new FutureTask<String>(() -> {
			Thread.sleep(2000);
			log.info("Async");
			return "Hello";
		});

		es.execute(f);
		es.shutdown();

		log.info("{}", f.isDone());
		Thread.sleep(2100);
		log.info("Exit");
		log.info("{}", f.isDone());
		log.info(f.get());		// Blocking
	}

	private static void futureTask2() {
		ExecutorService es = Executors.newCachedThreadPool();

		FutureTask<String> f = new FutureTask<String>(() -> {
			Thread.sleep(2000);
			log.info("Async");
			return "Hello";
		}) {
			@Override
			protected void done() {
				try {
					log.info(get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		};

		es.execute(f);
		es.shutdown();
	}

	interface SuccessCallback {
		void onSuccess(String result);
	}

	interface ExceptionCallback {
		void onError(Throwable t);
	}


	public static class CallbackFutureTask extends FutureTask<String> {
		SuccessCallback sc;
		ExceptionCallback ec;

		public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
			super(callable);
			this.sc = Objects.requireNonNull(sc);
			this.ec = Objects.requireNonNull(ec);
		}

		@Override
		protected void done() {
			try {
				sc.onSuccess(get());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				ec.onError(e.getCause()); 			// Because, e is a wrapped exception,
			}
		}
	}

	private static void callbackFutureTask() {
		ExecutorService es = Executors.newCachedThreadPool();

		CallbackFutureTask f = new CallbackFutureTask(() -> {
			Thread.sleep(2000);
			log.info("Async");
			return "Hello";
		}, s -> {
			log.info(s);
		}, e -> {
			log.error("Error : {]", e.getMessage());
		});

		es.execute(f);
		es.shutdown();
	}
}