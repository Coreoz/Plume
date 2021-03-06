package com.coreoz.plume.scheduler;

import com.coreoz.wisp.time.TimeProvider;

class PlumeTimeProvider implements TimeProvider {

	private final com.coreoz.plume.services.time.TimeProvider plumeTimeProvider;

	public PlumeTimeProvider(com.coreoz.plume.services.time.TimeProvider plumeTimeProvider) {
		this.plumeTimeProvider = plumeTimeProvider;
	}

	@Override
	public long currentTime() {
		return plumeTimeProvider.currentTime();
	}

}
