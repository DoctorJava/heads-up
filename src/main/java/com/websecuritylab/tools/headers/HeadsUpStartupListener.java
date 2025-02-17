package com.websecuritylab.tools.headers;

import jakarta.servlet.ServletContextEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.websecuritylab.tools.headers.PropsManager.PropName;
import com.websecuritylab.tools.headers.servlets.CheckHeadersServlet;
import com.websecuritylab.tools.headers.util.FileHandler;

public class HeadsUpStartupListener implements jakarta.servlet.ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger( HeadsUpStartupListener.class );  
	
    private static final String HEADSUP_FOLDER = new PropsManager().getProperty(PropName.HEADSUP_FOLDER);

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("Initializing HeadsUp webapp.");
		if (FileHandler.folderExists(HEADSUP_FOLDER)) {
			logger.info("Reading policies from: " + HEADSUP_FOLDER);			
		} else {
			logger.info("Creating HeadsUp policy foloder: " + HEADSUP_FOLDER);			
		}
	}

}
