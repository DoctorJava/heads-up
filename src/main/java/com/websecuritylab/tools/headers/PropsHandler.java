package com.websecuritylab.tools.headers;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropsHandler {
	private static Logger logger = LoggerFactory.getLogger(PropsHandler.class);

	private static final String PROPS_FILE = "headsUp.properties";

	public static enum PropName {
		POLICY_DIR
	}

	private static HashMap<PropName, String> _props = new HashMap<PropName, String>();

	private static final PropsHandler _instance = new PropsHandler();

	private PropsHandler() {
		try {
			loadProperties();
		} catch (FileNotFoundException e) {
			logger.error(">>>>>>>>>>Fatal Error: Can't Find Property File in ClassPath" + e);
		} catch (Exception e) {
			logger.error(">>>>>>>>>>Fatal Error: Exception loading Property File" + e);
		}

		System.out.println("In PropsHandler Got POLICY_DIR: " + PropsHandler.getProperty(PropName.POLICY_DIR));
	}

	private void loadProperties() throws Exception {
		Properties props = new Properties();

		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(PROPS_FILE);) {
			if (is == null)
				throw new FileNotFoundException(PROPS_FILE);
			props.load(is);
		}

		for (PropName pName : PropName.values())
			_props.put(pName, props.getProperty(pName.toString()));
	}

	// Usage: PropsHandler.getInstance().getProperty(PropName.POLICY_DIR))
	// PropsHandler.getProperty(SSOProperties.PropName.POLICY_DIR)
	public static String getProperty(PropName pName) {
		return _props.get(pName);
	}
}