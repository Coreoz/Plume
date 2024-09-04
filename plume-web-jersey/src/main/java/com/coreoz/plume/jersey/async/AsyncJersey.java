package com.coreoz.plume.jersey.async;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import jakarta.ws.rs.container.AsyncResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coreoz.plume.jersey.errors.ErrorResponse;
import com.coreoz.plume.jersey.errors.WsError;
import com.google.common.collect.ImmutableList;

/**
 * Provides a bridge between JAX-RS asynchronous API and Java 8 asynchronous API
 * @see CompletableFuture
 * @see AsyncResponse
 */
public class AsyncJersey {

	private static final Logger logger = LoggerFactory.getLogger(AsyncJersey.class);

	/**
	 * Provides a {@link BiConsumer} from an {@link AsyncResponse}
	 * that should be called when a {@link CompletableFuture} is terminated.<br>
	 * <br>
	 * Usage example:
	 * <code><pre>
	 * &commat;GET
	 * public void fetchAsync(@Suspended AsyncResponse asyncResponse) {
	 *   asyncService
	 *     .fetchDataAsync()
	 *     .whenCompleteAsync(toAsyncConsumer(asyncResponse));
	 * }
	 * </pre></code>
	 */
	public static BiConsumer<? super Object, ? super Throwable> toAsyncConsumer(AsyncResponse asyncResponse) {
		return (responseBody, exception) -> {
			if (exception == null) {
				asyncResponse.resume(responseBody);
			} else {
				logger.error("An exception was raised during the promise execution", exception);
				asyncResponse.resume(new ErrorResponse(WsError.INTERNAL_ERROR, ImmutableList.of()));
			}
		};
	}

}

