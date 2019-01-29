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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@WebServlet(name = "MovieServlet", urlPatterns = "/project1/movies")
public class MovieServlet extends HttpServlet{
		
	
	
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type
        String mode = request.getParameter("by");
        String result="";
        int total_page=0;
        //check mode and decide which function to call
        
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();
            
            if (mode.equals("browse")) {
            	result+=updateByBrowse(request);
            }
            else {
            	result+=updateBySearch(request);
            }
            total_page=getTotalPage(statement,result);
            //no matter what mode, we always need to add sorting and pages
            
            result+=updateBySort(request);
            result+=updateByPage(request);
            
            
            

            // Perform the query
            ResultSet rs = statement.executeQuery(result);

            JsonArray jsonArray = new JsonArray();
            JsonObject page = new JsonObject();
            page.addProperty("totalPage", total_page);
            jsonArray.add(page);
            // Iterate through each row of rs
            while (rs.next()) {
                String title = rs.getString("title");
                String year = rs.getString("year");
                String director = rs.getString("director");
                String rating = rs.getString("rating");
                String id = rs.getString("id");
                
                ArrayList<String> genres=getGenres(dbcon,id);
                ArrayList<ArrayList<String>> stars=getStars(dbcon,id);
                
                Gson gson = new GsonBuilder().create();
                JsonArray gen_list = gson.toJsonTree(genres).getAsJsonArray();
                JsonArray star_list = gson.toJsonTree(stars).getAsJsonArray();
                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("title", title);
                jsonObject.addProperty("year", year);
                jsonObject.addProperty("director", director);
                jsonObject.addProperty("rating", rating);
                jsonObject.add("genres", gen_list);
                jsonObject.add("stars", star_list);
                jsonObject.addProperty("id",id);
                jsonArray.add(jsonObject);
               
            }
            
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
        	
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
        out.close();

    }
	public int getTotalPage(Statement statement,String result) {
		
		//processing the query
				String query="SELECT COUNT(*) as ct";
				int index=result.indexOf("from");
				query=query + result.substring(index);
				
				int numberOfRows=0;
				try {
					
					ResultSet rs = statement.executeQuery(query);
					rs.next();
					int ct=(int) rs.getLong("ct"); 
					numberOfRows=ct;
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return numberOfRows;
		
	}
	public String updateByBrowse(HttpServletRequest request) {
		String query="";
		String genre = request.getParameter("genre");
        String alpha = request.getParameter("startsWith");
        
        if (genre != null) {
        	//browse by genre
        	System.out.println(genre);
        	query= "select movies.id,title, year, director,rating\n" + 
        			"from movies, ratings ,genres_in_movies g,genres\n" + 
        			"where movies.id=ratings.movieId and  g.movieId=movies.id "
        			+ "AND g.genreId=genres.Id AND genres.name='"+genre+"'\n";
        	
        }
        else {
        	//browse by title
        	query=
        			"select movies.id,title, year, director,rating\n" + 
        			"from movies, ratings \n" + 
        			"where movies.id=ratings.movieId and movies.title like '"+alpha+"%'\n";
        			
        }
        
		return query;
	}
	public String updateBySort(HttpServletRequest request) {
		return "";
	}
	public String updateByPage(HttpServletRequest request) {
		String limit = request.getParameter("limit");
        String page = request.getParameter("page");
        int offset= (Integer.parseInt(page)-1)*Integer.parseInt(limit);
		return "LIMIT "+limit+" OFFSET "+Integer.toString(offset)+";";
	}
	
	public String updateBySearch(HttpServletRequest request) {
		return "";
	}
	
	public ArrayList<String> getGenres(Connection dbcon, String id){
		ArrayList<String> result = new ArrayList<String>(); 
		Statement statement = null;
		try {
			statement = dbcon.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String query = "select genres.name\n" + 
        		"from genres_in_movies g, genres\n" + 
        		"where g.movieId = '"+id+"' and g.genreId=genres.id;\n";

        // Perform the query
        try {
			ResultSet res = statement.executeQuery(query);
			while (res.next()) {
				result.add(res.getString("genres.name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
	}
	public ArrayList<ArrayList<String>> getStars(Connection dbcon, String id){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(); 
		Statement statement = null;
		try {
			statement = dbcon.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String query = "select stars.name,stars.id\n" + 
        		"from stars ,stars_in_movies\n" + 
        		"where stars_in_movies.movieId= '"+id+"'and stars_in_movies.starId=stars.id;\n" + 
        		"";

        // Perform the query
        try {
			ResultSet res = statement.executeQuery(query);
			while (res.next()) {
				ArrayList<String> temp= new ArrayList<String>();
				temp.add(res.getString("stars.name"));
				temp.add(res.getString("stars.id"));
				result.add(temp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
	}
	
	
} 