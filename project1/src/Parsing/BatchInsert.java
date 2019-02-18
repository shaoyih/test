package Parsing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.annotation.Resource;

public class BatchInsert {
	private static HashSet<Movie> movies;
	private static HashMap<String,Integer> genresMap;
	private static HashMap<String,List<Star>> stars;
	
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

	 private Connection dbcon ;
	 
	 public BatchInsert() {
		 try {
				Final_parse gp = new Final_parse();
				genresMap=gp.getGenres();
				
				SIM_parse sp = new SIM_parse();
				stars=sp.parseDocument();
				Movie_parse mp = new Movie_parse();
				mp.parseDocument();
				movies=mp.getMovies();
		        

			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
	    	try {
				dbcon = DriverManager.getConnection(jdbcURL,"mytestuser", "mypassword");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	
	private  void batchInsert() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PreparedStatement psInsertRecord=null;
    	String sqlInsertRecord="INSERT INTO movies (id, title, year, director)\n" + 
    			"VALUES (?,?,?,?);";
    	int[] iNoRows;
    	
    	
    	
    
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			
		     
			dbcon.setAutoCommit(false);
		
		    psInsertRecord=dbcon.prepareStatement(sqlInsertRecord);
		    Iterator it = movies.iterator();
	        while (it.hasNext()) 
		   	     {
	        	 	System.out.println("starting to insert");
	        	 	Movie movie=(Movie) it.next();
	        	 		String id=movie.getId();
	        	 		String title=movie.getTitle();
	        	 		int year=movie.getYear();
	        	 		String director=movie.getDirector();
	        	 		
	        	 		    if (id=="") {
	        	 		    	System.out.println(movie);
	        	 		    	break;
	        	 		    }
	        	 			
			    		    psInsertRecord.setString(1, id);
			    		    psInsertRecord.setString(2, title);
			    		    psInsertRecord.setInt(3, year);
			    		    psInsertRecord.setString(4, director);
			    		   
			    		    psInsertRecord.addBatch();
	        	 	
		    	 }
		    		
		    iNoRows=psInsertRecord.executeBatch();
		    dbcon.commit();
    	
		    psInsertRecord.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
    	
		
		
	}
	
	 public static void main(String[] args) {
		long tStart = System.currentTimeMillis(); 
		BatchInsert sp = new BatchInsert();
//		System.out.println(movies);
//		System.out.println(stars);
//		System.out.println(genresMap);
		try {
			sp.batchInsert();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    long tEnd = System.currentTimeMillis();
	    long tDelta = tEnd - tStart;
	    double elapsedSeconds = tDelta / 1000.0;
	    System.out.println("time used: "+elapsedSeconds );
}

	

}
