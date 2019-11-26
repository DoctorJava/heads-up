package com.websecuritylab.tools.headers.model;

import java.util.List;

public final class ReportItem {
	private String _headerName;
	//private String[] _headerValues;
	private List<String> _headerValues;
	private Rule _rule;
	private boolean _isPresent;			
	//private boolean _isRequired;			// required is TRUE or FALSE
	private Boolean compliant;			// compliant is TRUE or FALSE or NULL ( if not required )
	
	public ReportItem(Rule rule, String headerName, List<String> headerValues, boolean isPresent, Boolean isCompliant) {
		_rule = rule;
		_headerName = headerName;
		_headerValues = headerValues;
		_isPresent = isPresent;
		compliant = isCompliant;
	}
	
	public Rule getRule() {
		return _rule;
	}

	public void setRule(Rule rule) {
		_rule = rule;
	}
	
	public String getHeaderName() {
		return _headerName;
	}

	public void setHeaderName(String headerName) {
		_headerName = headerName;
	}

	public List<String> getHeaderValues() {
		return _headerValues;
	}

	public void setHeaderValues(List<String> headerValues) {
		_headerValues = headerValues;
	}	


	public boolean isPresent() {
		return _isPresent;
	}

	public void setPresent(boolean isPresent) {
		_isPresent = isPresent;
	}


	public Boolean isCompliant() {
		return compliant;
	}

	public void setCompliant(Boolean isCompliant) {
		compliant = isCompliant;
	}
}
