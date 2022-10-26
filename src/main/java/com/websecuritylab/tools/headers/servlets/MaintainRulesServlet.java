package com.websecuritylab.tools.headers.servlets;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.digester.Rules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.websecuritylab.tools.headers.model.Rule;
import com.websecuritylab.tools.headers.FileHandler;
import com.websecuritylab.tools.headers.PolicyHandler;
import com.websecuritylab.tools.headers.ReferenceHandler;
import com.websecuritylab.tools.headers.constants.DoPostParams;
import com.websecuritylab.tools.headers.constants.ReqAttributes;
import com.websecuritylab.tools.headers.model.Policy;
import com.websecuritylab.tools.headers.model.Reference;

/**
 * Servlet implementation class MaintainRules
 */
public class MaintainRulesServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger( MaintainRulesServlet.class );  
	private static final long serialVersionUID = 1L;
	private static final String JSP_EDIT_RULES = "/WEB-INF/jsp/editRules.jsp";     
	private static final String JSP_SHOW_RULES = "/WEB-INF/jsp/showRules.jsp";     
	public static final String JSON_READ_REFERENCES  = "/opt/apps/data/json/headsUp-references.json";     	// Used by CheckHandlerServlet
	private static final String JSON_WRITE_REFERENCES = "/opt/apps/data/json/headsUp-referencesOut.json";     
 
	
	public static final String JSON_READ_POLICY  = "headsUp-policy-default";     				// Used by CheckHandlerServlet
	private static final String JSON_WRITE_POLICY = "headsUp-policy-custom";     

	//private List<String> policyFilenames;
	
    public MaintainRulesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String JSP = ( "edit".equals(req.getParameter("method") ) ? JSP_EDIT_RULES : JSP_SHOW_RULES);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP);

		setAttributes(req);
		
		Map<String,List<Reference>> referenceMap = ReferenceHandler.savedReferences(JSON_READ_REFERENCES);
		ReferenceHandler.writeReferences(referenceMap, JSON_WRITE_REFERENCES);
        //res.setContentType("text/html;charset=UTF-8");
		
		dispatcher.forward(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP_SHOW_RULES);
		
		setAttributes(req);
		
		//Policy policy = PolicyHandler.createDefaultPolicy();			// TODO: Create FORM to modify policy
		
		PolicyHandler.writePolicy((Policy) req.getAttribute(ReqAttributes.POLICY), JSON_WRITE_POLICY);
		
        //res.setContentType("text/html;charset=UTF-8");
		dispatcher.forward(req, res); 
	}
	
	private void setAttributes(HttpServletRequest req) {
		req.setAttribute(ReqAttributes.POLICY_LIST, PolicyHandler.getPolicyNames());
		
		System.out.println( "Got PolicyName: " + ( req.getParameter(DoPostParams.POLICY_NAME) == null ));
		if ( req.getParameter(DoPostParams.POLICY_NAME) == null ) req.setAttribute(ReqAttributes.POLICY, PolicyHandler.getPolicy(Policy.DEFAULT));
		else req.setAttribute(ReqAttributes.POLICY, PolicyHandler.getPolicy(req.getParameter(DoPostParams.POLICY_NAME)));
		
		//Policy policy = PolicyHandler.getPolicy(JSON_READ_POLICY);
		//req.setAttribute(ReqAttributes.POLICY, policy);
		
		req.setAttribute(ReqAttributes.CONTAINS_TYPE_LIST, Rule.CONTAINS_TYPE.values());

	}
	

}
