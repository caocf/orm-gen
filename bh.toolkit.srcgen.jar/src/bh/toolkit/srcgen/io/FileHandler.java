package bh.toolkit.srcgen.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

/**
 * <code>FileHandler</code> is a class that define the basic file operation such as read, write, copy, move, delete and so on.
 */
public class FileHandler {

	private static Logger logger = Logger.getLogger(FileHandler.class);

	/**
	 * Operation type.
	 */
	public static final int FILE_ONLY = 0;
	public static final int DIRECTORY_ONLY = 1;
	public static final int FILE_AND_DIRECTORY = 2;

	public static boolean isFile(String absoluteName) throws IOException {
		File dtoFile = new File(absoluteName);
		if (dtoFile.exists() == false || dtoFile.isFile() == false) {
			return false;
		}
		return true;
	}

	/**
	 * @param file
	 *            File
	 * @return String
	 * @throws IOException
	 */
	public static String readTextFile(File file) throws IOException {
		return readTextFile(file.getAbsolutePath());
	}

	/**
	 * @param fileName
	 *            String The absolute path of a file
	 * @return String
	 * @throws IOException
	 */
	public static String readTextFile(String fileName) throws IOException {

		BufferedReader in = null;
		StringBuffer all = new StringBuffer();
		String line = null;
		String lineSep = System.getProperty("line.separator");

		try {
			in = new BufferedReader(new FileReader(fileName));
			line = in.readLine();

			while (line != null) {
				all.append(line + lineSep);
				line = in.readLine();
			}
		} finally {
			// try {
			if (in != null) {
				in.close();
			}
			// } catch (Exception ex) {
			// throw new ReadFileException(ex);
			// }
		}

		return all.toString();
	}

	/**
	 * Read file with the specified encoding.
	 * 
	 * @param file
	 *            File
	 * @param enc
	 *            String
	 * @return String
	 * @throws IOException
	 */
	public static String readTextFile(File file, String enc) throws IOException {
		return readTextFile(file.getAbsolutePath(), enc);
	}

	/**
	 * Read file with the specified encoding.
	 * 
	 * @param fileName
	 *            java.lang.String
	 * @param enc
	 *            java.lang.String
	 * @return java.lang.String
	 * @throws IOException
	 */
	public static String readTextFile(String fileName, String enc) throws IOException {

		InputStreamReader in = null;
		StringBuffer all = new StringBuffer();
		int bufferLength = 512;
		int readLength = 0;
		char[] ch = new char[bufferLength];

		try {
			in = new InputStreamReader(new FileInputStream(fileName), enc);

			readLength = in.read(ch, 0, bufferLength);
			while (readLength != -1) {
				all.append(ch, 0, readLength);
				readLength = in.read(ch, 0, bufferLength);
			}
		} finally {
			// try {
			if (in != null) {
				in.close();
			}
			// } catch (Exception ex) {
			// throw new ReadFileException(ex);
			// }
		}

		return all.toString();
	}

	/**
	 * @param file
	 *            java.io.File
	 * @param content
	 *            java.lang.String
	 * @param append
	 *            boolean
	 * @throws IOException
	 */
	public static void writeTextFile(File file, String content, boolean append) throws IOException {

		writeTextFile(file.getAbsolutePath(), content, append);
	}

	/**
	 * @param fileName
	 *            java.lang.String The absolute path of the file ready to be written.
	 * @param content
	 *            java.lang.String
	 * @throws IOException
	 */
	public static void writeTextFile(String fileName, String content, boolean append) throws IOException {

		writeTextFile(fileName, content, "utf-8", append);

		//		BufferedWriter out = null;
		//
		//		try {
		//
		//			File file = new File(fileName);
		//			File parentPath = new File(file.getParent());
		//
		//			if (parentPath.exists() == false) {
		//				parentPath.mkdirs();
		//			} else if (parentPath.isFile() == true) {
		//				parentPath.mkdirs();
		//			}
		//
		//			// Create a empty new file if the file does not exist.
		//			file.createNewFile();
		//
		//			out = new BufferedWriter(new FileWriter(file, append));
		//			if (append) {
		//				out.newLine();
		//			}
		//			out.write(content);
		//
		//			logger.info("++++++++++【新建】" + file.getAbsoluteFile().toString());
		//
		//		} finally {
		//			// try {
		//			if (out != null) {
		//				out.close();
		//			}
		//			// } catch (Exception ex) {
		//			// throw new WriteFileException(ex);
		//			// }
		//		}

	}

