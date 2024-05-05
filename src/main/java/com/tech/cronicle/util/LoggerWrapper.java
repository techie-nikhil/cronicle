package com.tech.cronicle.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerWrapper {

	private Logger LOGGER;

	private LoggerWrapper(Class<?> clazz) {
		LOGGER = LogManager.getLogger(clazz);
	}

	public static LoggerWrapper getLogger(Class<?> clazz) {

		LoggerWrapper logWrapper = new LoggerWrapper(clazz);
		return logWrapper;
	}

	public static LoggerWrapper getLogger(String className) {
		LoggerWrapper logWrapper = null;
		try {
			logWrapper = new LoggerWrapper(Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return logWrapper;
	}

	public void debug(Object message) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message);
		}
	}

	public void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}

	public void debug(Object... message) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message);
		}
	}

	public void debug(Object message, Throwable t) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, t);
		}
	}

	public void debug(String message, Throwable t) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, t);
		}
	}

	public void error(Object message) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message);
		}
	}

	public void error(Object... message) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message);
		}
	}

	public void error(Object message, Throwable t) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, t);
		}
	}

	public void error(String message, Throwable t) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, t);
		}
	}

	public void error(String message, Object... params) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, params);
		}
	}

	public void info(Object message) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(message);
		}
	}

	public void info(Object... message) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(message);
		}
	}

	public void info(Object message, Throwable t) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(message, t);
		}
	}

	public void info(String message, Throwable t) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(message, t);
		}
	}

	public void info(String message, Object... params) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(message, params);
		}
	}

	public void fatal(Object message) {
		if (LOGGER.isFatalEnabled()) {
			LOGGER.fatal(message);
		}
	}

	public void fatal(String message, Object... params) {
		if (LOGGER.isFatalEnabled()) {
			LOGGER.fatal(message, params);
		}
	}

	public void request(String message, Object... params) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Request: " + message, params);
		}
	}

	public void response(String message, Object... params) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Response: " + message, params);
		}
	}
}
