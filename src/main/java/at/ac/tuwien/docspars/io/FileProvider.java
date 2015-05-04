package at.ac.tuwien.docspars.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileProvider {

	private String file_path;
	private final String file_type;
	private int max_Files;
	private ArrayList<File> files = null;
	private Iterator<File> it;

	private static final Logger logger = LogManager.getLogger(FileProvider.class.getName());

	// @ConstructorProperties({"file_path", "file_type", "max_Files"})
	public FileProvider(String file_path, String file_type, int max_Files) {
		super();
		this.file_path = file_path;
		this.file_type = file_type;
		this.max_Files = max_Files;

		File[] files = new File(this.file_path).listFiles();
		this.files = new ArrayList<File>(Arrays.asList(files));
		this.it = this.files.iterator();
		logger.debug("Found " + this.files.size() + " Files in directory " + this.file_path);
	}

	public File getNextFile() {
		if (it.hasNext() && this.max_Files > 0) {
			this.max_Files--;
			return (File) this.it.next();
		}
		return null;
	}

	/**
	 * @return the file_type
	 */
	public String getFile_type() {
		return file_type;
	}

	/**
	 * @return the max_Files
	 */
	public int getMax_Files() {
		return max_Files;
	}

	/**
	 * @param max_Files
	 *            the max_Files to set
	 */
	public void setMax_Files(int max_Files) {
		this.max_Files = max_Files;
	}

	/**
	 * @return the files
	 */
	public ArrayList<File> getFiles() {
		return files;
	}

	/**
	 * @param files
	 *            the files to set
	 */
	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}

	/**
	 * @return the it
	 */
	public Iterator<File> getIt() {
		return it;
	}

	/**
	 * @param it
	 *            the it to set
	 */
	public void setIt(Iterator<File> it) {
		this.it = it;
	}

	/**
	 * @return the file_path
	 */
	public String getFile_path() {
		return file_path;
	}

	public void setFilePath(String filePath) {
		this.file_path = filePath;
	}

}
