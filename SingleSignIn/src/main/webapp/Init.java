package com.uploadIsv.requiredFiles;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Init
 */
@WebServlet("/Init")
public class Init extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Init() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//obtain the header parameters
		String userIDpara=request.getParameter("userID");
    	String sessionIDpara=request.getParameter("sessionID");
    	
		//if one of both the parameters are null --> unauthorised user
		if(userIDpara==null || sessionIDpara==null){
    		response.sendRedirect("unauthorisedUser.jsp");  //the page which indicates unauthorised user
    	}
    	else{
		   	//int conversion					    
		  	int userID=Integer.parseInt(userIDpara);
		  	int sessionID=Integer.parseInt(sessionIDpara);
		  	
		  	UserSearch us1=new UserSearch();
		  	
		  	//if the IDs are valid->if they are in the LoggedIn table
		  	if(us1.search(userID, sessionID)==1) {
    		HttpSession session=request.getSession();	
    		 session.setAttribute("username",us1.getUsername());
		     session.setAttribute("role",us1.getRole());
		     response.sendRedirect("index.jsp");  //the homepage of the app
		  	}
		  	else {
    		response.sendRedirect("unauthorisedUser.jsp");  //the page which indicates unauthorised user
		  	}    	
	     }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
