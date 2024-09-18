package com.coreoz.plume.jersey.async;

import com.coreoz.plume.jersey.errors.ErrorResponse;
import com.coreoz.plume.jersey.errors.WsError;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.AsyncResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Provides a bridge between JAX-RS asynchronous API and Java 8 asynchronous API
 * @see CompletableFuture
 * @see AsyncResponse
 */
@Slf4j
public class AsyncJersey {
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
				asyncResponse.resume(new ErrorResponse(WsError.INTERNAL_ERROR, List.of()));
			}
		};
	}
}
