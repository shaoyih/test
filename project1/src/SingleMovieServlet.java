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

@WebServlet(name = "SingleMovieServlet", urlPatterns = "/project1/single_movie")
public class SingleMovieServlet extends HttpServlet{
		
	
	
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
	public ArrayList<String> getGenres(Connection dbcon, String id){
		ArrayList<String> result = new ArrayList<String>(); 
		Statement statement = null;
		try {
			statement = dbcon.createStatement();
		} catch (SQLException e) {
			
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

			e.printStackTrace();
		}
        return result;
	}
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

            String query = "select id,title, year, director,rating\n" + 
            		"from movies, ratings\n" + 
            		"where movies.id = '"+id+"' and movies.id=ratings.movieId;\n";
            		

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();


            while (rs.next()) {
                String title = rs.getString("title");
                String year = rs.getString("year");
                String director = rs.getString("director");
                String rating = rs.getString("rating");
                String movie_id = rs.getString("id");
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
                jsonObject.addProperty("movie_id",movie_id);
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