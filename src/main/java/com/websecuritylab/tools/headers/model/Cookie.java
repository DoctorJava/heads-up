package com.websecuritylab.tools.headers.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Cookie {
	private String _name;
	private String _value;
	private List<String> _directives;
	private boolean _isDuplicate = false; 			// Duplicate cookies with the sane name are not allowed
													// https://portswigger.net/kb/issues/00400a00_duplicate-cookies-set 
	private Boolean _isCompliant = null;
	private List<Rule> _rules = new ArrayList<>();
	
	public Cookie(String name, String value, List<String> directives) {
		_name = name;

		_directives = directives;
										// Decode the URL special characters for more friendly viewing.  Look for sensitive information being disclosed
		try {
			_value = URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName() {
		return _name;
	}

	public String getValue() {
		return _value;
	}

	public List<String> getDirectives() {
		return _directives;
	}

	public boolean isDuplicate() {
		return _isDuplicate;
	}

	public void setDuplicate(boolean isDuplicate) {
		_isDuplicate = isDuplicate;
		if (isDuplicate) _isCompliant = false;
	}

	public Boolean isCompliant() {
		return _isCompliant;
	}

	public void setCompliant(Boolean isCompliant) {
		_isCompliant = isCompliant;
	}

	public List<Rule> getRules() {
		System.out.println("Getting COOKIE rules: " + _rules);
		return _rules;
	}

//	public void setRules(List<Rule> rules) {
//		_rules = rules;
//	}
	public void addRules(Rule rule) {
		_rules.add(rule);
	}	
	
	
	//
	//
	//
	public boolean isSession() {
		return _name.toLowerCase().contains("sess");			// Matches on default name: JSESSIONID, PHPSESSID, ASP.NET_SessionId
		//return _name.toLowerCase().contains("session");
	}
	
	
}