	public static void writeTextFile(String fileName, String content, String charset, boolean append) throws IOException {

		BufferedWriter bw = null;

		try {

			File file = new File(fileName);
			File parentPath = new File(file.getParent());

			if (parentPath.exists() == false) {
				parentPath.mkdirs();
			} else if (parentPath.isFile() == true) {
				parentPath.mkdirs();
			}

			// Create a empty new file if the file does not exist.
			file.createNewFile();

			OutputStream fos = new FileOutputStream(fileName, append);
			bw = new BufferedWriter(new OutputStreamWriter(fos, charset));
			if (append) {
				bw.newLine();
			}
			bw.write(content);

			logger.info("++++++++++【新建】" + file.getAbsoluteFile().toString());

		} finally {
			// try {
			if (bw != null) {
				bw.close();
			}
			// } catch (Exception ex) {
			// throw new WriteFileException(ex);
			// }
		}

	}

	/**
	 * @param optType
	 *            int
	 * @param directory
	 *            java.lang.String
	 * @param ext
	 *            java.lang.String
	 * @param isRecursive
	 *            boolean
	 * @return java.util.ArrayList
	 * @throws IOException
	 */
	public static ArrayList<File> listFiles(int optType, String directory, String ext, boolean isRecursive) throws IOException {

		return listFiles(optType, new File(directory), ext, isRecursive);
	}

	/**
	 * @param directory
	 *            java.lang.String
	 * @param fileName
	 *            java.lang.String
	 * @param isRecursive
	 *            boolean
	 * @return java.util.ArrayList
	 * @throws IOException
	 */
	public static ArrayList<File> listFiles(File directory, String fileShortName, boolean isRecursive) throws IOException {

		// List all file objects under current directory.
		File[] currentFiles = directory.listFiles();

		// Result container.
		ArrayList<File> resultFiles = new ArrayList<File>();

		String fileName = null;
		for (int i = 0; i < currentFiles.length; i++) {

			// Detect the file extension.
			fileName = currentFiles[i].getName();

			if (fileName.equals(fileShortName)) {
				resultFiles.add(currentFiles[i]);
			} else {
				// No operation
			}

			// If current object is a file
			if (currentFiles[i].isDirectory() == true) {
				// make a recursive invoke.
				if (isRecursive == true) {
					resultFiles.addAll(listFiles(currentFiles[i], fileShortName, isRecursive));
				}
			}
		}

		return resultFiles;
	}

	/**
	 * @param optType
	 *            int
	 * @param directory
	 *            java.lang.String
	 * @param ext
	 *            java.lang.String
	 * @param isRecursive
	 *            boolean
	 * @return java.util.ArrayList A list of File objects.
	 * @throws IOException
	 */
	public static ArrayList<File> listFiles(int optType, File directory, String ext, boolean isRecursive) throws IOException {

		// Invalid argument.
		if ((optType == DIRECTORY_ONLY) && StringUtils.isNotBlank(ext) == true) {
			throw new IOException("Can not specify an extension for directory operation.");
		}

		// List all file objects under current directory.
		File[] currentFiles = directory.listFiles();

		// Result container.
		ArrayList<File> resultFiles = new ArrayList<File>();

		String fileName = null;
		for (int i = 0; i < currentFiles.length; i++) {

			// If current object is a file
			if (currentFiles[i].isFile() == true) {

				// If operation is file only or both.
				if (optType == FILE_ONLY || optType == FILE_AND_DIRECTORY) {

					// Detect the file extension.
					fileName = currentFiles[i].getAbsolutePath();

					if (StringUtils.isNotBlank(ext) == true && fileName.endsWith(ext) == false) {
						// No operation.
					} else {
						// Otherwise, save it to container.
						resultFiles.add(currentFiles[i]);
					}
				}
				// If operation is directory only, no action.
				else {
					// No operation.
				}
			}
			// If current object is a directory
			else {
				// If operation is file only, only make a recursive
				// invoke.
				if ((optType == FILE_ONLY) && (isRecursive == true)) {
					resultFiles.addAll(listFiles(optType, currentFiles[i], ext, isRecursive));
				}
				// If operation if file only, and not need recursive, no action.
				else if (optType == FILE_ONLY && isRecursive == false) {
					// No operation.
				}
				// If operation is directory only or both, save it
				// to container and then make a recursive invoke.
				else {
					resultFiles.add(currentFiles[i]);
					if (isRecursive == true) {
						resultFiles.addAll(listFiles(optType, currentFiles[i], ext, isRecursive));
					}
				}
			}
		}

		return resultFiles;
	}

