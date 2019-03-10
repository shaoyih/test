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

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * Servlet implementation class employeeLoginServlet
 */
@WebServlet(name = "employeeLoginServlet", urlPatterns = "/api/employee_login")
public class employeeLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
      
        try {
			if (this.loginSucceed(username,password)) {
			    
			    String sessionId = ((HttpServletRequest) request).getSession().getId();
			    Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
			    request.getSession().setAttribute("employee", new employee(username));

			    JsonObject responseJsonObject = new JsonObject();
			    responseJsonObject.addProperty("status", "success");
			    responseJsonObject.addProperty("message", "success");

			    response.getWriter().write(responseJsonObject.toString());
			} else {
			    // Login fails
			    JsonObject responseJsonObject = new JsonObject();
			    responseJsonObject.addProperty("status", "fail");
			    
			    responseJsonObject.addProperty("message", "USERNAME or PASSWORD is not correct!");
			    
			    response.getWriter().write(responseJsonObject.toString());
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private boolean loginSucceed(String username,String password) throws NamingException {
    	 Connection dbcon;
		try {
        	Context initCtx = new InitialContext();

            Context env = (Context) initCtx.lookup("java:comp/env");
            if (env == null) {
                
            System.out.println("ds is null");
            }
            DataSource ds = (DataSource) env.lookup("jdbc/moviedb");
            if (ds == null) {
                
                System.out.println("ds is null");
            }
            

            dbcon = ds.getConnection();
			
			Statement statement = dbcon.createStatement();
			String query = ("select * from employees where email= ?");
			
			PreparedStatement ps = dbcon.prepareStatement(query);
			ps.setString(1,username);
			
			ResultSet rs=ps.executeQuery();
			
			boolean checkResult= false;
			
			if (rs.next()) {
			    //get password
				String encryptedPassword = rs.getString("password");
				
				// use the encryptor we use before to excute
				checkResult = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
				System.out.println("get the pass ");
				
				return checkResult;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sql fail");
			e.printStackTrace();
		}

  
    	return false;
    }

}
