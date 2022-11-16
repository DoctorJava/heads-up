package com.websecuritylab.tools.headers;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropsManager {
	//private static final PropsManager _instance = new PropsManager();					// This static class is not instantiated externally
	
	private static Logger logger = LoggerFactory.getLogger(PropsManager.class);

	private final String PROPS_FILE = "headsUp.properties";

	public enum PropName {
		POLICY_FOLDER,
		REFERENCES_FILE
	}

	private static HashMap<PropName, String> _props = new HashMap<PropName, String>();


	//
	// Constructor
	//
	public PropsManager() {
		System.out.println("In PropsHandler Got POLICY_FOLDER: " + PROPS_FILE);
		try {
			loadProperties();
		} catch (FileNotFoundException e) {
			logger.error(">>>>>>>>>>Fatal Error: Can't Find Property File in ClassPath" + e);
		} catch (Exception e) {
			logger.error(">>>>>>>>>>Fatal Error: Exception loading Property File" + e);
		}

	}

	private void loadProperties() throws Exception {
		Properties props = new Properties();

		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(PROPS_FILE);) {
			if (is == null)
				throw new FileNotFoundException(PROPS_FILE);
			props.load(is);
		}
		System.out.println("Loading props ("+props+") from file with value: " + PROPS_FILE);
		//logger.info("Loading propertyfrom file with value: " + PROPS_FILE);
		System.out.println("But propName: " + PropName.values());

		for (PropName pName : PropName.values()) {
			System.out.println("Loading property: " + pName);
			String aProp = props.getProperty(pName.toString()).trim();				// Remove leading and trailing spaces
			logger.info("Loading property ("+pName+") with value: " + aProp);
			_props.put(pName, aProp);		
		}
	}

	// Usage: PropsHandler.getInstance().getProperty(PropName.POLICY_FOLDER))
	// PropsHandler.getProperty(SSOProperties.PropName.POLICY_FOLDER)
	public String getProperty(PropName pName) {
		return _props.get(pName);
	}
}