	/**
	 * @param source
	 *            java.lang.String The absolute path of source file.
	 * @param dest
	 *            java.lang.String The absolute path of destination file.
	 * @throws IOException
	 */
	public static void copyFile(String source, String dest) throws IOException {
		File srcFile = new File(source);
		File destFile = new File(dest);
		copyFile(srcFile, destFile);
	}

	/**
	 * Make a backup copy file of specified source file
	 * 
	 * @param srcFile
	 *            java.io.File The abstract representation of source file.
	 * @param destFile
	 *            java.io.File The abstract representation of destination file.
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {

		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			int bufferLength = 512;
			int readLength = 0;
			byte[] data = new byte[bufferLength];
			readLength = in.read(data, 0, bufferLength);
			while (readLength != -1) {
				out.write(data, 0, readLength);
				readLength = in.read(data, 0, bufferLength);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * @param source
	 * @param destination
	 * @throws IOException
	 */
	public static void copyFileTree(String source, String destination) throws IOException {

		File srcDir = new File(source);
		File destDir = new File(destination);
		copyFileTree(srcDir, destDir);
	}

	/**
	 * Copy files and sub-directories from source directory to destination direcoty.
	 * 
	 * @param srcDir
	 * @param destDir
	 * @throws IOException
	 */
	public static void copyFileTree(File srcDir, File destDir) throws IOException {

		// List all directories, sub-directories and files under
		// source directory
		ArrayList<File> allFileObjs = listFiles(FILE_AND_DIRECTORY, srcDir, null, true);

		// Process all the objects with two cycles, the first cycle
		// create new directories, and the second one copy files.
		String srcDirName = srcDir.getAbsolutePath();
		String destDirName = destDir.getAbsolutePath();
		int srcDirNameLen = srcDirName.length();
		String relativePathName = null;
		int relativePathNameLen = 0;
		File currentSrcObj = null;
		File currentDestObj = null;
		for (int i = 0; i < allFileObjs.size(); i++) {

			currentSrcObj = (File) allFileObjs.get(i);

			if (currentSrcObj.isDirectory() == true) {
				// Get the relative path of current object and
				// use it with the destination directory to
				// create a new directory.
				relativePathName = currentSrcObj.getAbsolutePath();
				relativePathNameLen = relativePathName.length();
				relativePathName = relativePathName.substring(srcDirNameLen, relativePathNameLen);
				currentDestObj = new File(destDirName + relativePathName);
				currentDestObj.mkdirs();
			}
		}

		for (int i = 0; i < allFileObjs.size(); i++) {

			currentSrcObj = (File) allFileObjs.get(i);

			if (currentSrcObj.isDirectory() == false) {
				// Get the relative path of current object and
				// use it with the destination directory to
				// copy file.
				relativePathName = currentSrcObj.getAbsolutePath();
				relativePathNameLen = relativePathName.length();
				relativePathName = relativePathName.substring(srcDirNameLen, relativePathNameLen);
				currentDestObj = new File(destDirName + relativePathName);
				copyFile(currentSrcObj, currentDestObj);
			}
		}
	}

	/**
	 * @param souce
	 *            java.lang.String The absolute path of source file.
	 * @param dest
	 *            java.lang.String The absolute path of destination file.
	 */
	public static void move(String source, String dest) {
		File srcFile = new File(source);
		File destFile = new File(dest);
		move(srcFile, destFile);
	}

	/**
	 * Move the specified source file to the destination file.
	 * 
	 * @param srcFile
	 *            java.io.File The abstract representation of source file.
	 * @param destFile
	 *            java.io.File The abstract representation of destination file.
	 */
	public static void move(File srcFile, File destFile) {
		if (srcFile.exists() && srcFile.isFile() && !destFile.isDirectory()) {
			srcFile.renameTo(destFile);
			srcFile.delete();
		}
	}

