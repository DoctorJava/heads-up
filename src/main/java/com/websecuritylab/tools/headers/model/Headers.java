package com.websecuritylab.tools.headers.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.websecuritylab.tools.headers.UrlHandler;

public class Headers {
    private static final Logger logger = LoggerFactory.getLogger( Headers.class );  
    
   
	private CaseInsensitiveMap<String, List<String>> _headerMap;
	private String _rawHeaders;
	
	public Headers(Map<String, List<String>> headerMap) {
		_headerMap = new CaseInsensitiveMap<String, List<String>>(headerMap);
		_rawHeaders = UrlHandler.generateRawHeaders(headerMap);
	}
	
	public Headers(String rawHeaders) {
		_rawHeaders = rawHeaders;
		_headerMap  = UrlHandler.generateHeaderMap(rawHeaders);
	}	
	

	
	
	public List<String> getValues(String headerName) {
		return _headerMap.get(headerName);
	}
	

	public Map<String, List<String>> getHeaderMap() {
		return _headerMap;
	}
	public void setHeaderMap(Map<String, List<String>> headerMap) {
		//_headerMap = headerMap;
		_headerMap = new CaseInsensitiveMap<String, List<String>>(headerMap);
	}
	public String getRawHeaders() {
		return _rawHeaders;
	}
	public void setRawHeaders(String rawHeaders) {
		_rawHeaders = rawHeaders;
	}
	
	

}
