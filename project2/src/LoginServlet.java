import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.sql.DataSource;

import org.apache.tomcat.jni.User;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;


@WebServlet(name="LoginServlet",urlPatterns="/raw/login")
public class LoginServlet extends HttpServlet{
	
	@Resource(name="jdbc/moviedb")
	private DataSource dataS;
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		
		
		//getQuery rLoginServletesult
		
		
		try {
			Connection dbcon = dataS.getConnection();
			Statement st = dbcon.createStatement();
			
			JsonObject result= new JsonObject();
			String query="SELECT * FROM customers WHERE email='";
			query+=username+"'";
			ResultSet rs=st.executeQuery(query);

			if(rs.next()) {
				String realPass=rs.getString("password");
				if(realPass.equals(password)) {
					 String sessionId = ((HttpServletRequest) request).getSession().getId();
			         Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
			         result.addProperty("status","success");
			         result.addProperty("message","success");
			         response.getWriter().write(result.toString());
			         
				}
				else {
					result.addProperty("status","fail");
				       result.addProperty("message","wrong password");
				        response.getWriter().write(result.toString());
					}
				
			}
			else {
				result.addProperty("status","fail");
		        result.addProperty("message","account "+username+" is not exist");
		        response.getWriter().write(result.toString());
			}
		}
		catch(Exception e) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("message", e.getMessage());
					response.getWriter().write(jsonObject.toString());
					response.setStatus(500);
				}
			
			
		
		
		}
		
}
