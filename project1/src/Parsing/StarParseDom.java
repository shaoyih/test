package Parsing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StarParseDom {
	List<Star> stars;
    Document dom;
    public StarParseDom () {
    	stars=new ArrayList<>();
    }
    private void parseXmlFile() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("actors63.xml");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    private void parseDoc() {
    	Element docEle = dom.getDocumentElement();
    	NodeList nl = docEle.getElementsByTagName("actor");
    	if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                Star s1 = getStar(el);
                stars.add(s1);
            }
        }
    }
    private Star getStar(Element e1) {
    	String name=getTextValue(e1,"stagename");
    	int dob= getIntValue(e1,"dob");
    	
    	Star s=new Star(name,dob);
    	return s;
    }
    
    //The parts used to get the tag in each element tree
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            if(el.hasChildNodes()) {
            textVal = el.getFirstChild().getNodeValue();
            }
            
        }
        return textVal;
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
    private int getIntValue(Element ele, String tagName) {
    	String result=getTextValue(ele, tagName);
        if(isYear(result)) {
        return Integer.parseInt(result);
        }
        return 0;
    }
    private void printData() {

        

        Iterator<Star> it = stars.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
        System.out.println("No of Employees '" + stars.size() + "'.");
    }
    
    public static void main(String[] args) {
    	long tStart = System.currentTimeMillis(); 
    	StarParseDom spd= new StarParseDom();
    	spd.parseXmlFile();
    	spd.parseDoc();
    	spd.printData();
    	long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        System.out.println("time used: "+elapsedSeconds );
    
    }
}
