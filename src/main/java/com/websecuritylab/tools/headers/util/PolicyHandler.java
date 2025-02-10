package com.websecuritylab.tools.headers.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.websecuritylab.tools.headers.PropsManager;
import com.websecuritylab.tools.headers.PropsManager.PropName;
import com.websecuritylab.tools.headers.constants.DoPostParams;
import com.websecuritylab.tools.headers.model.Policy;
import com.websecuritylab.tools.headers.model.Reference;
import com.websecuritylab.tools.headers.model.Rule;
import com.websecuritylab.tools.headers.model.Rule.CONTAINS_TYPE;
import com.websecuritylab.tools.headers.servlets.MaintainRulesServlet;

public class PolicyHandler {
	private static final PolicyHandler _instance = new PolicyHandler();			// This static class is not instantiated externally

	private static final Logger logger = LoggerFactory.getLogger( PolicyHandler.class );  
    
	private static final String HEADSUP_FOLDER = new PropsManager().getProperty(PropName.HEADSUP_FOLDER);
	
	public static final String POLICY_EXT  = "json";     
	
	public static List<String> HEADER_NAMES = Arrays.asList(new String[]{"Content-Type","Cache-Control", "strict-transport-security", "x-frame-options", "X-XSS-Protection", "x-content-type-options","Referrer-Policy"});    
    
    public enum COOKIE_RULE{SESSION, NO_DUPLICATES} 
    
	public PolicyHandler() {					// Create HeadsUp data folders, if they don't exist (Ex: /op/apps/headsUp/policy )
//		logger.info("Checking the HeadsUp Policy folder specified in headsUp.properties:");
//		File file = new File(HEADSUP_FOLDER); 
//		try {
//			FileUtils.forceMkdir(file);
//		} catch (IOException e) {
//										// Not sure why this would fail, or what to do about it
//			logger.error("Error creating the HeadsUp Policy folder specified in headsUp.properties:" + HEADSUP_FOLDER);
//			e.printStackTrace();
//		}

	}

	public static Rule getRule (COOKIE_RULE cr) {	
		switch (cr) {
			case SESSION:
				Rule sessionRule = new Rule("Session", false, Arrays.asList("HttpOnly","secure") , CONTAINS_TYPE.ALL);
		        List<Reference> refSessionCookie = new ArrayList<>();       
//		        refSessionCookie.add(new Reference("OWASP","https://www.owasp.org/index.php/HttpOnly"));
//		        refSessionCookie.add(new Reference("BurpSuite","https://portswigger.net/kb/issues/00500600_cookie-without-httponly-flag-set"));
		        sessionRule.setReferences(refSessionCookie);
		        return sessionRule;

			case NO_DUPLICATES:
				Rule dupRule = new Rule("Session", false, Arrays.asList("No Duplicates") , CONTAINS_TYPE.NONE);
		        List<Reference> refNoDups = new ArrayList<>();       
//		        refNoDups.add(new Reference("BurpSuite","https://portswigger.net/kb/issues/00400a00_duplicate-cookies-set"));
		        dupRule.setReferences(refNoDups);
		        return dupRule;
		        				
		}
        return null;
       
	}
	
	public static List<String> getPolicyNames(){
		return FileHandler.getFilenamesInFolder(true,HEADSUP_FOLDER,POLICY_EXT);
	}
	
	
	public static Policy getPolicy(String policyName) {
		if (Policy.DEFAULT.equals(policyName)) return getDefaultPolicy();
		String filename = HEADSUP_FOLDER + policyName + "." + POLICY_EXT;
		Policy policy = null;	
		try {
			logger.info("Reading policy from file: " + filename);
			policy = PolicyHandler.readPolicy(filename);
		} catch (Exception e) {
			policy = PolicyHandler.getDefaultPolicy();							// File not found
		}
		if (policy == null ) policy = PolicyHandler.getDefaultPolicy();			// File is empty
		return policy;
	}

	private static Policy readPolicy(String filename) throws FileNotFoundException {
		//System.out.println("Reading policy from file:" + filename);
		logger.info("Reading policy from file:" + filename);
		Gson gson = new Gson();
		Policy policy = null;
		try (Reader reader = new FileReader(filename)) {
    		policy = gson.fromJson(reader, Policy.class);
        } catch (IOException e) {
            e.printStackTrace();
        }      
		return policy;
	}
	
