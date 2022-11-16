package com.websecuritylab.tools.headers;

import java.io.*; 
import java.util.*;
import java.util.stream.Collectors;

import com.websecuritylab.tools.headers.exceptions.InvalidUrlException;
import com.websecuritylab.tools.headers.exceptions.SiteNotFoundException;
import com.websecuritylab.tools.headers.model.Cookie;
import com.websecuritylab.tools.headers.model.Policy;
import com.websecuritylab.tools.headers.model.Rule;
import com.websecuritylab.tools.headers.model.Rule.CONTAINS_TYPE;
import com.websecuritylab.tools.headers.util.PolicyHandler;
import com.websecuritylab.tools.headers.util.PolicyHandler.COOKIE_RULE;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.validator.routines.UrlValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UrlManager {					// This is not a static Handler because it opens Connection which may cause thread problems

	private static final Logger logger = LoggerFactory.getLogger( UrlManager.class );  
	private URL _url;

	public UrlManager(String urlStr) throws InvalidUrlException, MalformedURLException {
        boolean valid = validateUrl(urlStr);

        if (!valid)  throw new InvalidUrlException ();

       _url = new URL(urlStr);
    }


	public Map<String, List<String>> getHeaderMap() throws MalformedURLException, SiteNotFoundException{
       
		setAllTrustingSSL();			// This is required for handling self signed certs.  Heapp is not designed to test SSL compliance.

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
			throw new SiteNotFoundException(e);				// ConnectException is thrown for 404
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

	  private void setAllTrustingSSL() {
	        // Create a trust manager that does not validate certificate chains
	              TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	                     public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                           return null;
	                     }
	 
	                     public void checkClientTrusted(X509Certificate[] certs, String authType) {
	                     }
	 
	                     public void checkServerTrusted(X509Certificate[] certs, String authType) {
	                     }
	              } };
	              try {
	              // Install the all-trusting trust manager
	                     SSLContext sc = SSLContext.getInstance("SSL");
	                     sc.init(null, trustAllCerts, new java.security.SecureRandom());
	               HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	              } catch (NoSuchAlgorithmException e1) {
	                     // TODO Auto-generated catch block
	                     e1.printStackTrace();
	              }
	              catch (KeyManagementException e1) {
	                     // TODO Auto-generated catch block
	                     e1.printStackTrace();
	              }
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);          
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
			List<String> gotHeaderValues = Arrays.asList(line.replace(";",",").substring(i + 1).split(","));			// Some values are ; separated, but other are comma .  Both are treated the same for this tester
																											// Content-Type	text/html; charset=UTF-8
																											// Cache-Control: no-cache, no-store, max-age=0, must-revalidate
			String headerName = line.substring(0, i);
			
			if ( "Set-Cookie".equals(headerName) ) continue;				// The HeaderMap doesn't include cookies
			
			
																	// Multiple lines with same header name are sometimes valid
																	// https://stackoverflow.com/questions/48012994/is-it-fine-to-use-duplicate-response-header-with-same-value
							
			if ( headerMap.get(headerName) != null) {				// Already have some headers with this name.  This often occurs with "Set-Cookie"
				headerMap.put(headerName, ListUtils.union(gotHeaderValues, headerMap.get(headerName)));					
			}
			else {
				headerMap.put(headerName, gotHeaderValues );					
			}
			
		}
		return headerMap;
	}
	
	//
	// Note that Policy rule is enforced when examining each cookie
	// Whereas the other rules are enforced by looking through the Rules to see if a match is found
	//
	
	public static List<Cookie> generateCookies(String rawHeaders, Policy policy) {
		//HashMap<String, List<String>> headerMap = new HashMap<>();
		//CaseInsensitiveMap<String, List<String>> cookieMap = new CaseInsensitiveMap<>();
		List<String> cookieNames = new ArrayList<>();
		List<Cookie> cookies = new ArrayList<>();
		String[] lines = rawHeaders.split("\\r?\\n");
		for (String line : lines) {
			if ( line.length() <= 0 ) continue;
			line = line.replace(",",";");		//// Sometimes Cookie directives are ; separated, but other are comma.  Both are treated the same for this tester
			int iColon = line.indexOf(":");
			int iEquals = line.indexOf("=");
			
			if ( !";".equals(line.substring(line.length() - 1))) line += ";";   // Add a semicolon to the end-of-line if it isn't present
			int iSemi = line.indexOf(";");										// There is always at least one ; because of the line above
			if (iColon < 0 || iEquals < 0) continue;

			
			//List<String> values = Arrays.asList(line.substring(i + 1).split(";"));
			//List<String> gotHeaderValues = Arrays.asList(line.replace(";",",").substring(iColon + 1).split(","));			// Some values are ; separated, but other are comma .  Both are treated the same for this tester
																											// Content-Type	text/html; charset=UTF-8
																											// Cache-Control: no-cache, no-store, max-age=0, must-revalidate
			String headerName = line.substring(0, iColon);
			
			if ( !"Set-Cookie".equals(headerName)  ) continue;				// THis method handles the multiple "Set-Cookie" headers
			
			String cookieName = line.substring(iColon+1, iEquals).trim();
			String cookieValue = line.substring(iEquals+1, iSemi).trim();
			String directStr= line.substring(iSemi+1).trim();
			//System.out.println("Got Cookie: " + cookieName );
			
																	// Duplicate cookies with the sane name are not allowed

			List<String> directives = Arrays.asList(directStr.split(";"));			// Values are separated by comma

			System.out.println("Got Cookie Directive String: " + directStr + " with directives List: "  + directives);

			Cookie cookie = new Cookie ( cookieName, cookieValue, directives);	
			
			if ( cookie.isSession() ) {
				cookie.setCompliant(true);			// Session cookies will have COMPLIANT set true/false.  Other cookies are NULL
				if ( ! lineContains(line,"HttpOnly", policy.isCaseSensitiveValues())) {
					System.out.println("Didn't find 'HttpOnly' in the values: " + directives);
					cookie.setCompliant(false);
				}
				if ( ! lineContains(line,"secure", policy.isCaseSensitiveValues())) {
					System.out.println("Didn't find 'secure' in the values: " + directives);
					cookie.setCompliant(false);			
				}
				if (cookie.isCompliant() == false ) cookie.addRules(PolicyHandler.getRule(COOKIE_RULE.SESSION));   // Set for any of the above SESSION rules
			}			
			if ( cookieNames.contains(cookieName)) {
				cookie.setDuplicate(true);						// Also sets COMPLIANT=false
				cookie.addRules(PolicyHandler.getRule(COOKIE_RULE.NO_DUPLICATES));
			}
				
			cookieNames.add(cookieName);			// THis local LIST is used to identify duplicate cookie names
			cookies.add(cookie);					
			

		}
		return cookies;
	}
	
	private static boolean lineContains( String line, String directive, boolean caseSensitive) {
		if ( caseSensitive ) return line.contains(directive);
		else return line.toLowerCase().contains(directive.toLowerCase());
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

	public static String getURL(HttpServletRequest req) {

	    String scheme = req.getScheme();             // http
	    String serverName = req.getServerName();     // hostname.com
	    int serverPort = req.getServerPort();        // 80
	    String contextPath = req.getContextPath();   // /mywebapp
	    String servletPath = req.getServletPath();   // /servlet/MyServlet
	    String pathInfo = req.getPathInfo();         // /a/b;c=123
	    String queryString = req.getQueryString();          // d=789

	    // Reconstruct original requesting URL
	    StringBuilder url = new StringBuilder();
	    url.append(scheme).append("://").append(serverName);

	    if (serverPort != 80 && serverPort != 443) {
	        url.append(":").append(serverPort);
	    }

	    url.append(contextPath).append(servletPath);

	    if (pathInfo != null) {
	        url.append(pathInfo);
	    }
	    if (queryString != null) {
	        url.append("?").append(queryString);
	    }
	    return url.toString();
	}
  
}
