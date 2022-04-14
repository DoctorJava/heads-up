package com.websecuritylab.tools.headers.model;

public final class Reference {
	//private String headerName;
	private String title;
	private String url;
	
	//public Reference(String headerName, String title, String url) {
	public Reference(String title, String url) {
		//this.headerName = headerName;
		this.title = title;
		this.url = url;
	}

//	public String getHeaderName() {
//		return headerName;
//	}
//	public void setHeaderName(String headerName) {
//		this.headerName = headerName;
//	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
 
}
