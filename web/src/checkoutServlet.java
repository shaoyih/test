import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import java.util.Random;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@WebServlet(name = "checkoutServlet", urlPatterns = "/project1/checkout")
public class checkoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String cardNum = request.getParameter("cardNum");
        String date = request.getParameter("date");
        System.out.println("here");
        Connection dbcon = null;
        try {
        	
            
        	Context initCtx = new InitialContext();
        	
        	//checking for env
            Context env = (Context) initCtx.lookup("java:comp/env");
            if (env == null) {
            System.out.println("ds is null");
            }
            
            db_source dbs=new db_source(request.getRequestURL().toString());
        	DataSource ds=dbs.getSource();
        	dbcon = ds.getConnection();
        	
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NamingException e) {
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
        
            
      
        String query = ("select id from customers \n" + 
                    "where (ccID LIKE ? ) and firstName= ? and lastName= ? ;");
            
            PreparedStatement prepare = dbcon.prepareStatement(query);
            prepare.setString(1,cardNum);
            prepare.setString(2,firstName);
            prepare.setString(3,lastName);
            ResultSet rs=prepare.executeQuery();
        
            
        if (rs.next() ) customer_id=rs.getInt("id"); 
        
        JsonObject totalJson = new JsonObject();
        
        
        
        
        
        
        
        HttpSession session = request.getSession();
        HashMap<String,Integer> previousItems = (HashMap<String,Integer>) session.getAttribute("previousItems");
        
        System.out.println(customer_id);
        System.out.println(todayDate);
        for (String key : previousItems.keySet()) {
            String movieId="";
            
            
            
    
            String query1 = ("select id from movies\n" + 
                        "where title=?;");
            PreparedStatement prepare1 = dbcon.prepareStatement(query1);
            prepare1.setString(1,key);
            
            ResultSet rs1=prepare1.executeQuery();
        
            
            
            if (rs1.next() ) movieId=rs1.getString("id"); 
            
            int value=previousItems.get(key);
            JsonArray innerArray = new JsonArray();
            dbcon.setReadOnly(false);
            for (int i=0;i<value;i++) {
                long saleId = -1L;

                String query11 = "INSERT INTO sales VALUES (default, ?, ?,?);";
                PreparedStatement prepare11 = dbcon.prepareStatement(query11,Statement.RETURN_GENERATED_KEYS);
                prepare11.setInt(1,customer_id);
                prepare11.setString(2,movieId);
                prepare11.setString(3,todayDate);
                
            
                prepare11.executeUpdate();
                
                
                
                
                ResultSet rs2 = prepare11.getGeneratedKeys();
                if (rs2 != null && rs2.next()) {
                    saleId = rs2.getLong(1);
                }

                innerArray.add(saleId);
            }
            dbcon.setReadOnly(true);
            totalJson.add(key, innerArray);
            
        }
        return totalJson;
        
    }
    private boolean loginSucceed(Connection dbcon, String firstName,String lastName,String cardNum,String date) {
         
         cardNum= cardNum.replaceAll("....(?!$)", "$0%");
         System.out.println(cardNum);
        try {
            
            
            String query = ("select count(*) from creditcards \n" + 
                    "where (id LIKE ?) and firstName= ? and lastName= ? AND expiration= ? ;");
            PreparedStatement prepare = dbcon.prepareStatement(query);
            prepare.setString(1,cardNum);
            prepare.setString(2,firstName);
            prepare.setString(3,lastName);
            prepare.setString(4,date);
        
            ResultSet rs=prepare.executeQuery();
            
            
            
            if (rs.next() && rs.getInt("count(*)")>0) return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

  
        return false;
    }
}

    
