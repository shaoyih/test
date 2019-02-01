import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * This IndexServlet is declared in the web annotation below, 
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "IndexServlet", urlPatterns = "/project1/shoppingCart")
public class shoppingCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * handles POST requests to store session information
     */
    

    /**
     * handles GET requests to add and show the item list information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String mode = request.getParameter("mode");
        String result;
        if (mode==null) {
        	result=firstComeSession(request,response);
        	
        }
        else {
        	result=updateSession(request,response);
        }
        
        
        
        response.getWriter().write(result);
    }
    private String firstComeSession(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	String item = request.getParameter("movie");
        // get the previous items in a ArrayList
        HashMap<String,Integer> previousItems = (HashMap<String,Integer>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new HashMap<>();
            previousItems.put(item,1);
            session.setAttribute("previousItems", previousItems);
        } else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            synchronized (previousItems) {
            	if (previousItems.containsKey(item)) {
            		int count=previousItems.get(item);
            		previousItems.put(item, count+1);
            		
            	}
            	else {
            		previousItems.put(item, 1);
            	}
            }
        }
        JsonArray jsonArray = new JsonArray();
        for (String key : previousItems.keySet()) {
        	JsonArray jsonObject = new JsonArray();
            jsonObject.add(key);
            jsonObject.add(previousItems.get(key));
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }
    private String updateSession(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	String item = request.getParameter("movie");
    	
    	String value = request.getParameter("value");
    	HashMap<String,Integer> previousItems = (HashMap<String,Integer>) session.getAttribute("previousItems");
    	synchronized (previousItems) {
        	if (previousItems.containsKey(item)) {
        		System.out.println(value);
        		int temp=Integer.parseInt(value);
        		if (temp==0) {
        			System.out.println(item);
        			previousItems.remove(item);
        		}
        		else{
        			previousItems.put(item, temp);
        		}
        		
        	}
        	
    	}
    	JsonArray jsonArray = new JsonArray();
        for (String key : previousItems.keySet()) {
        	System.out.println(key);
        	JsonArray jsonObject = new JsonArray();
            jsonObject.add(key);
            jsonObject.add(previousItems.get(key));
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    	
    }
}