	public static void writePolicy(Policy policy, String filename) throws IOException {
		//Gson gson = new Gson();
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		
//		FileWriter writer = new FileWriter(filename);
//		System.out.println("Writing JSON:" + writer);
//		gson.toJson(policy, writer);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
        	System.out.println("Writing JSON File:" + filename);
            gson.toJson(policy, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static Policy buildPolicy(HttpServletRequest req) {
		String newPolicyName = req.getParameter(DoPostParams.NEW_POLICY_NAME);
		List<Rule> rules = new ArrayList<>();
		
		for (String header: HEADER_NAMES) {
			boolean required = "on".equals(req.getParameter(header+"_required"));								// HTML Browser returns "on" for checked boxes
			CONTAINS_TYPE containsType = CONTAINS_TYPE.valueOf(req.getParameter(header+"_containsType"));
			List<String> containsValues = Arrays.asList(req.getParameterValues(header+"_containsValue"));
			
			//System.out.println("Got ("+header+"_containsType) rule containsType: "+ containsType );
			//System.out.println("Got ("+header+"_containsValue) rule containsValue: "+containsValues.size() );
			Rule rule = new Rule(header, required, containsValues, containsType);
			rules.add(rule);
		}
		return ( new Policy (newPolicyName, rules ));
	}
	
	//
	//Private Methods
	//
	private static Policy getDefaultPolicy() {
		
		
		List<Rule> rules = new ArrayList<>();
		
		//
		// New rules can use this code template
		//	
//		List<String> contains_VALUES = Arrays.asList("Val 1","Val 2");
//		Rule ruleNAME = new Rule("<HEADER_NAME>", contains_VALUES, CONTAINS_TYPE.<ALL, ANY, ONLY, NONE>);
//		List<Reference> refNAME = new ArrayList<>();
//		refNAME.add(new Reference("",""));
//		refNAME.add(new Reference("",""));
//		ruleNAME.setReferences(refNAME);
//      rules.add(ruleNAME);
        

		//List<String> containsOnly_Content = Arrays.asList( "text/html; charset=UTF-8");
        //rules.add(new Rule("Content-Type", containsOnly_Content, CONTAINS_TYPE.ONLY));
 		List<String> containsAll_Content = Arrays.asList( "text/html", "charset=UTF-8");
		Rule ruleContentType = new Rule("Content-Type", true, containsAll_Content, CONTAINS_TYPE.ALL);
//        List<Reference> refContentType = new ArrayList<>();       
//        refContentType.add(new Reference("OWASP","https://www.owasp.org/index.php/OWASP_Testing_Guide_Appendix_D:_Encoded_Injection"));
//        refContentType.add(new Reference("w3.org","https://www.w3.org/International/articles/http-charset/index"));
//        ruleContentType.setReferences(refContentType);
        rules.add(ruleContentType);
       
		//String containsExact_Cache = "no-store";
        //rules.add(new Rule("Cache-Control", containsExact_Cache)); 
		List<String> contains_Cache = Arrays.asList("no-store");
		Rule ruleCache = new Rule("Cache-Control", true, contains_Cache, CONTAINS_TYPE.ONLY);
//		List<Reference> refCache = new ArrayList<>();
//		refCache.add(new Reference("HyperText Transfer Protocol: A Short Course","https://condor.depaul.edu/dmumaugh/readings/handouts/SE435/HTTP/node24.html"));
//		refCache.add(new Reference("Mozilla MDN","https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cache-Control"));
//		ruleCache.setReferences(refCache);
        rules.add(ruleCache);
        
        //Rule ruleHSTS = new Rule("strict-transport-security", true);
		List<String> contains_Hsts = Arrays.asList("max-age=31536000","includeSubDomains");
		Rule ruleHSTS = new Rule("strict-transport-security", true, contains_Hsts, CONTAINS_TYPE.ALL);

//		List<Reference> refHSTS = new ArrayList<>();
//		refHSTS.add(new Reference("OWASP HSTS Cheatsheet","https://cheatsheetseries.owasp.org/cheatsheets/HTTP_Strict_Transport_Security_Cheat_Sheet.html"));		
//		refHSTS.add(new Reference("Wikipedia: HSTS","https://en.wikipedia.org/wiki/HTTP_Strict_Transport_Security"));
//		ruleHSTS.setReferences(refHSTS);
        rules.add(ruleHSTS);
        
		List<String> containsAny_Frame = Arrays.asList("SAMEORIGIN", "DENY");
		Rule ruleXframe = new Rule("x-frame-options" , true, containsAny_Frame, CONTAINS_TYPE.ANY);
//        List<Reference> refXframe = new ArrayList<>();       
//        refXframe.add(new Reference("OWASP Clickjacking","https://www.owasp.org/index.php/Clickjacking"));
//        refXframe.add(new Reference("KeyCDN Blog","https://www.keycdn.com/blog/x-frame-options"));
//        ruleXframe.setReferences(refXframe);
        rules.add(ruleXframe);
        
        List<String> containsAny_Xss = Arrays.asList("1");
        Rule ruleXss = new Rule("X-XSS-Protection", true, containsAny_Xss, CONTAINS_TYPE.ONLY);
//        List<Reference> refXss = new ArrayList<>();
//        refXss.add(new Reference("Mozilla MDN","https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection"));
//        refXss.add(new Reference("KeyCDN","https://www.keycdn.com/blog/x-xss-protection"));
//        ruleXss.setReferences(refXss);
        rules.add(ruleXss);
        
		List<String> contains_Options = Arrays.asList("nosniff");
        Rule ruleNosniff = new Rule("x-content-type-options", true, contains_Options, CONTAINS_TYPE.ONLY);
//   	    List<Reference> refNosniff = new ArrayList<>();
//        refNosniff.add(new Reference("Helmet Nosniff","https://helmetjs.github.io/docs/dont-sniff-mimetype/"));
//        refNosniff.add(new Reference("Denim Group Blog","https://www.denimgroup.com/resources/blog/2019/05/mime-sniffing-in-browsers-and-the-security-implications/"));
//        ruleNosniff.setReferences(refNosniff);
        rules.add(ruleNosniff); 
        
		List<String> contains_referrer = Arrays.asList("no-referrer");
		Rule ruleReferrer = new Rule("Referrer-Policy", true, contains_referrer, CONTAINS_TYPE.ONLY);
//		List<Reference> refReferrer = new ArrayList<>();		
//		refReferrer.add(new Reference("OWASP Referrer-Policy","https://owasp.org/www-project-secure-headers/#referrer-policy"));
//		refReferrer.add(new Reference("Scott Helme: A new security header: Referrer Policy","https://scotthelme.co.uk/a-new-security-header-referrer-policy/"));		
//		ruleReferrer.setReferences(refReferrer);
        rules.add(ruleReferrer);
         
        //return new Policy("Default Policy", rules);
        return new Policy(Policy.DEFAULT, rules);
	}

}
