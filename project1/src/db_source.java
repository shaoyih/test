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
	
	public db_source() throws NamingException {
		random = new Random();
		Context initCtx = new InitialContext();
		
		//checking for env
	    env = (Context) initCtx.lookup("java:comp/env");
	}
	
	
	
	public DataSource read_from() throws NamingException {
		int num = random.nextInt(100);
		
	    if (env == null) {
	    System.out.println("env is null");
	    }
	    
	    DataSource ds;
	    //decide which ds to use
	    if(num<50) {
	    	System.out.println("ds is master");
	    	 ds = (DataSource) env.lookup("jdbc/master_inst");
	    }
	    else {
	    	System.out.println("ds is slave");
	    	 ds = (DataSource) env.lookup("jdbc/slave_inst");
	    }
	    return ds;
	}
	
	public DataSource write_to() throws NamingException {

		    //decide which ds to use
		    
		  System.out.println("ds is master");
		  DataSource ds = (DataSource) env.lookup("jdbc/master_inst");
		  
		  return ds;
		
	}

}
