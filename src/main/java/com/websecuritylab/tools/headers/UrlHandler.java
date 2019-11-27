package com.websecuritylab.tools.headers;

import java.io.*; 
import java.util.*;
import java.util.stream.Collectors;


import com.websecuritylab.tools.headers.exceptions.InvalidUrlException;
import com.websecuritylab.tools.headers.exceptions.SiteNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.validator.routines.UrlValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UrlHandler {
    private static final Logger logger = LoggerFactory.getLogger( UrlHandler.class );  
	private URL _url;

	public UrlHandler(String urlStr) throws InvalidUrlException, MalformedURLException {
        boolean valid = validateUrl(urlStr);

        if (!valid)  throw new InvalidUrlException ();

       _url = new URL(urlStr);
    }


	public Map<String, List<String>> getHeaderMap() throws MalformedURLException, SiteNotFoundException{
		try {
			URLConnection conn = _url.openConnection();
			//get all headers
			Map<String, List<String>> inMap = conn.getHeaderFields();
			Map<String, List<String>> outMap = new HashMap<>();
			for (Map.Entry<String, List<String>> entry : inMap.entrySet()) {
				for ( String valWithSemis : entry.getValue()) {
					List<String> strList = new ArrayList<>();
					if (valWithSemis.contains(";")) {					// Splits strings with ;
						for (String s : valWithSemis.split(";"))
						{
						    strList.add(s.trim());
						}
					}
					else if (valWithSemis.contains(",")) {				// OR Splits strings with , but not both
						for (String s : valWithSemis.split(","))
						{
						    strList.add(s.trim());
						}
					}
					else strList = entry.getValue();
					
					outMap.put(entry.getKey(), strList);
				}
			}
			
			
			
			return outMap;
		} catch (IOException e) {
			throw new SiteNotFoundException(e);
		}
	}
	
	private List<String> parseTokens(String str, String token){
		List<String> strList = new ArrayList<>();
		if (str.contains(token)) {
			for (String s : str.split(token))
			{
			    strList.add(s.trim());
			}
		}
		else strList.add(str);
		
		return strList;
	}
	
	public String getWebPageContent() {
		String content;
		try (InputStream is = _url.openStream();
			 BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) 
		{
			content = br.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException ex) {
			content = String.format("Cannot read webpage %s", ex);
			logger.error(null, ex);
		}

		return content;
	}

	private boolean validateUrl(String urlStr) {

		UrlValidator urlValidator = new UrlValidator();

		return urlValidator.isValid(urlStr);
	}
	
	//
//	public String getRawHeaders() throws MalformedURLException, SiteNotFoundException{
//		StringBuffer sb = new StringBuffer();
//		try {
//			URLConnection conn = _url.openConnection();
//			for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
//			    System.out.println("Got Entry: " +entry.toString());
//			    //sb.append(entry.toString().replaceFirst("=",": ") + "\n");				// Only name: value needs replaced.  The value may have multiple items like charset=UTF-8
//			    sb.append(entry.toString() + "\n");				
//			}
//			return sb.toString();
//		} catch (IOException e) {
//			throw new SiteNotFoundException(e);
//		}
//	}	
	//
	// Static Methods
	//
	
	public static CaseInsensitiveMap<String, List<String>> generateHeaderMap(String rawHeaders) {
		//HashMap<String, List<String>> headerMap = new HashMap<>();
		CaseInsensitiveMap<String, List<String>> headerMap = new CaseInsensitiveMap<>();
		String[] lines = rawHeaders.split("\\r?\\n");
		for (String line : lines) {
			int i = line.indexOf(":");
			if (i < 0) continue;
			//List<String> values = Arrays.asList(line.substring(i + 1).split(";"));
			List<String> values = Arrays.asList(line.replace(";",",").substring(i + 1).split(","));			// Some values are ; separated, but other are comma .  Both are treated the same for this tester
																											// Content-Type	text/html; charset=UTF-8
																											// Cache-Control: no-cache, no-store, max-age=0, must-revalidate
			headerMap.put(line.substring(0, i), values);
		}
		return headerMap;
	}
	
	//
	// List<String> is created from semis or commas.  The reconstruction uses commas for both as default for List.toString()
	// 
	// Cache-Control: no-cache, no-store
	// Content-Type: text/html;charset=UTF-8
	// X-XSS-Protection: 1; mode=block
	//
	public static String generateRawHeaders(Map<String, List<String>> headerMap) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, List<String>> entry : headerMap.entrySet()) {
			System.out.println(entry.toString());
			//sb.append(entry.toString().replace("=", ": ").replace("[", "").replace("]", "") + "\n");			// Default MapEntry to String has = and brackets [...]
			sb.append(entry.toString().replaceFirst("=", ": ").replace("[", "").replace("]", "") + "\n");		// Only name: value needs replaced.  The value may have multiple items like charset=UTF-8
		}
		return sb.toString();
	}
}
