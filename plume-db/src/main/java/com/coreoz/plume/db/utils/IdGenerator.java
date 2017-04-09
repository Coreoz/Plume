package com.coreoz.plume.db.utils;

import java.net.InetAddress;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Handle long identifiers generation that should be unique.
 *
 * @author <a href="mike@baroukh.com">Mike Baroukh</a>
 */
public class IdGenerator {

	private static long counterMax = 0;
	private static long counter = counterMax+1;

	private static void calcCounter() {
		// bits 33 -> 62 (30) : current time in seconds
		counter = ((System.currentTimeMillis() / 1000) & 0x3FFFFFFFL) << 33;

		String ip = "127.0.0.1";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Throwable t) {
			// Don't care any error
		}

		// bits 25 -> 32 (8)
		if (ip.equals("127.0.0.1")) {
			// 127.0.0.1 is too common. Instead, generate a unique string.
			counter |= (ThreadLocalRandom.current().nextInt(256) & 0xFFL) << 25;
		} else {
			counter |= (Long.parseLong(ip.substring(ip.lastIndexOf('.')+1)) & 0xFF) << 25;
		}

		// bits 21 -> 24 (3)
		counter |= (ThreadLocalRandom.current().nextInt(16) & 0x0FL) << 21;

		// All remaining bits 0 -> 20 (21) are the counter

		// It will count until it reach
		counterMax = counter + 0x1FFFFF; // 21 bits
	}

	public static synchronized long generate() {
		if (counter>counterMax) {
			calcCounter();
		}
		return counter++;
	}

}
