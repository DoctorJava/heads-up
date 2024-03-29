package com.websecuritylab.tools.headers.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class Report {
	private String _name;
	private String _url;
	private String _date;
	private Policy _policy;
	private List<ReportItem> _items;
	private String _rawHeaders;
	private List<Cookie> _cookies;
	private Boolean _pass = true;

	public Report(String name, Policy policy, List<ReportItem> items, Headers headers) {
		_name = name;
		
		_date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		_policy = policy;

		_rawHeaders =  headers.getRawHeaders();
		
		_cookies = headers.getCookies();
		
		_items = items;
		for ( ReportItem item: items ) if (!item.isCompliant()) _pass=false;
		
	}


	public Policy getPolicy() {
		return _policy;
	}
	public String getName() {
		return _name;
	}
	public String getDate() {
		return _date;
	}
	public List<ReportItem> getItems() {
		return _items;
	}
	public String getRawHeaders() {
		return _rawHeaders;
	}
	public String getUrl() {
		return _url;
	}
	public List<Cookie> getCookies() {
		return _cookies;
	}
	public Boolean getPass() {
		return _pass;
	}


	//
	// Setters
	//
	public void setUrl(String url) {
		_url = url;
	}
	


}
