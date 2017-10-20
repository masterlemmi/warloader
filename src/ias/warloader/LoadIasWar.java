package ias.warloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoadIasWar {

	Logger logger = Logger.getLogger("LoadIasWar");
	FileHandler fh;
	InputStream input = null;
	String iasLoc, middlewareLoc, iasBin, middlewareBin, logLoc;
	String ias = "ias.war";
	String mid = "ias_middleware.war";
	File iasWebAppsWarPath;
	File middlewareWebAppsWarPath;

	public static void main(String[] args) {
		new LoadIasWar();
	}

	public LoadIasWar() {
		init();

		try {
			if (warFilesExist()) {
				deleteOldWarFiles();
				deleteOldFolders();
				moveWarFiles();
			}
		} catch (Exception e) {
			logger.warning("failed to complete loading War Files  "+  e.getMessage());
		} 
		logger.info("END");
	}

	private void moveWarFiles() {
		logger.info("moving ias to webapps... ");
		File oldIas = new File(ias);
		oldIas.renameTo(iasWebAppsWarPath);
		oldIas.delete();
		logger.info("ias.war file moved... deleting old ias");

		File oldMid = new File(mid);
		oldMid.renameTo(middlewareWebAppsWarPath);
		oldMid.delete();
		logger.info("middleware.war file moved. deleting old ias_middlware");

	}
	
	private void deleteOldFolders() {
		deleteOldFolders(new File(iasLoc + "ias"));
		deleteOldFolders (new File (middlewareLoc + "ias_middleware"));
	}

	private void deleteOldFolders(File folder) {
		logger.info("deleting contents in " + folder);
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteOldFolders(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
		logger.info("deleted all contents in " + folder);
	}

	private void deleteOldWarFiles() throws IOException {
		try {
			Files.deleteIfExists(iasWebAppsWarPath.toPath());
			Files.deleteIfExists(middlewareWebAppsWarPath.toPath());
		} catch (IOException e) {
			logger.info("problem deleting old war files");
			logger.info(e.getMessage());
			throw new IOException();
		}
		logger.info("old war files deleted");

	}

	private boolean warFilesExist() throws FileNotFoundException {
		File iasf = new File(ias);
		File midf = new File(mid);
		boolean exists = iasf.exists() && midf.exists();
		if(!exists) throw new FileNotFoundException("Missing war files in home dir");
		logger.info("war files detected: " + exists);
		return exists;

	}

	private void init() {

		Properties prop = new Properties();
		try {
			input = new FileInputStream("warloader.properties");
			prop.load(input);
			iasLoc = prop.getProperty("ias.webapps");
			middlewareLoc = prop.getProperty("middleware.webapps");
			iasBin = prop.getProperty("ias.bin");
			middlewareBin = prop.getProperty("middleware.bin");
			logLoc = prop.getProperty("logLoc");

			fh = new FileHandler("warloader.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			iasWebAppsWarPath = new File(iasLoc + ias);
			middlewareWebAppsWarPath = new File(middlewareLoc + mid);

		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(0);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}