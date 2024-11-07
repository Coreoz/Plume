package com.coreoz.plume.jersey.async;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import jakarta.annotation.Nonnull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Provides a bridge between OkHttp asynchronous API and Java 8 asynchronous API
 * @see CompletableFuture
 */
public class AsyncOkHttp {

	/**
	 * Execute asynchronously a OkHttp {@link Call}.
	 * Usage example:
	 * @return A {@link CompletableFuture} OkHttp {@link Response}
	 */
    @Nonnull
	public static CompletableFuture<Response> executeAsync(@Nonnull Call okHttpCall) {
		CompletableFuture<Response> promise = new CompletableFuture<>();
		okHttpCall.enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				promise.complete(response);
			}

			@Override
			public void onFailure(Call call, IOException e) {
				promise.completeExceptionally(e);
			}
		});
		return promise;
	}

	/**
	 * Wrap a function that throws an exception in a classic {@link Function}.
	 * If the "unsafe" function throws an exception, it will be propagated as an unchecked exception.
	 */
    @Nonnull
	public static<R> Function<Response, R> wrapUncheked(@Nonnull ThrowingFunction<Response, R> throwingFunction) {
		return response -> {
			try {
				return throwingFunction.apply(response);
			} catch (Exception e) {
				sneakyThrow(e);
				// will never be called since the exception will be thrown
				return null;
			}
		};
	}

	@FunctionalInterface
	public static interface ThrowingFunction<T, R> {
		R apply(T t) throws Exception;
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
		throw (E) e;
	}

}

