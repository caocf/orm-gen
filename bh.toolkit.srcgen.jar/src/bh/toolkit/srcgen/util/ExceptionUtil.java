package bh.toolkit.srcgen.util;

import org.apache.log4j.Logger;

import bh.toolkit.srcgen.exception.AppException;

public class ExceptionUtil {

	public static void handleException(Throwable t, Logger logger) throws AppException {

		if (t instanceof AppException) {

			// Print the given t as the root cause.
			if (t.getCause() == null) {
				logger.error(t.getMessage(), t);
			}

			// Anyway, throw to up level.
			throw (AppException) t;

		} else {

			// If the given t is not the instance of AppException, then get the root cause and print.
			logger.error(t.getMessage(), t.getCause());
			throw new AppException(t);

		}

	}

}
