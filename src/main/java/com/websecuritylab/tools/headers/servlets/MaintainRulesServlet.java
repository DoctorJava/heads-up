package com.websecuritylab.tools.headers.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.websecuritylab.tools.headers.PropsManager;
import com.websecuritylab.tools.headers.PropsManager.PropName;
import com.websecuritylab.tools.headers.constants.DoPostParams;
import com.websecuritylab.tools.headers.constants.ReqAttributes;
import com.websecuritylab.tools.headers.model.Policy;
import com.websecuritylab.tools.headers.model.Reference;
import com.websecuritylab.tools.headers.model.Rule;
import com.websecuritylab.tools.headers.util.PolicyHandler;
import com.websecuritylab.tools.headers.util.ReferenceHandler;

/**
 * Servlet implementation class MaintainRules
 */
public class MaintainRulesServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger( MaintainRulesServlet.class );  
	private static final long serialVersionUID = 1L;
	private static final String JSP_PAGE = "/WEB-INF/jsp/page.jsp";     
	//private static final String JSP_EDIT_RULES = "/WEB-INF/jsp/editRules.jsp";     
	//private static final String JSP_SHOW_RULES = "/WEB-INF/jsp/showRules.jsp";     
	//public static final String JSON_READ_REFERENCES  = "/opt/apps/data/json/headsUp-references.json";     	// Used by CheckHandlerServlet
	//private static final String JSON_WRITE_REFERENCES = "/opt/apps/data/json/headsUp-referencesOut.json";     
	public static String REFERENCES_FILE = new PropsManager().getProperty(PropName.REFERENCES_FILE);
	
	public static final String JSON_READ_POLICY  = "headsUp-policy-default";     				// Used by CheckHandlerServlet
	//private static final String JSON_WRITE_POLICY = "headsUp-policy-custom";     
	private static final String POLICY_FOLDER = new PropsManager().getProperty(PropName.POLICY_FOLDER);


	//private List<String> policyFilenames;
	
    public MaintainRulesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//String pageType = ( "edit".equals(req.getParameter(DoPostParams.PAGE_TYPE) ) ? "edit" : "view");
		String pageType = req.getParameter(DoPostParams.PAGE_TYPE);
		String JSP = JSP_PAGE;
		req.setAttribute(ReqAttributes.PAGE_TYPE, pageType);					// This is either 'view' or 'edit'
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP);

		
		Map<String,List<Reference>> referenceMap = ReferenceHandler.savedReferences(REFERENCES_FILE.replace(".json",".yaml"));
		//ReferenceHandler.writeReferences(referenceMap, REFERENCES_FILE);
        //res.setContentType("text/html;charset=UTF-8");
		
		setCommonAttributes(req);
		if ( req.getParameter(DoPostParams.POLICY_NAME) == null ) req.setAttribute(ReqAttributes.POLICY, PolicyHandler.getPolicy(Policy.DEFAULT));
		else req.setAttribute(ReqAttributes.POLICY, PolicyHandler.getPolicy(req.getParameter(DoPostParams.POLICY_NAME)));
		dispatcher.forward(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP_SHOW_RULES);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JSP_PAGE);
		
		Policy updatedPolicy = PolicyHandler.buildPolicy(req);			// TODO: Create FORM to modify policy
		
		PolicyHandler.writePolicy(updatedPolicy, POLICY_FOLDER + updatedPolicy.getName() + "." + PolicyHandler.POLICY_EXT );
		
 		setCommonAttributes(req);
		req.setAttribute(ReqAttributes.POLICY,updatedPolicy);
		dispatcher.forward(req, res); 
	}
	
	private void setCommonAttributes(HttpServletRequest req) {
		req.setAttribute(ReqAttributes.POLICY_LIST, PolicyHandler.getPolicyNames());		
		req.setAttribute(ReqAttributes.CONTAINS_TYPE_LIST, Rule.CONTAINS_TYPE.values());
	}
	

}
