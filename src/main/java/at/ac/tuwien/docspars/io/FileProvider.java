package at.ac.tuwien.docspars.io;

import at.ac.tuwien.docspars.util.EndOfProcessReachedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class FileProvider {

  private String filePath;
  private Iterator<File> files = null;
  private String processed = "";
  private final String VALID_FILE_PATTERN;

  private static final Logger logger = LogManager.getLogger(FileProvider.class);


  public FileProvider(String file_path, String file_type) throws IOException {
    super();
    this.filePath = file_path;
    VALID_FILE_PATTERN = ".*\\." + file_type + "$";
  }

  public File getNextFile() {
    if (files.hasNext()) {
      File next = files.next();
      this.processed += next.getName() + System.getProperty("line.separator");
      return next;
    }
    return null;
  }

  public void init() throws NullPointerException {
    List<File> filesList = Arrays.stream(new File(this.filePath).listFiles()).filter(f -> f.getName().matches(VALID_FILE_PATTERN)).distinct()
        .sorted((fa, fb) -> compareFile(fa.getName(), fb.getName())).collect(Collectors.toList());
    this.files = filesList.iterator();
    if (!files.hasNext()) {
      logger.info("no files for processing found in {} ", this.filePath);
      throw new EndOfProcessReachedException("no files to process");
    }
    logger.debug("FoundFiles {} in directory {} ", filesList.size(), this.filePath);
    logger.trace(this.files);
  }

  private int compareFile(String a, String b) {
    if (a.length() < b.length()) {
      return -1;
    }
    return a.compareTo(b);
  }

  /**
   * @return the files
   */
  public Iterator<File> getFiles() {
    return files;
  }

  /**
   * @return the file_path
   */
  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getProcessed() {
    return this.processed;
  }

}
