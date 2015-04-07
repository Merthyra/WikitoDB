package at.ac.tuwien.docspars.io.db;

	import java.util.MissingResourceException;
	import java.util.ResourceBundle;

	public class SQLStatements {
		private static final String BUNDLE_NAME = "sql_statements"; 

		private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

		private SQLStatements() {
		}

		public static String getString(String key) {
			try {
				return RESOURCE_BUNDLE.getString(key);
			} catch (MissingResourceException e) {
				return '!' + key + '!';
			}
		}
}


