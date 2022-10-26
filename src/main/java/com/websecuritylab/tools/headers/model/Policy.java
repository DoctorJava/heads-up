package com.websecuritylab.tools.headers.model;

import java.util.List;

public class Policy {
    public static final String DEFAULT = "default"; 
  
    private String name;
    private boolean caseSensitiveValues = false;
    private List<Rule> rules;
    
	public Policy(String name, List<Rule> rules) {
		this.name = name;
		this.rules = rules;
	}

	public String getName() {
		return name;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public boolean isCaseSensitiveValues() {
		return caseSensitiveValues;
	}

	public void setCaseSensitiveValues(boolean caseSensitiveValues) {
		this.caseSensitiveValues = caseSensitiveValues;
	}
    
	
}
