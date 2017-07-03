package com.mars.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class provides basic functionalities related to File handling.
 *
 * @author Bhanu Mishra Created on 04-01-2016.
 */
public final class FileUtils {
	private static final Logger logger = Logger.getLogger(FileUtils.class.getSimpleName());
	// private static final String FILE_SEP = System.getProperty("file.separator");
	// private static final String PATH_SEP = System.getProperty("path.separator");

	private static boolean status = false;

	public static void checkAndDelete(final File file) {
		String name = "";
		if (fileExists(file) && file.isFile()) {
			name = file.getPath();
			status = file.delete();
		}

		logger.info(status && !name.trim().isEmpty() ? "File deleted: " + name : "File does not exists!");
	}

	public static void checkAndDelete(final String name) {
		checkAndDelete(new File(name));
	}

	public static void createFile(final String filePath) throws IOException {
		File file = new File(filePath);
		String message = "";

		if (!fileExists(file)) {
			status = file.getParentFile().mkdirs();
			message = status ? "File directory and " : "";
			status = file.createNewFile();
		}

		logger.info(status ? message + "File created " + filePath : "ERROR: Unable to create file at given path.");
	}

	public static void deleteAllFilesFromDir(final File dir) {
		if (fileExists(dir)) {
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				if (files != null && files.length > 0) {
					for (File file : files) {
						checkAndDelete(file);
					}
				}
			} else if (dir.isFile()) {
				deleteAllFilesFromDir(dir.getParentFile());
			} else {
				checkAndDelete(dir);
			}
		}
	}

	public static void deleteAllFilesFromDir(final String dir) throws IOException {
		if (dir != null && !dir.trim().isEmpty()) {
			File file = new File(dir);

			if (file != null && fileExists(file)) {
				if (file.isDirectory()) {
					deleteAllFilesFromDir(file);
				} else if (file.isFile()) {
					deleteAllFilesFromDir(file.getParentFile());
				}
			}
		}
	}

	public static boolean fileEquals(final File org, final File dup) throws IOException {
		byte[] b1 = getBytesFromFile(org);
		byte[] b2 = getBytesFromFile(dup);

		if (b1.length != b2.length)
			return false;
		for (int i = 0; i < b1.length; i++) {
			if (b1[i] != b2[i])
				return false;
		}
		return true;
	}

	public static boolean fileExists(final File file) {
		return file != null && file.exists();
	}

	public static boolean fileExists(final String name) {
		return name != null && !name.trim().isEmpty() && fileExists(new File(name));
	}

	/**
	 * returns the index (0 indexed) of the first difference, or -1 if identical fails for files 2G or more due to
	 * limitations of "int"... use long if needed
	 */
	static int firstDiffBetween(final File f1, final File f2) throws IOException {
		final byte[] b1 = getBytesFromFile(f1);
		final byte[] b2 = getBytesFromFile(f2);

		int shortest = b1.length;
		if (b2.length < shortest)
			shortest = b2.length;
		for (int i = 0; i < shortest; i++) {
			if (b1[i] != b2[i])
				return i;
		}
		return -1;
	}

	/**
	 * Returns the contents of the file in a byte array.
	 *
	 * Taken from (Shamelessly stolen from)
	 * <a href="http://www.exampledepot.com/egs/java.io/file2bytearray.html"> http://www.exampledepot.com/egs/java.io/
	 * file2bytearray.html</a>
	 */
	private static byte[] getBytesFromFile(final File file) throws IOException {
		final InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		/*
		 * You cannot create an array using a long type. It needs to be an int type. Before converting to an int type,
		 * check to ensure that file is not larger than Integer.MAX_VALUE.
		 */
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			is.close(); // close the stream before throwing exception.
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	/**
	 * Utility method to generate the MD5 Checksum of given file.
	 *
	 * @param file
	 *            The file for which you want to get checksum of.
	 * @return MD5 Checksum of {@code file} in HEX format.
	 */
	public static String getChecksum(final File file) {
		return fileExists(file) ? getChecksum(file, "MD5") : null;
	}

	/**
	 * Utility method to generate the Checksum of a file.
	 *
	 * @param file
	 *            The file for which you want to get checksum of.
	 * @param algo
	 *            checksum algorithm either MD5 or SHA1
	 * @return Checksum of {@code file} in HEX format.
	 */
	public static final String getChecksum(final File file, final String algo) {
		MessageDigest md = null;
		StringBuilder sb = new StringBuilder("");

		if (fileExists(file)) {
			try (FileInputStream fis = new FileInputStream(file)) {
				if (!StringUtils.isNullOrEmpty(algo)
						&& (algo.equalsIgnoreCase("MD5") || algo.equalsIgnoreCase("SHA1"))) {
					md = MessageDigest.getInstance(algo);
				} else {
					md = MessageDigest.getInstance("MD5");
				}

				if (md != null) {
					byte[] dataBytes = new byte[2048];
					int read = -1;

					while ((read = fis.read(dataBytes)) != -1) {
						md.update(dataBytes, 0, read);
					}

					byte[] data = md.digest();
					sb.append(md.getAlgorithm()).append(": ");

					for (int i : data) {
						sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
					}
				}
			} catch (IOException | NoSuchAlgorithmException e) {
				logger.log(Level.SEVERE, "Unable to generate checksum of given file.", e);
			}
		} else {
			throw new NullPointerException("File does not exist.");
		}

		return sb.toString().toUpperCase();
	}

	/**
	 * Gets content of a file
	 *
	 * @param file
	 *            file to read
	 * @return content of file
	 * @throws IOException
	 *             if any IO Exception occurred.
	 */
	public static String getFileContent(final File file) throws IOException {
		StringBuilder sb = new StringBuilder(32);

		if (fileExists(file)) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line;

				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				throw e;
			}
		}
		return sb.toString();
	}

	public static String getValueByKey(String key, String lang) {
		String value = "";
		String fileName = "";

		switch (lang) {
		case "English":
			fileName = "en-US.properties";
			break;
		case "French":
			fileName = "fr-FR.properties";
			break;
		default:
			fileName = "hi-IN.properties";
			break;
		}

		File file = new File("public/app/i18n", fileName);

		if (fileExists(file)) {
			try (FileInputStream fis = new FileInputStream(file)) {
				Properties property = new Properties();
				property.load(fis);
				value = property.getProperty(key);
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Unable to load locale file for language: " + lang, ex);
			}
		}

		return value;
	}

	/**
	 * @param file
	 *            File to be read.
	 * @return content of the file as String.
	 * @throws IOException
	 *             if any IO Exception arises.
	 */
	public static String readFile(final File file) throws IOException {
		StringBuilder sb = new StringBuilder("");

		if (fileExists(file)) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;

				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} catch (IOException e) {
				throw new IOException("ERROR: Unable to read file.", e);
			}
		}

		return sb.toString();
	}

	/**
	 * @param fileName
	 *            Name of the file to be read.
	 * @return content of file as String
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException {
		return !StringUtils.isNullOrEmpty(fileName) ? readFile(new File(fileName)) : "";
	}

	public static void writeFile(String content, final File file) throws IOException {
		if (fileExists(file)) {
			status = file.getParentFile().mkdirs();
			status = status || file.createNewFile();
		}

		logger.info(status ? "File created for writing." : "Unable to create the given file.");

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.write(content);
			bw.flush();
		}
	}

	public static void writeFile(String content, String fileName) throws IOException {
		if (!StringUtils.isNullOrEmpty(content) && fileExists(fileName)) {
			writeFile(content, new File(fileName));
		}
	}

	private FileUtils() {
		super();
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode());
	}
}
