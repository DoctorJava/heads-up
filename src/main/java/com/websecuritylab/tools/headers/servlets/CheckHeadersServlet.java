package com.websecuritylab.tools.headers.servlets;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.websecuritylab.tools.headers.PolicyEnforcer;
import com.websecuritylab.tools.headers.PolicyHandler;
import com.websecuritylab.tools.headers.UrlHandler;
import com.websecuritylab.tools.headers.constants.DoPostParams;
import com.websecuritylab.tools.headers.exceptions.InvalidUrlException;
import com.websecuritylab.tools.headers.exceptions.SiteNotFoundException;
import com.websecuritylab.tools.headers.model.Headers;
import com.websecuritylab.tools.headers.model.Policy;
import com.websecuritylab.tools.headers.model.Report;
import com.websecuritylab.tools.headers.model.ReportItem;
import com.websecuritylab.tools.headers.model.Rule;

/**
 * Servlet implementation class CheckHeaders
 */
public class CheckHeadersServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger( CheckHeadersServlet.class );  
	private static final long serialVersionUID = 1L;
	private static final String JSP_SHOW_REPORT = "/WEB-INF/jsp/showReport.jsp";     

    public CheckHeadersServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);		// GET is used with the URL so the results can be copy/pasted into email/notes.
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("Got SELECT: " + req.getParameter(DoPostParams.POLICY));
		boolean processURL = ("true".equals(req.getParameter(DoPostParams.PROCESS_URL)));
		String testUrl = req.getParameter(DoPostParams.TEST_URL);

		try {
//				UrlHandler handler = new UrlHandler(testUrl);
//				Map<String, List<String>> headerMap = handler.getHeaderMap();
//				Headers headers = new Headers(headerMap);

			Policy policy = getPolicy(true);
			Headers headers;
			if (processURL) {
				if (testUrl == null || testUrl.length() == 0) {
					res.sendRedirect("index.html"); // goto index.html
					return;
				}
				if (!testUrl.startsWith("http://") && !testUrl.startsWith("https://"))
					testUrl = "http://" + testUrl;
				logger.info("Got testUrl: " + testUrl);
				headers = getHeadersFromUrl(testUrl, policy);
			} else {
				String testHeaders = req.getParameter(DoPostParams.TEST_HEADERS);
				logger.info("Got testHeaders: " + testHeaders);

				headers = new Headers(testHeaders, policy);
				
			}

			PolicyEnforcer enforcer = new PolicyEnforcer(headers);

			Report report = getReport(enforcer, policy);
			report.setUrl(testUrl);
			req.setAttribute("report", report);
			req.setAttribute("policy", report.getPolicy());		// JSPF needs ${policy} because it is show on both REPORT and MAINTENANCE screen

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP_SHOW_REPORT);
			// response.setContentType("text/html;charset=UTF-8");
			dispatcher.forward(req, res);
		} catch (SiteNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidUrlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	
	private Policy getPolicy(boolean useDefault ) {
		Policy policy = PolicyHandler.savedPolicy(MaintainRulesServlet.JSON_READ_POLICY);
		return policy;
	}
	
	private Headers getHeadersFromUrl(String url, Policy policy) throws MalformedURLException, InvalidUrlException, SiteNotFoundException {
		UrlHandler handler = new UrlHandler(url);

		Map<String, List<String>> headerMap = handler.getHeaderMap();
		return new Headers(headerMap, policy);

	}
	
//	private Report getReport(PolicyEnforcerHardcodedEnum rules, String rawHeaders) {
//        List<ReportItem> items = new ArrayList<>();
//        for (HEADER header : HEADER.values()) { 
//        	System.out.println("Getting Report: " + header + ": "+ enforcer.getValues(header) + ": "+  enforcer.isCompliant(header));
//            items.add(new ReportItem(header, enforcer.getValues(header), enforcer.isRequired(header), enforcer.isCompliant(header)));
//        } 		
//        return new Report("Report Name", items, rawHeaders);
//	}
	
	//private Report getReport(PolicyEnforcer enforcer, Headers headers) {
	private Report getReport(PolicyEnforcer enforcer, Policy policy) {
        List<ReportItem> items = new ArrayList<>();
        Headers headers = enforcer.getHeaders();
        
		//Policy policy = PolicyHandler.savedPolicy(MaintainRulesServlet.JSON_READ_POLICY);
        
        
        
        for (Rule rule : policy.getRules()) { 
        	String headerName = rule.getHeaderName();
        	//System.out.println("Getting Report: " + rule.getHeaderName() + ": "+ rule.isRequired() + ": "+  enforcer.isPresent(rule));
        	boolean present =  enforcer.isPresent(rule);
        	boolean compliant = false;
        	if ( !present && rule.isRequired()) {
        		compliant = false;
        	}else {
        		compliant = enforcer.isCompliant(rule, policy.isCaseSensitiveValues());
        	}
            items.add(new ReportItem(rule, headerName, headers.getValues(headerName), present, compliant));
        	
        }
 		
        return new Report("Report Name", policy, items, headers);

	}
	
	private void writeJson(Policy rules) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(rules);

		try (FileWriter writer = new FileWriter("WebContent/json/staff.json")) {
			gson.toJson(rules, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
