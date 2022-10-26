package com.websecuritylab.tools.headers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public final class FileHandler {

	public static List<String> getFilenamesInFolder(boolean stripExt, String folderName, String extension){
		//File folder = new File("/Users/you/folder/");
		File folder = new File(folderName);		
		File[] listOfFiles = folder.listFiles();
		List<String> listOfFilenames = new ArrayList<>();

		for (File file : listOfFiles) {
		    if (file.isFile() && extension.equals(FilenameUtils.getExtension(file.getName()))) {
		    	//System.out.println("Adding file with extension: " + FilenameUtils.getExtension(file.getName()));
		    	String name = file.getName();
		    	listOfFilenames.add(name.substring(0,name.length()-extension.length()-1));
		    }
		}
		return listOfFilenames;
	}
	
	//
	// TODO: Complete this method
	//
	public static void writeFile(String path) {
		String cleanFilePath = path.replaceAll("[^a-zA-Z0-9.-]", "_");
	}

}
