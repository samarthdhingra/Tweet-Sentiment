package com.ajax.servelets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.annotation.WebServlet;
//import com.google.gson.Gson;

import com.google.gson.Gson;

/**
 * Servlet implementation class UpdateUsername
 */
public class UpdateUsername extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUsername() {
        super();
 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isValid = false;
		System.out.println("This is called");
		String username = request.getParameter("username");
		if(username != null && username.trim().length() != 0) {
			isValid = true;
			map.put("username", username);
		}
		map.put("isValid", isValid);
		write(response, map);
	}

	private void write(HttpServletResponse response, Map<String, Object> map) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new Gson().toJson(map));
		//response.setContentType("text/html");  
		//response.getWriter().write("Hello");
		//PrintWriter out = response.getWriter();  
		//out.println("Hello");
	}

}
