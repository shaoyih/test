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
import java.util.*;

@WebServlet(name = "getGenres", urlPatterns = "/project1/get_genres")
public class getGenres extends HttpServlet{
		
	
	
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type
        String id = request.getParameter("id");
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();

            String query = "select distinct name from genres;";
            		

            PreparedStatement prepare = dbcon.prepareStatement(query);
        	
			
			ResultSet rs=prepare.executeQuery();
			

            JsonArray jsonArray = new JsonArray();


            while (rs.next()) {
                String name = rs.getString("name");
                
                Gson gson = new GsonBuilder().create();
               
                JsonObject jsonObject = new JsonObject();
               
                jsonObject.addProperty("name",name);
                jsonArray.add(jsonObject);
               
            }
            

            out.write(jsonArray.toString());

            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
        	

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());


			response.setStatus(500);

        }
        out.close();

    }
}