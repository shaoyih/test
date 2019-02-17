package Parsing;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map; 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Movie_parse extends DefaultHandler{
	private HashMap<String,Movie> movies;
	private String tempVal;
	private Movie movieTemp;
	private String movieId;
	private HashSet<String> genres;
	
	public Movie_parse() {
		movies=new HashMap<>();
		genres=new HashSet<>();
	}
	public void parseDocument() {
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
		 if (qName.equalsIgnoreCase("film")) {
	            //add it to the hashmap
	            movies.put(movieId,movieTemp);
	    }
		 else if (qName.equalsIgnoreCase("fid")) {
			 	movieId=tempVal;
			 	//System.out.println(tempVal);
		 }
		 else if (qName.equalsIgnoreCase("t")) {
			 	movieTemp.setTitle(tempVal);
		 }
		 else if(qName.equalsIgnoreCase("year")) {
			 if(isYear(tempVal)) {
		         movieTemp.setYear(Integer.parseInt(tempVal));
		      } 	
		 }
		 //for director we only require one name so we set the last as our final director, reason use dirs instead of dir is we met a bug before.
		 else if(qName.equalsIgnoreCase("dirs")) {
			 movieTemp.setDirector(tempVal);
			 
		 }
		 else if(qName.equalsIgnoreCase("cat")) {
			 genres.add(tempVal);
			 movieTemp.addGenre(tempVal);
		 }
	 }
	 public void printData() {
	       
	        /*
	        for (String dir :dirs) {
	        	System.out.print(dir);
	        	System.out.print(", ");
	        }*/
	        for (String id: movies.keySet()){

	            
	            String value = movies.get(id).toString();  
	            System.out.println(id + " " + value);  


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
	 public HashMap<String,Movie> getMovies(){
		 return this.movies;
	 }
	/* public static void main(String[] args) {
	    	long tStart = System.currentTimeMillis(); 
	        Movie_parse sp = new Movie_parse();
	        sp.parseDocument();
	        sp.printData();
	        long tEnd = System.currentTimeMillis();
	        long tDelta = tEnd - tStart;
	        double elapsedSeconds = tDelta / 1000.0;
	        System.out.println("time used: "+elapsedSeconds );
	        
	    }*/
}
