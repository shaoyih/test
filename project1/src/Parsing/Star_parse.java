package Parsing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class Star_parse extends DefaultHandler {
	HashMap<String,Star> stars;
	private String tempVal;
	private Star starTemp;
	
	//initialize class 
	public Star_parse() {
        stars = new HashMap<>();
    }
	
	 public HashMap<String,Star> parseDocument() {
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        try {
	            SAXParser sp = spf.newSAXParser();
	            sp.parse("actors63.xml", this);

	        } catch (SAXException se) {
	            se.printStackTrace();
	        } catch (ParserConfigurationException pce) {
	            pce.printStackTrace();
	        } catch (IOException ie) {
	            ie.printStackTrace();
	        }
	        return stars;
	    }
	    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	        //reset
	        tempVal = "";
	        if (qName.equalsIgnoreCase("actor")) {
	            //create a new instance of employee
	            starTemp = new Star();
	       
	        }
	    }

	    public void characters(char[] ch, int start, int length) throws SAXException {
	        tempVal = new String(ch, start, length);
	    }

	    public void endElement(String uri, String localName, String qName) throws SAXException {

	        if (qName.equalsIgnoreCase("actor")) {
	            //add it to the list
	            stars.put(starTemp.getName(),starTemp);

	        } else if (qName.equalsIgnoreCase("stagename")) {
	            starTemp.setName(tempVal);
	        } else if (qName.equalsIgnoreCase("dob")) {
	        	if(isYear(tempVal)) {
	            starTemp.setDob(Integer.parseInt(tempVal));
	        	}
	        	else {
		            starTemp.setDob(0);
	        	}
	        }

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
	    public void printData() {

	    	for (String id: stars.keySet()){

	            
	            String value = stars.get(id).toString();  
	            System.out.println(id + " " + value);  


	        } 

	        
	        System.out.println("No of Stars '" + stars.size() + "'.");
	    }
	    public HashMap<String,Star> returnData() {

	    	return stars;
	    }
	    
//	   
//	    public static void main(String[] args) {
//	    	long tStart = System.currentTimeMillis(); 
//	        Star_parse sp = new Star_parse();
//	        sp.parseDocument();
//	        sp.printData();
//	        long tEnd = System.currentTimeMillis();
//	        long tDelta = tEnd - tStart;
//	        double elapsedSeconds = tDelta / 1000.0;
//	        System.out.println("time used: "+elapsedSeconds );
//	    }
//	    

}
