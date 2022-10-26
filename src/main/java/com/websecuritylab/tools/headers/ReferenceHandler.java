package com.websecuritylab.tools.headers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.websecuritylab.tools.headers.model.Policy;
import com.websecuritylab.tools.headers.model.Reference;
import com.websecuritylab.tools.headers.model.Rule;
import com.websecuritylab.tools.headers.model.Rule.CONTAINS_TYPE;
import com.websecuritylab.tools.headers.servlets.MaintainRulesServlet;

public class ReferenceHandler {
    private static final Logger logger = LoggerFactory.getLogger( ReferenceHandler.class );  
    

	
	public static Map<String,List<Reference>> createDefaultReferences() {
				
		Map<String,List<Reference>> referenceMap = new HashMap<>();
		List<Reference> refs; 
		
        refs = new ArrayList<>();       
        refs.add(new Reference("OWASP","https://www.owasp.org/index.php/OWASP_Testing_Guide_Appendix_D:_Encoded_Injection"));
        refs.add(new Reference("w3.org","https://www.w3.org/International/articles/http-charset/index"));
        referenceMap.put(Rule.HEADER_CONTENT_TYPE,refs);
       
        
		refs = new ArrayList<>();
		refs.add(new Reference("HyperText Transfer Protocol: A Short Course","https://condor.depaul.edu/dmumaugh/readings/handouts/SE435/HTTP/node24.html"));
		refs.add(new Reference("Mozilla MDN","https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cache-Control"));
        referenceMap.put(Rule.HEADER_CACHE_CONTROL,refs);
        
		refs = new ArrayList<>();
		refs.add(new Reference("OWASP HSTS Cheatsheet","https://cheatsheetseries.owasp.org/cheatsheets/HTTP_Strict_Transport_Security_Cheat_Sheet.html"));		
		refs.add(new Reference("Wikipedia: HSTS","https://en.wikipedia.org/wiki/HTTP_Strict_Transport_Security"));
	    referenceMap.put(Rule.HEADER_HSTS,refs);

		refs = new ArrayList<>();
		refs.add(new Reference("OWASP Clickjacking","https://www.owasp.org/index.php/Clickjacking"));
		refs.add(new Reference("KeyCDN Blog","https://www.keycdn.com/blog/x-frame-options"));
	    referenceMap.put(Rule.HEADER_XFRAME,refs);

		refs = new ArrayList<>();
		refs.add(new Reference("Mozilla MDN","https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection"));
		refs.add(new Reference("KeyCDN","https://www.keycdn.com/blog/x-xss-protection"));
	    referenceMap.put(Rule.HEADER_XSS,refs);

		refs = new ArrayList<>();
		refs.add(new Reference("Helmet Nosniff","https://helmetjs.github.io/docs/dont-sniff-mimetype/"));
		refs.add(new Reference("Denim Group Blog","https://www.denimgroup.com/resources/blog/2019/05/mime-sniffing-in-browsers-and-the-security-implications/"));
	    referenceMap.put(Rule.HEADER_CONTENT_TYPE_OPTIONS,refs);
 
		refs = new ArrayList<>();
		refs.add(new Reference("OWASP Referrer-Policy","https://owasp.org/www-project-secure-headers/#referrer-policy"));
		refs.add(new Reference("Scott Helme: A new security header: Referrer Policy","https://scotthelme.co.uk/a-new-security-header-referrer-policy/"));		
	    referenceMap.put(Rule.HEADER_REFERRER,refs);

	    return referenceMap;
	}
	
	public static Map<String,List<Reference>> savedReferences(String filename){
		return createDefaultReferences();
	}
	
//	public static Policy savedPolicy(String filename) {
//		Policy policy = null;	
//		try {
//			policy = ReferenceHandler.readPolicy(filename);
//		} catch (Exception e) {
//			policy = ReferenceHandler.createDefaultPolicy();							// File not found
//		}
//		if (policy == null ) policy = ReferenceHandler.createDefaultPolicy();			// File is empty
//		return policy;
//	}
//
//	private static Policy readPolicy(String filename) throws FileNotFoundException {
//		Gson gson = new Gson();
//		Policy policy = null;
//		try (Reader reader = new FileReader(filename)) {
//    		policy = gson.fromJson(reader, Policy.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }      
//		return policy;
//	}
//	
	public static void writeReferences(Map<String, List<Reference>> referenceMap, String filename) throws IOException {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
        	//System.out.println("Writing References JSON:" + writer);
            gson.toJson(referenceMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
