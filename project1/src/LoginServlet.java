import com.google.gson.JsonObject;


import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    
  
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String userAgent = request.getHeader("User-Agent");
    	/*
    	if (userAgent != null && !userAgent.contains("Android")) {
	    	String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
	       
	        try {
	            recaptchaVerify.verify(gRecaptchaResponse);
	        } catch (Exception e) {
	        	JsonObject responseJsonObject = new JsonObject();
	            responseJsonObject.addProperty("status", "fail");
	            
	            responseJsonObject.addProperty("message", e.getMessage());
	            
	            response.getWriter().write(responseJsonObject.toString());
	            return;
	        }
    	}*/
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
      
        try {
        	
        	
			if (this.loginSucceed(username,password,request)) {
			    
			    String sessionId = ((HttpServletRequest) request).getSession().getId();
			    Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
			    request.getSession().setAttribute("user", new User(username));

			    JsonObject responseJsonObject = new JsonObject();
			    responseJsonObject.addProperty("status", "success");
			    responseJsonObject.addProperty("message", "success");

			    response.getWriter().write(responseJsonObject.toString());
			
				System.out.println("loginsucc:" +request.getSession().getAttribute("user"));
			    System.out.println("success login ");
			} else {
			    // Login fails
			    JsonObject responseJsonObject = new JsonObject();
			    responseJsonObject.addProperty("status", "fail");
			    
			    responseJsonObject.addProperty("message", "USERNAME or PASSWORD is not correct!");
			    
			    response.getWriter().write(responseJsonObject.toString());
			    System.out.println("fail login ");
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
    }
    private boolean loginSucceed(String username,String password,HttpServletRequest request) throws NamingException {
    	boolean success = false;
    	
		try {
			db_source dbs=new db_source(request.getRequestURL().toString());
			
        	DataSource ds=dbs.getSource();
            Connection dbcon = ds.getConnection();
			String query = ("select * from customers where email= ?;");
			
			PreparedStatement rs = dbcon.prepareStatement(query);
			rs.setString(1,username);
			
			ResultSet res=rs.executeQuery();
			
			if (res.next()) {
			    // get the encrypted password from the database
				String encryptedPassword = res.getString("password");
				
				// use the same encryptor to compare the user input password with encrypted password stored in DB
				success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
			}

		



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql fail");
			e.printStackTrace();
		}

  
    	return success;
    }
}

    
