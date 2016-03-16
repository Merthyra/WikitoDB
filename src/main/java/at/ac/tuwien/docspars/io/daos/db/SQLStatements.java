package at.ac.tuwien.docspars.io.daos.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SQLStatements {
	private static final String BUNDLE_NAME = "sql_statements";
	private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private SQLStatements() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			logger.error(SQLStatements.class.getName() + " Cannot find SQL Statement with key: " + key);
			return '!' + key + '!';
		}
	}
}
