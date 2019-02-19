import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class UpdateSecurePassword {

    /*
     * 
     * This program updates your existing moviedb customers table to change the
     * plain text passwords to encrypted passwords.
     * 
     * You should only run this program **once**, because this program uses the
     * existing passwords as real passwords, then replace them. If you run it more
     * than once, it will treat the encrypted passwords as real passwords and
     * generate wrong values.
     * 
     */
    public static void main(String[] args) throws Exception {

        String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        Statement statement = connection.createStatement();

        String alterEmployee = "ALTER TABLE employees MODIFY COLUMN password VARCHAR(128)";
        String alterCustomer = "ALTER TABLE customers MODIFY COLUMN password VARCHAR(128)";
        int alterResult = statement.executeUpdate(alterEmployee);
        int alterResult1 = statement.executeUpdate(alterCustomer);
        System.out.println("altering customers table schema completed, " + alterResult1 + " rows affected");

       
        String query = "SELECT email, password from employees";
        String query1 = "SELECT id, password from customers";

        ResultSet rs = statement.executeQuery(query);
       
      
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        ArrayList<String> updateQueryList = new ArrayList<>();

        System.out.println("encrypting password (this might take a while)");
        while (rs.next()) {

            String email = rs.getString("email");
            String password = rs.getString("password");
        
            
            String encryptedPassword = passwordEncryptor.encryptPassword(password);

            
            System.out.println(email);
            System.out.println(encryptedPassword);
            String updateQuery = String.format("UPDATE employees SET password='%s' WHERE email='%s';", encryptedPassword,
                    email);
            updateQueryList.add(updateQuery);
        }
        rs.close();
        ResultSet rs1 = statement.executeQuery(query1);
        while (rs1.next()) {
            // get the ID and plain text password from current table
            String id = rs1.getString("id");
            String password = rs1.getString("password");
            
            // encrypt the password using StrongPasswordEncryptor
            String encryptedPassword = passwordEncryptor.encryptPassword(password);

            // generate the update query
            String updateQuery = String.format("UPDATE customers SET password='%s' WHERE id=%s;", encryptedPassword,
                    id);
            updateQueryList.add(updateQuery);
        }
        rs1.close();

        
        System.out.println("updating password");
        int count = 0;
        for (String updateQuery : updateQueryList) {
        	System.out.println(updateQuery);
            int updateResult = statement.executeUpdate(updateQuery);
            count += updateResult;
        }
        System.out.println("updating password completed, " + count + " rows affected");

        statement.close();
        connection.close();

        System.out.println("finished");

    }

}
