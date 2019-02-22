package Parsing;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import java.io.PrintWriter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.annotation.Resource;
import javax.sql.DataSource;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Movie_parse extends DefaultHandler{
	private HashSet<Movie> movies;
	private String tempVal;
	private Movie movieTemp;
	private Connection dbcon ;
	
	
	private HashSet<String> genres;
	private HashSet<String> movies_id;
	private HashSet<Movie> movies_in_db;
	
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
	
	public Movie_parse() {
		movies=new HashSet<>();
		genres=new HashSet<>();
		movies_id=new HashSet<>();
		movies_in_db=new HashSet<>();
		String jdbcURL="jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
    	try {
			dbcon = DriverManager.getConnection(jdbcURL,"mytestuser", "mypassword");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
	public void parseDocument() {
			try {
				movies=loadDBdata();
			
				movies_in_db.addAll(movies);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        try {
	            SAXParser sp = spf.newSAXParser();
	            sp.parse("mains243.xml", this);

	        } catch (SAXException se) {
	            se.printStackTrace();
	        } catch (ParserConfigurationException pce) {
	            pce.printStackTrace();
	        } catch (IOException ie) {
	            ie.printStackTrace();
	        }
	        movies.removeAll(movies_in_db);
	        //keep only parse movies
	    }
	public HashSet<Movie> loadDBdata() throws SQLException {
		HashSet<Movie> result=new HashSet<>();
		 Statement statement = dbcon.createStatement();

         String query = "select id,title, year, director from movies";
         		

         PreparedStatement prepare = dbcon.prepareStatement(query);
     	
			
		ResultSet rs=prepare.executeQuery();
			

        


         while (rs.next()) {
            
			try {
				Movie temp1=new Movie();
				String title = rs.getString("title");
				int year = rs.getInt("year");
	             String director = rs.getString("director");
	             
	             String movie_id = rs.getString("id");
	             temp1.setDirector(director);
	             temp1.setYear(year);
	             temp1.setTitle(title);
	             temp1.setId(movie_id);
	             result.add(temp1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            
         }
		return result;
         
         
		
	}
	 public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	        //reset
	        tempVal = "";
	        
	        if (qName.equalsIgnoreCase("film")) {
	            //create a new instance of employee
	            movieTemp = new Movie();
	       
	        }
	        
	    }
	 public void characters(char[] ch, int start, int length) throws SAXException {
	        tempVal = new String(ch, start, length);
	        
	    }
	 private boolean isYear(String str) {
	    	if(str==null || str.isEmpty()||str.length()!=4) {
	    		return false;
	    	}
	    	for (int i=0;i<str.length();i++) {
	    		char c = str.charAt(i);
	            if (c < '0' || c > '9') {
	                return false;
	            }
	    	}
	    	return true;
	    }

	 public void endElement(String uri, String localName, String qName) throws SAXException {
		 tempVal=tempVal.trim();
		 if (qName.equalsIgnoreCase("film")) {
	            
				 String id=movieTemp.getId();;
	 	 		String title=movieTemp.getTitle();
	 	 		int year=movieTemp.getYear();
	 	 		String director=movieTemp.getDirector();
	 	 		
	 	 		if (id==null ||id =="" || title==null || title ==""|| year==0 || director==null ||director =="") {
	 	 			System.out.println("error (empty required field)"+movieTemp);
	 	 			//field is empty
	 	 		}
	 	 		else {
	 	 			if(movies_id.contains(id)) {
				 		//one fid to multiple value
				 		System.out.println("duplicate fid ("+id+")exists,"+ title+" is escaping");
				 	}
				 	else {
				 		if (movies.contains(movieTemp)) {
				 			//duplicate according to project 3 movie identifier (title,director,year)
				 			System.out.println("duplicate field (director and year and title) exists,"+ title+" is escaping");
				 		}
				 		else{
				 			movies.add(movieTemp);
				 			movies_id.add(id);
				 		}
				 		
				 	}
	 	 			
	 	 		}
			 	
	    }
		 else if (qName.equalsIgnoreCase("fid")) {
			 	movieTemp.setId(tempVal.toLowerCase());
			 	
			 	//System.out.println(tempVal);
		 }
		 else if (qName.equalsIgnoreCase("t")) {
			 	movieTemp.setTitle(tempVal.toLowerCase());
		 }
		 else if(qName.equalsIgnoreCase("year")) {
			 if(isYear(tempVal)) {
		         movieTemp.setYear(Integer.parseInt(tempVal));
		      } 
			 else {
				 movieTemp.setYear(0);
			 }
		 }
		 //for director we only require one name so we set the last as our final director, reason use dirs instead of dir is we met a bug before.
		 else if(qName.equalsIgnoreCase("dirs")) {
			 movieTemp.setDirector(tempVal.toLowerCase());
			 
		 }
		 else if(qName.equalsIgnoreCase("cat")) {
			 if (tempVal!=null && tempVal.length()!=0) {
				 tempVal=tempVal.toLowerCase();
				 	genres.add(tempVal);
				 	movieTemp.addGenre(tempVal);
		    	 }
			 
			 
		 }
	 }
	 public void printData() {
	       
	        /*
	        for (String dir :dirs) {
	        	System.out.print(dir);
	        	System.out.print(", ");
	        }*/
	        for (Movie id: movies){

	            
	            
	            System.out.println(id );  


	        } 
	        for(String genre : genres) {
	        	
	        	System.out.print(genre);
	        	System.out.print("\n");
	        }
	        System.out.println("No of Movies '" + movies.size() + "'.");

	        System.out.print("\n");
	    }
	 public HashSet<String> getGenres(){
		 return this.genres;
	 }
	 public HashSet<Movie> getMovies(){
		 
		 return this.movies;
	 }
	 public HashSet<String> getMoviesID(){
		 
		 return this.movies_id;
	 }
//	 public static void main(String[] args) {
//	    	long tStart = System.currentTimeMillis(); 
//	        Movie_parse sp = new Movie_parse();
//	        sp.parseDocument();
//	        sp.printData();
//	        long tEnd = System.currentTimeMillis();
//	        long tDelta = tEnd - tStart;
//	        double elapsedSeconds = tDelta / 1000.0;
//	        System.out.println("time used: "+elapsedSeconds );
//	        
//	    }
}
