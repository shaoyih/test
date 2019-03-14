import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class db_source {
	Random random;
	Context env;
	String url;
	
	public db_source(String url1) throws NamingException {
		random = new Random();
		Context initCtx = new InitialContext();
		url=url1;
		//checking for env
	    env = (Context) initCtx.lookup("java:comp/env");
	}
	
	
	
	public DataSource multiple() throws NamingException {
		int num = random.nextInt(100);
		
	   
	    
	   
	    System.out.println("ds is loadbalancer one");
	    DataSource ds = (DataSource) env.lookup("jdbc/master_inst");
	    

	    return ds;
	}
	
	public DataSource single() throws NamingException {

		    //decide which ds to use
		    
		  System.out.println("ds is single one");
		  DataSource ds = (DataSource) env.lookup("jdbc/moviedb");
		  
		  return ds;
		
	}
	
	public DataSource getSource() throws NamingException {
		if(url.contains("172.31")) {
			return multiple();
		}
		else {
			return single();
		}
	}

}