	/**
	 * @param directory
	 *            java.lang.String
	 */
	public static void delete(String directory) {
		delete(new File(directory));
	}

	/**
	 * @param directory
	 *            java.io.File
	 */
	public static void delete(File directory) {
		File[] allFiles = directory.listFiles();
		for (int i = 0; i < allFiles.length; i++) {
			allFiles[i].delete();
		}
	}

	public static String backup(String origFileName) throws IOException {

		// Get backup file name by inserting timestamp before extension name.
		String timestamp = DateFormatUtils.format(Calendar.getInstance().getTime(), "[yyyy.MM.dd'T'HH.mm.ss]");
		int lastIdxOfDot = origFileName.lastIndexOf(".");
		String backupFileName = origFileName.substring(0, lastIdxOfDot + 1) + timestamp + origFileName.substring(lastIdxOfDot, origFileName.length());

		// Create backup file by renaming original file.
		File origFile = new File(origFileName);
		File backFile = new File(backupFileName);

		logger.info("BACKUP: Begin to make a backup for file: " + origFileName);
		FileUtils.copyFile(origFile, backFile);

		return backupFileName;

	}

	/**
	 * Count the lines in one file.
	 * 
	 * @param fileName
	 *            java.lang.String The absolute path of the file to be counted.
	 * @return int[] Line count and comment count.
	 * @throws IOException
	 */
	public static int[] countLine(String fileName) throws IOException {

		BufferedReader in = null;
		String line = null;
		int lineCount = 0;
		int commentsCount = 0;
		try {
			in = new BufferedReader(new FileReader(fileName));
			line = in.readLine();
			while (line != null) {
				line = line.trim();
				// Blank line is ignored.
				if (line.length() == 0) {
					line = in.readLine();
					continue;
				}

				if (!line.startsWith("/*") && !line.startsWith("*") && !line.startsWith("//")) {
					lineCount++;
				} else {
					commentsCount++;
				}
				line = in.readLine();
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return new int[] { lineCount, commentsCount };

	}

	/**
	 * For unit test only
	 */
	public static void main(String[] args) {
		try { // Display the usage of this class.
			if (args.length == 0) {
				System.out.println("Usage: java FileHander <options> <parameter>");
				System.out.println("Possible options include:");
				System.out.println("\t-l List files under specified directory " + "with specified extension.");
				System.out.println("\t-lw List files under specified directory " + "with specified extension, and write them into specified file.");
				System.out.println("\t-c Copy files");
				System.exit(0);
			}

			// List all file names under specified directory.
			if (args[0].equals("-l")) {
				if (args.length != 3) {
					System.out.println("Usage: java FileHandler -l ");
					System.out.println("<directory> <extension>");
					System.exit(0);
				}

				ArrayList<File> allFiles = listFiles(FILE_AND_DIRECTORY, new File(args[1]), null, true);
				for (int i = 0; i < allFiles.size(); i++) {
					System.out.println(((File) allFiles.get(i)).getAbsolutePath());
				}
			}

			// List all file names under specified directory and write these
			// names into a file.
			else if (args[0].equals("-lw")) {
				if (args.length != 4) {
					System.out.println("Usage: java FileHandler -lw ");
					System.out.println("<directory> <extension> <filename>");
					System.exit(0);
				}

				ArrayList<File> allFiles = listFiles(FILE_AND_DIRECTORY, new File(args[1]), args[2], true);

				// Construct file content.
				StringBuffer fileContent = new StringBuffer();
				for (int i = 0; i < allFiles.size(); i++) {
					fileContent.append(((File) allFiles.get(i)).getAbsoluteFile());
					fileContent.append("\n");
				}

				// Write the file content to a file.
				writeTextFile(args[3], fileContent.toString(), true);
			}

			// Test copy method
			else if (args[0].equals("-c")) {
				if (args.length != 3) {
					System.out.println("Usage: java FileHandler -c ");
					System.out.println("<source file> <destination file>");
					System.exit(0);
				}

				copyFile(args[1], args[2]);

			} else {
				System.out.println("Invalid parameter");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}