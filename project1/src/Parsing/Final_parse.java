package Parsing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Final_parse {
	private static HashSet<String> genres;
	private static HashSet<Movie> movies;
	private static HashMap<String,Integer> genresMap;
//	private static HashMap<String,List<Star>> stars;

	private static void getDataFromMs() {
		Movie_parse mp = new Movie_parse();
//		SIM_parse sp = new SIM_parse();
        mp.parseDocument();
        genres=mp.getGenres();
        
//        stars=sp.parseDocument();
	}
	private static void putIntoGenres(Connection conn) throws SQLException{
		Statement select = conn.createStatement();
		int maxR=0;
        ResultSet result = select.executeQuery("Select * from genres");
        while(result.next()) {
        	int id=result.getInt("id");
        	genresMap.put(result.getString("name"),id);
        	if(id >maxR) {
        		maxR=id;
        	}
        }
        for(String genre:genres) {
        	if(genresMap.get(genre)==null && genre!="") {
        		genresMap.put(genre,++maxR);
        	}
        }
	}
	private static void getGenreList(Connection conn) throws SQLException{
		genresMap=new HashMap<>();
		Statement select = conn.createStatement();
		int maxR=0;
        ResultSet result = select.executeQuery("Select * from genres");
        while(result.next()) {
        	int id=result.getInt("id");
        	genresMap.put(result.getString("name").toLowerCase(),id);
        	
        }
        
	}
	
	private static void loadGenres(Connection conn) {
		PreparedStatement psInsertRecord=null;
		int[] iNoRows=null;
		String sqlInsertRecord="insert into genres (id, name) select ?,? From dual WHERE NOT EXISTS(SELECT * FROM genres WHERE name=?)";
		try {
			conn.setAutoCommit(false);

            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            for(String name :genresMap.keySet()) {
            	psInsertRecord.setInt(1, genresMap.get(name));
            	psInsertRecord.setString(2, name);
            	psInsertRecord.setString(3, name);
            	psInsertRecord.addBatch();
            }
            iNoRows=psInsertRecord.executeBatch();
			conn.commit();
		} catch (SQLException e) {
   		 e.printStackTrace();
   	}
            
		 try {
			 if(psInsertRecord!=null) psInsertRecord.close();
    	} catch (SQLException e) {
    		 e.printStackTrace();
    	}
		

	}
	public  HashMap<String,Integer> getGenres() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		//counter
    	long tStart = System.currentTimeMillis(); 
    	getDataFromMs();
    	
    	Connection conn = null;
    	Class.forName("com.mysql.jdbc.Driver").newInstance();
    	String jdbcURL="jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
    	
    	 try {
    		 conn = DriverManager.getConnection(jdbcURL,"mytestuser", "mypassword");
    	} catch (SQLException e) {
    		 e.printStackTrace();
    	}
    	 genresMap=new HashMap<>();
    	 putIntoGenres(conn);
    	 
    	 loadGenres(conn);
    	 getGenreList(conn);
        
  /*      StarParse sp = new Star_parse();
        sp.parseDocument();
        sp.printData();
        
        SIM_parse sim = new SIM_parse();
        sim.parseDocument();
        sim.printData();*/
    	 
    	 
    	 try {
             
             if(conn!=null) conn.close();
         } catch(Exception e) {
             e.printStackTrace();
         }
     	
    	 return genresMap;
//        
//        //counter
//        long tEnd = System.currentTimeMillis();
//        long tDelta = tEnd - tStart;
//        double elapsedSeconds = tDelta / 1000.0;
//        System.out.println("time used: "+elapsedSeconds );
        
       
	}

}
