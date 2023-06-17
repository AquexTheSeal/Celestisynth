package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.core.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Celestisynth {

	public static final String MODID = "celestisynth";
	public static final String MOD_NAME = "Celestisynth";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public static void init() {
		Registry.loadClasses();
	}

	public static void logWarning(String msg) {
		LOGGER.warn("Celestisynth: " + msg);
	}

	public static void logInfo(String msg) {
		LOGGER.info("Celestisynth: " + msg);
	}

	public static void logDebug(String msg) {
		LOGGER.debug("Celestisynth: " + msg);
	}

	public static <T> void logDebug(String msg, T loadedService, Class<T> clazz) {
		LOGGER.info("Celestisynth: " + msg, loadedService, clazz);
	}

	public static void logError(String msg) {
		LOGGER.error("Celestisynth: " + msg);
	}
	public static void logError(String msg, Throwable throwable) {
		LOGGER.error("Celestisynth:" + msg, throwable);
	}
}