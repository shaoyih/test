package Parsing;

import java.util.ArrayList;
import java.util.List;

public class Movie {
	private String title;
	private List<String> genres;
	private String director;
	private int year;
	
	public Movie() {
		genres= new ArrayList<String>();
	}
	public Movie(String t, List<String> g, String d, int y) {
		this.title=t;
		this.director=d;
		this.year=y;
	}
	
	public void setTitle(String title) {
		this.title=title;
	}
	public void setYear(int year) {
		this.year=year;
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
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Movie Details - ");
		sb.append("title:" + getTitle());
		sb.append(",");
		sb.append("year:"+getYear());
		sb.append(",");
		sb.append("director:" + getDirector());
		sb.append(",\n");
		sb.append("genres:\n");
		List<String> genres=getGenre();
		for (String genre : genres) {
			sb.append(genre);
			sb.append(", ");
		}
		
		sb.append(".");
		
		return sb.toString();
	}
	
	
}
