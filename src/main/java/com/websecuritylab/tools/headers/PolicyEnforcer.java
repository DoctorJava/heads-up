package com.websecuritylab.tools.headers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.websecuritylab.tools.headers.model.Cookie;
import com.websecuritylab.tools.headers.model.Headers;
import com.websecuritylab.tools.headers.model.Policy;
import com.websecuritylab.tools.headers.model.Rule;
import com.websecuritylab.tools.headers.servlets.MaintainRulesServlet;

public class PolicyEnforcer {
    private static final Logger logger = LoggerFactory.getLogger( PolicyEnforcer.class );  
    

	private Headers _headers;
	
	public PolicyEnforcer(Headers headers) {
		_headers = headers;
	}
	
	
    
//	private Policy _policy;
//	
//	public PolicyEnforcer(Policy policy) {
//		_policy = policy;
//	}


//	public List<String> getValueList(HEADER header){
//		return _headerMap.get(header.toString());
//	}
//	public String[] getValues(HEADER header){
//		if ( getValueList(header) == null ) return null;
//		else return getValueList(header).toArray(new String[0]);
//	}	
	




	public Headers getHeaders() {
		return _headers;
	}



	public boolean isPresent(Rule rule) {
		//System.out.println("Looking for Header Values for ("+rule.getHeaderName() +") KEY: " + _headers.getValues( rule.getHeaderName() ));

		return ( _headers.getValues( rule.getHeaderName() ) != null );
	}	
	
	public boolean isCompliant(Rule rule, boolean caseSensitive) {
		boolean compliant = false;
		List<String> ruleValues = rule.getContains();
		List<String> headerValues = _headers.getValues( rule.getHeaderName() );
		
		//System.out.println("Found ("+headerValues.size()+") Header Values for ("+rule.getHeaderName() +") KEY: " + headerValues);
		
		String ruleVal;				
		String headerVal;						// Header strings start with a space when constructed with conn.getHeaderFields();
		switch(rule.getContainsType()) {
			case ONLY:
				ruleVal = ruleValues.get(0).trim();	
				if ( !caseSensitive ) ruleVal = ruleVal.toUpperCase();
				headerVal = headerValues.get(0).trim();						// Header strings start with a space when constructed with conn.getHeaderFields();
				if ( !caseSensitive ) headerVal = headerVal.toUpperCase();
				compliant = ( headerVal.equals(ruleVal));
				break;
			case ANY:
				for ( String ruleValAny : ruleValues ) {
					ruleVal = ruleValAny.trim();				
					if ( !caseSensitive ) ruleVal = ruleVal.toUpperCase();
					for ( String headerValAny : headerValues ) {
						headerVal = headerValAny.trim();						// Header strings start with a space when constructed with conn.getHeaderFields();
						
						
						System.out.println(">>> Checking ANY Header Values for ("+headerVal +") equals: " + ruleVal);
						
						if ( !caseSensitive ) headerVal = headerVal.toUpperCase();						
						if ( headerVal.equals(ruleVal)) {
							compliant = true;
							break;
						}
					}
				}
				break;
			case ALL:
				compliant = true;			// Assume true, and set false if you don't find one of the entries
				for ( String ruleValAll : ruleValues ) {
					boolean foundIt = false;
					ruleVal = ruleValAll.trim();				
					System.out.println(">>> Checking ruleVal equals: " + ruleVal);
					if ( !caseSensitive ) ruleVal = ruleVal.toUpperCase();
					for ( String headerValAll : headerValues ) {
						headerVal = headerValAll.trim();						// Header strings start with a space when constructed with conn.getHeaderFields();
						if ( !caseSensitive ) headerVal = headerVal.toUpperCase();
						System.out.println(">>>>>>> Checking ALL Header Values for ("+headerVal +") equals: " + ruleVal);
						if ( headerVal.equals(ruleVal)) {
							foundIt = true;
							continue;
						}
					}
					if ( !foundIt ) {
						compliant = false;
						break;					
					}
				}
				break;
			case NONE:
	    		compliant = true;
	    		break;
			default:
				logger.error("Error checking compliance. Missing CONTAINS_TYPE" + rule.getContainsType());
		} 
		return compliant;
	}		

	
	
//	public boolean isRequired(HEADER header) {
//		return header.required();
//	}	
//	public Boolean isCompliant(HEADER header) {
//		if ( !header.required() ) return null;				// This is for optional headers that are neither COMPLIANT nor NON-COMLIANT
//		else if ( isPresent(header) ) return true;
//		else return false;
//	}
//	public boolean isPresent(HEADER header) {
//		for(String name : _headerMap.keySet()){
//			if ( name == null ) continue;
//			if ( name.equalsIgnoreCase(header.toString()) ) return true;
//		}
//		return false;
//	}
	
}
