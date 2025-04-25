package async;

import static java.lang.Thread.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.ValueSources;

public class CompletableFutureTest {

	@Test
	void 반환값없는_비동기_처리_runAsync() throws InterruptedException, ExecutionException {
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			System.out.println("Async Thread: " + Thread.currentThread().getName());
		});

		Void unused = future.get(); // runAsync() 는 반환 값을 반환 하지 않음

		System.out.println("Test Thread: " + Thread.currentThread().getName());
	}

	@Test
	void 반환값을_받는_비동기처리_supplyAsync() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			System.out.println("Async Thread: " + Thread.currentThread().getName());
			return Thread.currentThread().getName();
		});

		System.out.println("Test Thread: " + Thread.currentThread().getName());
		System.out.println("Async Result: " + future.get()); // supplyAsync() 는 반환 값을 반환 함
	}

	@Test
	void ForkJoinPool이_아닌_다른_쓰레드_풀_생성() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			System.out.println("Async Thread: " + Thread.currentThread().getName());
		}, executorService); // CompletableFuture는 기본적으로 ForkJoinPool 을 사용해서 비동기 처리를 함 하지만 인자로 다른 쓰레드 풀을 사용하게 할 수 있다.

		System.out.println("Test Thread: " + Thread.currentThread().getName());
	}

	@Test
	void 콜백함수_사용_thenApply() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			return "Async Thread : " + Thread.currentThread().getName();
		}).thenApply((threadName) -> {
			return threadName + " - " + "callback thread : " + Thread.currentThread().getName();
		}).thenApply((threadName) -> {
			return threadName + " - " + "callback2 thread : " + Thread.currentThread().getName();
		}); // thenApply() 는 비동기 처리 후에 반환값을 받아서 처리후 값을 반환한다.
		System.out.println("Test Thread: " + Thread.currentThread().getName());
		System.out.println("Async Result: " + future.get());
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void 예외처리_콜백함수사용하기_exceptionally(boolean isDoThrow) throws ExecutionException, InterruptedException {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			return "Async Thread : " + Thread.currentThread().getName();
		}).thenApply((threadName) -> {
			if (isDoThrow) {
				throw new RuntimeException("예외 발생"); // 예외 발생
			}
			return threadName + " - " + "callback thread : " + Thread.currentThread().getName();
		}).thenApply((threadName) -> {
			return threadName + " - " + "callback2 thread : " + Thread.currentThread().getName();
		}).exceptionally((e) -> {
			System.out.println("Exception Async Thread: " + Thread.currentThread().getName());
			System.out.println("Exception: " + e.getMessage());
			return null;
		}); // exceptionally() 는 비동기 처리 중에 예외가 발생했을 때 처리하는 콜백함수이다.

		System.out.println("Test Thread: " + Thread.currentThread().getName());
		System.out.println("Async Result: " + future.get());
	}

	// whenComplete() 는 비동기 처리 후에 성공과 실패를 모두 처리하는 콜백함수이다. 반환값을 바꿀 수는 없다
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void 예외처리_콜백_및_성공_콜백_등록하기(boolean isDoThrow) {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			return "Async Thread : " + Thread.currentThread().getName();
		}).thenApply((threadName) -> {
			if (isDoThrow) {
				throw new RuntimeException("예외 발생"); // 예외 발생
			}
			return threadName + " - " + "callback thread : " + Thread.currentThread().getName();
		}).thenApply((threadName) -> {
			return threadName + " - " + "callback2 thread : " + Thread.currentThread().getName();
		}).whenComplete((result, e) -> { // whenComplete() 는 비동기 처리 후에 성공과 실패를 모두 처리하는 콜백함수이다.
			System.out.println("whenComplete Async Thread: " + Thread.currentThread().getName());
			if (e != null) {
				System.out.println("Exception: " + e.getMessage());
			} else {
				System.out.println("Result: " + result);
			}
		});

		System.out.println("Test Thread: " + Thread.currentThread().getName());
		try{
			String result = future.join();
			System.out.println("Async Result: " + result);
		}catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}
}
