package Parsing;
import java.io.IOException;
import java.util.HashMap; 

import java.util.ArrayList;

import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SIM_parse extends DefaultHandler {
	private HashMap<String,List<String>> SIM;
	private String tempVal;
	private String movieId;
	
	
	public SIM_parse() {
		SIM= new HashMap<>();
		
	}
	public void parseDocument() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse("casts124.xml", this);

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
	        
	    }

	 public void endElement(String uri, String localName, String qName) throws SAXException {
		 
		 if (qName.equalsIgnoreCase("f")) {
			 	movieId=tempVal;
			 	System.out.println(tempVal);
			 	if(SIM.get(movieId)==null) {
			 		SIM.put(movieId,new ArrayList<String>());
			 	}
		 }
		 else if (qName.equalsIgnoreCase("a")) {
			 	SIM.get(movieId).add(tempVal);
		 }
		 
		 		
	 }
	 public void characters(char[] ch, int start, int length) throws SAXException {
	        tempVal = new String(ch, start, length);
	    }
	 public void printData() {
	       
	       
	        for (String id: SIM.keySet()){

	            
	            String value = SIM.get(id).toString();  
	            System.out.println(id + " " + value);  


	        } 
	        System.out.println("No of Movies '" + SIM.size() + "'.");

	        System.out.print("\n");
	    }
	 /*
	 public static void main(String[] args) {
	    	long tStart = System.currentTimeMillis(); 
	        SIM_parse sp = new SIM_parse();
	        sp.parseDocument();
	        sp.printData();
	        long tEnd = System.currentTimeMillis();
	        long tDelta = tEnd - tStart;
	        double elapsedSeconds = tDelta / 1000.0;
	        System.out.println("time used: "+elapsedSeconds );
	    }
	    */
	
	
	
	

}
