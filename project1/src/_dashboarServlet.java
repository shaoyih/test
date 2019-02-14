import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.CallableStatement;
import java.util.*;

/**
 * Servlet implementation class _dashboarServlet
 */
@WebServlet(name="dashboarServlet",urlPatterns="/api/dashboard")
public class _dashboarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		
		String by=request.getParameter("by");
		String type=request.getParameter("type");
		if(by.equals("get") && type.equals("t")) {
			getAllNames(request,response);
		}
		if(by.equals("get") && type.equals("a")) {
			String name=request.getParameter("name");
			
			getAttrForTables(request,response,name);
		}
		else if (by.equals("insertS")) {
			System.out.println("inserting star");
			insertStar(request,response);
		}
		else if(by.equals("insertM")) {
			System.out.println("inserting movie");
			insertMovie(request,response);
		}
		
		
		
		
	}
	private void insertMovie(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		PrintWriter out=response.getWriter();
		try {
			
			Connection conn=dataSource.getConnection();
			String query="{call add_movie(?,?,?,?,?,?)}";
			CallableStatement cst=conn.prepareCall(query);
			String t=request.getParameter("title");
			String s=request.getParameter("star");
			String g=request.getParameter("genre");
			String y=request.getParameter("year");
			String d=request.getParameter("director");
			
			cst.setString(1, t);
			if(y!=null && y!="") {
			cst.setInt(2,Integer.parseInt(y));
			}
			else {
				cst.setString(2, null);
			}
			cst.setString(3, d);
			cst.setString(4, s);
			cst.setString(5, g);
			cst.registerOutParameter(6, Types.VARCHAR);
			boolean result=cst.execute();
			//dealing the feedback
			String output=cst.getString(6);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("message", output);
			out.write(jsonObject.toString());
			System.out.println(output);
			
		}catch(Exception e) {
			
			System.out.println("into exception");
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("message", e.getMessage());
			out.write(jsonObject.toString());
			response.setStatus(501);
		}
	}
	private void insertStar(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		PrintWriter out=response.getWriter();
		try {
			
			Connection conn=dataSource.getConnection();
			String query="{call add_star(?,?,?)}";
			CallableStatement cst=conn.prepareCall(query);
			
			String SN=request.getParameter("starName");
			String t=request.getParameter("starBirth");
			System.out.println("result t is :'"+t+"'");
			cst.setString(1, SN);
			
			if(t!=null && t!="") {
		
			cst.setInt(2,Integer.parseInt(t));
			}
			else {
				cst.setString(2, null);
			}
			cst.registerOutParameter(3,Types.VARCHAR);
			
			
			boolean result=cst.execute();
			String output=cst.getString(3);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("message", output);
			out.write(jsonObject.toString());
			System.out.println(output);
			
		}catch(Exception e) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			response.setStatus(500);
		}
	}
	private void getAttrForTables(HttpServletRequest request, HttpServletResponse response,String name) throws ServletException,IOException {
		System.out.println("into name process!");
		System.out.println(name);
		PrintWriter out=response.getWriter();
		String query="SELECT COLUMN_NAME as name,DATA_TYPE as type FROM information_schema.COLUMNS WHERE table_name= ? ;";
		
		
		try {
			Connection conn=dataSource.getConnection();
			PreparedStatement statement=conn.prepareStatement(query);
			statement.setString(1,name);
			ResultSet rs=statement.executeQuery();
			JsonArray jsonArray = new JsonArray();
            
            while (rs.next()) {
            	String attrName = rs.getString("name");
            	String attrType =rs.getString("type");
            	JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name",attrName);
                jsonObject.addProperty("type",attrType);
                jsonArray.add(jsonObject);
            }
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            conn.close();
            System.out.println("finish attr process");
		}catch (Exception e) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			response.setStatus(500);

        }
        out.close();
	}
	
	private void getAllNames(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		
		PrintWriter out=response.getWriter();
		String query="SELECT TABLE_NAME as name FROM information_schema.TABLES WHERE `TABLE_SCHEMA`='moviedb';";
		
		try {
			Connection conn=dataSource.getConnection();
			Statement statement=conn.createStatement();
			ResultSet rs=statement.executeQuery(query);
			JsonArray jsonArray = new JsonArray();
            
            while (rs.next()) {
            	String name = rs.getString("name");
            	JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name",name);
                jsonArray.add(jsonObject);
            }
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            conn.close();
            
		}catch (Exception e) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			response.setStatus(500);

        }
        out.close();
	}

	
	

}
