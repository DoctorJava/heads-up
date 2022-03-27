package com.websecuritylab.tools.headers.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.websecuritylab.tools.headers.constants.DoPostParams;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Servlet implementation class CheckHttpMethods
 */
public class CheckHttpMethods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckHttpMethods() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		URL url = new URL("https://carsonline.com");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		System.out.println(conn.getRequestMethod()); // GET
		conn.setRequestMethod("OPTIONS");
		System.out.println(conn.getHeaderField("Allow")); // depends
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
