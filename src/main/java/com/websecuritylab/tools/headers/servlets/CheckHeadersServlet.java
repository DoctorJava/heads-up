package com.websecuritylab.tools.headers.servlets;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.websecuritylab.tools.headers.PolicyEnforcer;
import com.websecuritylab.tools.headers.UrlManager;
import com.websecuritylab.tools.headers.constants.DoPostParams;
import com.websecuritylab.tools.headers.constants.ReqAttributes;
import com.websecuritylab.tools.headers.exceptions.InvalidUrlException;
import com.websecuritylab.tools.headers.exceptions.SiteNotFoundException;
import com.websecuritylab.tools.headers.model.Headers;
import com.websecuritylab.tools.headers.model.Policy;
import com.websecuritylab.tools.headers.model.Reference;
import com.websecuritylab.tools.headers.model.Report;
import com.websecuritylab.tools.headers.model.ReportItem;
import com.websecuritylab.tools.headers.model.Rule;
import com.websecuritylab.tools.headers.util.PolicyHandler;
import com.websecuritylab.tools.headers.util.ReferenceHandler;

/**
 * Servlet implementation class CheckHeaders
 */
public class CheckHeadersServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger( CheckHeadersServlet.class );  
	private static final long serialVersionUID = 1L;
	private static final String JSP_PRINT_REPORT = "/WEB-INF/jsp/printReport.jsp";     
	private static final String JSP_PAGE = "/WEB-INF/jsp/page.jsp";     

    public CheckHeadersServlet() {
        super();
        
    }
    							// GET is used with the URL so the results can be copy/pasted into email/notes.
    
    
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    String policyName = req.getParameter(DoPostParams.POLICY_SELECT);
	    Policy policy = PolicyHandler.getPolicy(policyName);

		//System.out.println("Got Policy from GET: " + policy);
		//System.out.println("Got URL from GET: " + req.getRequestURI() + "?" + req.getQueryString());
		//System.out.println("Got URL from GET: " + UrlHandler.getURL(req));
		req.setAttribute(DoPostParams.REQUEST_URL, UrlManager.getURL(req));
		doBoth(req, res, policy);
	}

			// Custom policy.json requires post, which does not support URL being copied into email/notes
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    String policyStr = req.getParameter(DoPostParams.POLICY);
        Gson gson = new Gson();
        Policy policy = gson.fromJson(policyStr, Policy.class);
		System.out.println("Got Policy from POST: " + policy);
		doBoth(req, res, policy);
	}

	private void doBoth(HttpServletRequest req, HttpServletResponse res, Policy policy) throws ServletException, IOException {
		String reportName = req.getParameter(DoPostParams.REPORT_NAME);
		boolean processURL = ("url".equals(req.getParameter(DoPostParams.TEST_TYPE)));
		String testUrl = req.getParameter(DoPostParams.TEST_URL);
		String pageType = req.getParameter(DoPostParams.PAGE_TYPE);					// req param is either 'print' or NULL
		if ( pageType == null ) pageType = "report";								// THis is so pageType is added to UrlManager.getURL without being present in the original URL
		req.setAttribute(ReqAttributes.PAGE_TYPE, pageType);						// req attribute is either 'report' or 'print'
		String JSP = ( "print".equals(pageType) ? JSP_PRINT_REPORT : JSP_PAGE);

		try {
//			UrlHandler handler = new UrlHandler(testUrl);
//			Map<String, List<String>> headerMap = handler.getHeaderMap();
//			Headers headers = new Headers(headerMap);

		Headers headers;
		if (processURL) {
			if (testUrl == null || testUrl.length() == 0) {
				res.sendRedirect("index.jsp"); // goto index.html
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

		Report report = getReport(reportName, enforcer, policy);
		report.setUrl(testUrl);
		req.setAttribute("report", report);
		req.setAttribute(ReqAttributes.POLICY, report.getPolicy());		// JSPF needs ${policy} because it is show on both REPORT and MAINTENANCE screen

		//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP_SHOW_REPORT);
		req.setAttribute(ReqAttributes.PAGE_TYPE, "report");
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP);
		// response.setContentType("text/html;charset=UTF-8");
		dispatcher.forward(req, res);
	} catch (SiteNotFoundException e) {
		e.printStackTrace();
	} catch (InvalidUrlException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
	}
	
//	private Policy getPolicy(boolean useDefault ) {
//		Policy policy = PolicyHandler.getPolicy(MaintainRulesServlet.JSON_READ_POLICY);
//		Map<String,List<Reference>> refMap = ReferenceHandler.savedReferences(MaintainRulesServlet.READ_REFERENCES);
////		for ( Rule r: policy.getRules()) {
////			System.out.println("Adding Rule ("+r.getHeaderName()+") with References: " + refMap.get(r.getHeaderName()));
////		}
//		return policy;
//	}
	
	private Headers getHeadersFromUrl(String url, Policy policy) throws MalformedURLException, InvalidUrlException, SiteNotFoundException {
		UrlManager urlManager = new UrlManager(url);

		Map<String, List<String>> headerMap = urlManager.getHeaderMap();
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
	private Report getReport(String reportName, PolicyEnforcer enforcer, Policy policy) {
        List<ReportItem> items = new ArrayList<>();
        Headers headers = enforcer.getHeaders();
        
		//Policy policy = PolicyHandler.savedPolicy(MaintainRulesServlet.JSON_READ_POLICY);
        
        
        
        for (Rule rule : policy.getRules()) { 
        	String headerName = rule.getHeaderName();
        	System.out.println("GGGGGGGGGGGetting Report: " + rule.getHeaderName() + ": "+ rule.isRequired() + ":  is CaseSensitive: "+ policy.isCaseSensitiveValues());
        	boolean present =  enforcer.isPresent(rule);
        	boolean compliant = false;
        	if (rule.isRequired()) {
        		if ( !present ) compliant = false;
         		compliant = enforcer.isCompliant(rule, policy.isCaseSensitiveValues());
         		
                items.add(new ReportItem(rule, headerName, headers.getValues(headerName), present, compliant));
        	}
        	
        }
 		
        return new Report(reportName, policy, items, headers);

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
