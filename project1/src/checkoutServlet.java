import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@WebServlet(name = "checkoutServlet", urlPatterns = "/project1/checkout")
public class checkoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String cardNum = request.getParameter("cardNum");
        String date = request.getParameter("date");
        System.out.println("here");
        Connection dbcon = null;
		try {
			dbcon = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (this.loginSucceed(dbcon, firstName,lastName,cardNum,date)) {
            
            JsonObject total=null;
        	try {
				total=updateSales(request, dbcon,firstName,lastName,cardNum);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");
            responseJsonObject.add("data", total);

            response.getWriter().write(responseJsonObject.toString());
        } else {
            // Login fails
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            
            response.getWriter().write(responseJsonObject.toString());
        }
    }
    private JsonObject updateSales(HttpServletRequest request,Connection dbcon, String firstName,String lastName,String cardNum) throws SQLException {
    	//get customer id
    	//get movieId
    	//INSERT INTO sales VALUES (DEfault, 490001, 'tt0357987','2005/10/21');
    	
    	cardNum= cardNum.replaceAll("....(?!$)", "$0%");
    	int customer_id=0;
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date();
    	String todayDate=dateFormat.format(date);
    	
			
		Statement statement = dbcon.createStatement();
		String query = ("select id from customers \n" + 
					"where (ccID LIKE '"+cardNum+"') and firstName='"+firstName+
					"' and lastName='"+lastName+"';");
			ResultSet rs = statement.executeQuery(query);
			
		if (rs.next() ) customer_id=rs.getInt("id"); 
		
		JsonObject totalJson = new JsonObject();
		
		
		
		
		
		
    	
    	HttpSession session = request.getSession();
    	HashMap<String,Integer> previousItems = (HashMap<String,Integer>) session.getAttribute("previousItems");
    	
    	System.out.println(customer_id);
    	System.out.println(todayDate);
        for (String key : previousItems.keySet()) {
        	String movieId="";
        	
        	
        	
    		Statement statement1 = dbcon.createStatement();
    		String query1 = ("select id from movies\n" + 
    					"where title='"+key+"';");
    		ResultSet rs1 = statement1.executeQuery(query1);
    		
            
    	    if (rs1.next() ) movieId=rs1.getString("id"); 
    		
        	int value=previousItems.get(key);
        	JsonArray innerArray = new JsonArray();
        	for (int i=0;i<value;i++) {
        		long saleId = -1L;
        		Statement update = dbcon.createStatement();
                update.executeUpdate("INSERT INTO sales VALUES (default, "+customer_id+", "
                		+ "'"+movieId+"','"+todayDate+"');",Statement.RETURN_GENERATED_KEYS);
                ResultSet rs2 = update.getGeneratedKeys();
                if (rs2 != null && rs2.next()) {
                    saleId = rs2.getLong(1);
                }

                innerArray.add(saleId);
        	}
        	totalJson.add(key, innerArray);
        	
        }
        return totalJson;
        
    }
    private boolean loginSucceed(Connection dbcon, String firstName,String lastName,String cardNum,String date) {
    	 
    	 cardNum= cardNum.replaceAll("....(?!$)", "$0%");
    	 System.out.println(cardNum);
		try {
			
			Statement statement = dbcon.createStatement();
			String query = ("select count(*) from creditcards \n" + 
					"where (id LIKE '"+cardNum+"') and firstName='"+firstName+
					"' and lastName='"+lastName+"' AND expiration='"+date+"' ;");
			ResultSet rs = statement.executeQuery(query);
			
			if (rs.next() && rs.getInt("count(*)")>0) return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  
    	return false;
    }
}

    
