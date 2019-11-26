package com.websecuritylab.tools.headers.model;

import java.util.List;

public final class Rule {
	
	public static enum CONTAINS_TYPE { MUST, ONLY, ANY, ALL, NONE }
	
	private String headerName;
	private boolean required;
	private CONTAINS_TYPE containsType;
	private List<String> contains;
//	private String containsExact;
//	private List<String> containsAny;
//	private List<String> containsAll;
	private List<Reference> references;
	
	public Rule(String name, boolean required) {
		this.headerName = name;
		this.required = required;
		this.containsType = CONTAINS_TYPE.NONE;
	}
//	public Rule(String name, String containsExact) {			// containsExact is a String ( not a list )
//		this.headerName = name;
//		this.required = true;
//		this.contains = containsExact;
//		this.containsType = CONTAINS_TYPE.EXACT;
//	}	
	public Rule(String name,  List<String> contains, CONTAINS_TYPE type ) {
		this.headerName = name;
		this.required = true;
		this.contains = contains;
		this.containsType = type;
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
		this.references = references;
	}
	



	
}
