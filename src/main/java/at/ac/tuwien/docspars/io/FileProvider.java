package at.ac.tuwien.docspars.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileProvider {

	private String file_path;
	private final String file_type;
	private ArrayList<File> files = null;
	private String processed = "";
	private Iterator<File> it;

	private static final Logger logger = LogManager.getLogger(FileProvider.class.getName());

	// @ConstructorProperties({"file_path", "file_type", "max_Files"})
	public FileProvider(String file_path, String file_type) throws IOException {
		super();
		this.file_path = file_path;
		this.file_type = file_type;
		try {
		init();
		} 
		catch (NullPointerException ex) {
			throw new IOException("No valid file path provided");
		}
	}

	public File getNextFile() {
		if (it.hasNext()) {
			File next = it.next();
			this.processed += next.getName() + System.getProperty("line.separator");
			return next;
		}
		return null;
	}
	
	private void init() throws NullPointerException {
		File[] files = new File(this.file_path).listFiles();
		if (files == null) files = new File[] {};
		this.files =  new ArrayList<File>(Arrays.asList(files));
		this.it = this.files.iterator();
		processed = "";
		logger.debug("Found " + this.files.size() + " Files in directory " + this.file_path);
	}

	/**
	 * @return the file_type
	 */
	public String getFile_type() {
		return file_type;
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

	public void updateFilePath(String filePath) {
		this.file_path = filePath;
		init();
	}
	
	public String getProcessed() {
		return this.processed;
	}

}
