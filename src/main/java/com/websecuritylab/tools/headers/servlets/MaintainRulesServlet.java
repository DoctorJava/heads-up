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
import com.websecuritylab.tools.headers.PolicyHandler;
import com.websecuritylab.tools.headers.ReferenceHandler;
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
	public static final String JSON_READ_POLICY  = "/opt/apps/data/json/headsUp-policy-default.json";     				// Used by CheckHandlerServlet
	private static final String JSON_WRITE_POLICY = "/opt/apps/data/json/headsUp-policy-custom.json";     
	public static final String JSON_READ_REFERENCES  = "/opt/apps/data/json/headsUp-references.json";     	// Used by CheckHandlerServlet
	private static final String JSON_WRITE_REFERENCES = "/opt/apps/data/json/headsUp-referencesOut.json";     
      
    public MaintainRulesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String JSP = ( req.getParameter("edit") != null ? JSP_EDIT_RULES : JSP_SHOW_RULES);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP);
		
		Policy policy = PolicyHandler.savedPolicy(JSON_READ_POLICY);

		req.setAttribute(ReqAttributes.POLICY, policy);
		req.setAttribute(ReqAttributes.CONTAINS_TYPE_LIST, Rule.CONTAINS_TYPE.values());

		Map<String,List<Reference>> referenceMap = ReferenceHandler.savedReferences(JSON_READ_REFERENCES);
		ReferenceHandler.writeReferences(referenceMap, JSON_WRITE_REFERENCES);
        //res.setContentType("text/html;charset=UTF-8");
		dispatcher.forward(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP_SHOW_RULES);
		
		Policy policy = PolicyHandler.createDefaultPolicy();			// TODO: Create FORM to modify policy
		
		PolicyHandler.writePolicy(policy, JSON_WRITE_POLICY);
		
		req.setAttribute(ReqAttributes.POLICY, policy);
        //res.setContentType("text/html;charset=UTF-8");
		dispatcher.forward(req, res); 
	}
	
	

}
