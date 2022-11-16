package com.websecuritylab.tools.headers.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class FileHandler {
	private static final FileHandler _instance = new FileHandler();					// This static class is not instantiated externally

	private static final Logger logger = LoggerFactory.getLogger( FileHandler.class );  

										// Accept folderNamws with, or without, '/' 
										// Accept extensions with, or without, '.'
	public static List<String> getFilenamesInFolder(boolean stripExt, String folderName, String ext){
		//File folder = new File("/Users/you/folder/");
		if (!folderName.endsWith("/")) folderName += "/";
		if (ext.startsWith(".")) ext = ext.substring(1);
		
		File folder = new File(folderName);		
		File[] listOfFiles = folder.listFiles();
		List<String> listOfFilenames = new ArrayList<>();
		for (File file : listOfFiles) {
		    if (file.isFile() && ext.equals(FilenameUtils.getExtension(file.getName()))) {
		    	//System.out.println("Adding file with extension: " + FilenameUtils.getExtension(file.getName()));
		    	String name = file.getName();
		    	listOfFilenames.add(name.substring(0,name.length()-ext.length()-1));
		    }
		}
		return listOfFilenames;
	}
	
									// Accept folderNamws with, or without, '/' 
									// Accept extensions with, or without, '.'
	public static String writeFileLines(String folderName, String filename, String ext, List<String> lines) throws IOException {
		if (!folderName.endsWith("/")) folderName += "/";
		if (!ext.startsWith(".")) ext = "." + ext;
		String cleanFilename = filename.replaceAll("[^a-zA-Z0-9.-]", "_");
		String path = folderName + cleanFilename + ext;
    	logger.info("Creating file: " + path);
        
        File myfile = new File(path);
       
        FileUtils.writeLines(myfile, StandardCharsets.UTF_8.name(), lines);
        
        return path;
	}

}
