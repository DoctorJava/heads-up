package com.websecuritylab.tools.headers.model;

import java.util.List;

import com.websecuritylab.tools.headers.util.ReferenceHandler;

public final class Rule {
	
//	public static enum HEADER_NAME { CONTENT_TYPE("Content-Type"), CACHE_CONTROL("Cache-Control"), HSTS("strict-transport-security"), XFRAME("x-frame-options"), xss("X-XSS-Protection"), CONTENT_TYPE_OPTIONS("x-content-type-options"), REFERRER("Referrer-Policy");
//	private String name;
//	HEADER_NAME(String value) {name=value;}
//	public String getValue() { return name; }
//	}
	
	//public static enum CONTAINS_TYPE { MUST, ONLY, ANY, ALL, NONE }
	public static enum CONTAINS_TYPE { ONLY, ANY, ALL, NONE }
	
	public static String HEADER_CONTENT_TYPE = "Content-Type";
	public static String HEADER_CACHE_CONTROL = "Cache-Control";
	public static String HEADER_HSTS = "strict-transport-security";
	public static String HEADER_XFRAME = "x-frame-options";
	public static String HEADER_XSS = "X-XSS-Protection";
	public static String HEADER_CONTENT_TYPE_OPTIONS = "x-content-type-options";
	public static String HEADER_REFERRER = "Referrer-Policy";
	
	
	private String headerName;
	private boolean required;
	private CONTAINS_TYPE containsType;
	private List<String> contains;
//	private String containsExact;
//	private List<String> containsAny;
//	private List<String> containsAll;
	private List<Reference> references;
	
//	public Rule(String name, boolean required) {
//		this.headerName = name;
//		this.required = required;
//		this.containsType = CONTAINS_TYPE.NONE;
//		this.references = ReferenceHandler.getReferencesByHeader(name);
//	}
//	public Rule(String name, String containsExact) {			// containsExact is a String ( not a list )
//		this.headerName = name;
//		this.required = true;
//		this.contains = containsExact;
//		this.containsType = CONTAINS_TYPE.EXACT;
//	}	
	public Rule(String name,  boolean required, List<String> contains, CONTAINS_TYPE type ) {
		this.headerName = name;
		this.required = required;
		this.contains = contains;
		this.containsType = type;
		this.references = ReferenceHandler.getReferencesByHeader(name);
	}
	
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public CONTAINS_TYPE getContainsType() {
		return containsType;
	}
	public void setContainsType(CONTAINS_TYPE containsType) {
		this.containsType = containsType;
	}
	public List<String> getContains() {
		return contains;
	}
	public void setContains(List<String> contains) {
		this.contains = contains;
	}
	public List<Reference> getReferences() {
		return references;
	}
	public void setReferences(List<Reference> references) {
		//this.references = references;
		// TODO: Remove this obsolete method
	}
	



	
}
