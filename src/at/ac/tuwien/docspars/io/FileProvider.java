package at.ac.tuwien.docspars.io;

import java.beans.ConstructorProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileProvider {
	
	private final String file_path;
	private final String file_type;
	private int max_Files;
	private ArrayList<File> files = null;
	private Iterator<File> it;
	
	private static final Logger logger = LogManager.getLogger(FileProvider.class.getName());
		
//	@ConstructorProperties({"file_path", "file_type", "max_Files"})
	public FileProvider(String file_path, String file_type, int max_Files) {
		super();
		this.file_path = file_path;
		this.file_type = file_type;
		this.max_Files = max_Files;
		
		File[] files = new File(this.file_path).listFiles();		
		this.files = new ArrayList<File>(Arrays.asList(files));		
		this.it = this.files.iterator();
		logger.trace("Found " + this.files.size() + " Files in directory " + this.file_path);	
	}
	

	public File getNextFile() {
		if (it.hasNext() && this.max_Files > 0) {
			this.max_Files--;
			return (File) this.it.next();
		}
		return null;
	}

}
