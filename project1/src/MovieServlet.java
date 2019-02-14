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
        boolean browse=false;
        boolean search=false;
        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();
            
            if (mode.equals("browse")) {
            	result+=updateByBrowse(request);
            	browse=true;
            }
            else {
            	result+=updateBySearch(request);
            	search=true;
            }
            total_page=getTotalPage( dbcon,request, result, browse, search, mode);
            //no matter what mode, we always need to add sorting and pages
            
            result+=updateBySort(request);
            
            result+=updateByPage(request);
           
            
            

            // Perform the query
            
            
            PreparedStatement prepare=dbcon.prepareStatement(result);
            updateParameter(request,prepare,browse,search,true);
           
            ResultSet rs = prepare.executeQuery();

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
            System.out.println("finish");
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
	public void updateParameter(HttpServletRequest request,PreparedStatement result,boolean browse,boolean search,boolean checkLimit) {
		
		  int index=1;
		
	       try {
	    	   if (browse) {
		   			String genre = request.getParameter("genre");
		   			String alpha = request.getParameter("startsWith");
		   			if (genre != null) {
		   				result.setString(index++, genre);
		   				
		   				
		   			}
		   			else{
		   				result.setString(index++, alpha+"%");
		   			}
	    	   }
	    	   //search
	    	   else {
	    		   String title = request.getParameter("title");
	    	        String year = request.getParameter("year");
	    			String director = request.getParameter("director");
	    	        String star = request.getParameter("stars");
	    	        if(title!="" &&!title.equals("null")) {
	    	        	result.setString(index++, "%"+title+"%");
	    	        }
	    	        
	    	        if(year!=""&&!year.equals("null")) {
	    	        	result.setInt(index++, Integer.parseInt(year));
	    	        }
	    	        if (director!=""&&!director.equals("null")){
	    	        	result.setString(index++, "%"+director+"%");
	    	        }
	    	        if(star!=""&&!star.equals("null")) {
	    	        	result.setString(index++, "%"+star+"%");
	    	        }
	    		   
	    		   
	    	   }
	    	   System.out.println("after-------");
	    	   System.out.println(result);
	    	   if (checkLimit) {
	    	   int limit = Integer.parseInt(request.getParameter("limit"));
	           String page = request.getParameter("page");
	           int offset= (Integer.parseInt(page)-1)*limit;
	           result.setInt(index++,limit);
	           result.setInt(index++,offset);
	           System.out.println(result);
	    	   }
			} catch (SQLException e) {
				
			}
	       
		
	}
	
	public int getTotalPage(Connection dbcon ,HttpServletRequest request,String result,boolean browse,boolean search,String mode) {
		
		//processing the query
				String query="SELECT COUNT(*) as ct ";
				int index=result.indexOf("from");
				query=query + result.substring(index);
				System.out.println(mode);
				
				if(mode.equals("search")) {
					query="SELECT COUNT(*) as ct FROM(\n"+query+") as SubQuery";
				}
				System.out.println(query);
				
				int numberOfRows=0;
				try {
					
					PreparedStatement prepare=dbcon.prepareStatement(query);
		            updateParameter(request,prepare,browse,search,false);
		            System.out.println(prepare);
					ResultSet rs = prepare.executeQuery();
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
        if (genre != null) {
        	//browse by genre
        	
        	query= "select movies.id,title, year, director,rating\n" + 
        			"from movies, ratings ,genres_in_movies g,genres\n" + 
        			"where movies.id=ratings.movieId and  g.movieId=movies.id "
        			+ "AND g.genreId=genres.Id AND genres.name= ? \n";
        	
        }
        else {
        	//browse by title
        	query=
        			"select movies.id,title, year, director,rating\n" + 
        			"from movies, ratings \n" + 
        			"where movies.id=ratings.movieId and movies.title like ? \n";
        	
        }
        System.out.println(query);
		return query;
	}
	public String updateBySort(HttpServletRequest request) {
		String order = request.getParameter("order");
		
		if(order!=null) {
			if(order.equals("rateA")) {
				return "ORDER BY rating ASC ";
			}
			else if(order.equals("rateD")) {
				return "ORDER BY rating DESC ";
			}
			else if(order.equals("titleA")) {
				return "ORDER BY title ASC ";
			}
			else if(order.equals("titleD")) {
				return "ORDER BY title DESC ";
			}
		}
		return "";
	}
	public String updateByPage(HttpServletRequest request) {
		
        return "LIMIT ? OFFSET ? ;";
//		return "LIMIT "+limit+" OFFSET "+Integer.toString(offset)+";";
	}
	
	public String updateBySearch(HttpServletRequest request) {
		String query="select m.id,title, year, director,rating\n"
				+"from movies m, ratings r,stars s,stars_in_movies sm\n"
				+"WHERE m.id=r.movieId and sm.movieId=m.id and sm.starId=s.id";
		String title = request.getParameter("title");
        String year = request.getParameter("year");
		String director = request.getParameter("director");
        String star = request.getParameter("stars");
        if(title!="" &&!title.equals("null")) {
        	query+=" and m.title LIKE ? \n";
        }
        
        if(year!=""&&!year.equals("null")) {
        	query+=" and year=? \n";
        }
        if (director!=""&&!director.equals("null")){
        	query+=" and director LIKE ? \n";
        }
        if(star!=""&&!star.equals("null")) {
        	query+=" and s.name LIKE ? \n";
        }
        
        
//        if(title!="" &&!title.equals("null")) {
//        	query+=" and m.title LIKE '%"+title+"%'\n";
//        }
//        
//        if(year!=""&&!year.equals("null")) {
//        	query+=" and year="+year+"\n";
//        }
//        if (director!=""&&!director.equals("null")){
//        	query+=" and director LIKE '%"+director+"%'\n";
//        }
//        if(star!=""&&!star.equals("null")) {
//        	query+=" and s.name LIKE '%"+star+"%'\n";
//        }
        
        query+="Group by m.id,title, year, director,rating\n";
        System.out.println(query);
        return query;
        
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
        		"where g.movieId = ? and g.genreId=genres.id;\n";

        
        try {
        	PreparedStatement prepare=dbcon.prepareStatement(query);
        	prepare.setString(1, id);
			ResultSet res = prepare.executeQuery();
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
        		"where stars_in_movies.movieId= ? and stars_in_movies.starId=stars.id;\n" + 
        		"";

        // Perform the query
        try {
        	PreparedStatement prepare=dbcon.prepareStatement(query);
        	prepare.setString(1, id);
			ResultSet res = prepare.executeQuery();
			
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