package com.coreoz.plume.services.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @deprecated {@link Clock} should be used instead
 */
@Deprecated
public interface TimeProvider {

	Clock clock();

	/**
	 * Returns the current time in milliseconds
	 */
	default long currentTime() {
		return clock().millis();
	}

	default Instant currentInstant() {
		return Instant.now(clock());
	}

	default LocalDate currentLocalDate() {
		return LocalDate.now(clock());
	}

	default LocalDateTime currentDateTime() {
		return LocalDateTime.now(clock());
	}

}
