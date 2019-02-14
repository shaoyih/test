import com.google.gson.JsonObject;

import javax.annotation.Resource;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
        try {
            recaptchaVerify.verify(gRecaptchaResponse);
        } catch (Exception e) {
            return;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
      
        if (this.loginSucceed(username,password)) {
            
            String sessionId = ((HttpServletRequest) request).getSession().getId();
            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
            request.getSession().setAttribute("user", new User(username));

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
    }
    private boolean loginSucceed(String username,String password) {
    	boolean success = false;
    	 Connection dbcon;
		try {
			dbcon = dataSource.getConnection();
			Statement statement = dbcon.createStatement();
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
			e.printStackTrace();
		}

  
    	return success;
    }
}

    
