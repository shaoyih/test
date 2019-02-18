package Parsing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Movie {
	private String title;
	private List<String> genres;
	private String director;
	private int year;
	private String id;
	public Movie() {
		genres= new ArrayList<String>();
	}
	public Movie(String t, String d, int y) {
		this.title=t;
		this.director=d;
		this.year=y;
		genres= new ArrayList<String>();
		id="";
	}
	
	public void setTitle(String title) {
		this.title=title;
	}
	public void setYear(int year) {
		this.year=year;
	}
	public void setId(String id) {
		this.id=id;
	}
	public String getId() {
		return this.id;
	}
	public void setDirector(String director) {
		this.director=director;
	}
	public void addGenre(String genre) {
		this.genres.add(genre);
	}
	public String getTitle( ) {
		return this.title;
	}
	public int getYear( ) {
		return this.year; 
	}
	public String getDirector( ) {
		return this.director; 
	}
	public List<String> getGenre( ) {
		return this.genres; 
	}
	@Override
	public boolean equals(Object o) {
 
		
		if (o == null) {
			return false;
		}
 
		// this instance check
		if ((o instanceof Movie)) {
			return ((Movie) o).getDirector().equals(this.getDirector())&& ((Movie) o).getYear()==this.getYear()
					&&((Movie) o).getTitle().equals(this.getTitle());
		}
 
		return false;
	}
 
	@Override
	public int hashCode() {
		return (this.getDirector()+this.getTitle()+Integer.toString(this.getYear())).hashCode();
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Movie Details - ");
		sb.append("title:" + getTitle());
		sb.append(",");
		sb.append("id:" + getId());
		sb.append(",");
		sb.append("year:"+getYear());
		sb.append(",");
		sb.append("director:" + getDirector());
		sb.append(", ");
		sb.append(" genres:");
		List<String> genres=getGenre();
		for (String genre : genres) {
			sb.append(genre);
			sb.append(", ");
		}
		
		sb.append(".");
		
		return sb.toString();
	}
//	 public static void main(String[] args) {
//		 HashSet<Movie> h = new HashSet<Movie>(); 
//		  Movie e= new Movie("1","11",1);
//		  Movie e1= new Movie("1","11",1);
//		  Movie e2= new Movie("1","11",1);
//		  Movie e3= new Movie("1","11",1);
//		  Movie a= new Movie("1","1",1);
//		  
//	        // Adding elements into HashSet usind add() 
//	        h.add(e); 
//	        h.add(a); 
//	        h.add(e1); 
//	        h.add(e2); 
//	        h.add(e3); 
//	       
//	  
//	        // Displaying the HashSet 
//	        System.out.println(h); 
//	 }
	
}
