package Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/SignUp")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String JDBC_URL = "jdbc:mysql://localhost/BuddyFinder";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "NeitherStock1@3";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	
    	
    	BufferedReader reader = request.getReader();
    	JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

    	String username = json.get("username").getAsString();
    	String password = json.get("password").getAsString();
    	String email = json.get("email").getAsString();
         
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        System.out.println(username);
        System.out.println(email);
        System.out.println(password);
        
        Connection conn;
		try {
			//conn = DriverManager.getConnection("jdbc:mysql://localhost/BuddyFinder?user=root&password=NeitherStock1@3");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/BuddyFinder?user=root&password=NeitherStock1@3");
			 PreparedStatement stmt = conn.prepareStatement("INSERT INTO studentTable (username, email, password) VALUES (?, ?, ?)");
	            stmt.setString(1, username);
	            stmt.setString(2, email);
	            stmt.setString(3, password);

	            int rowsAffected = stmt.executeUpdate();
	            JsonObject jsonResponse = new JsonObject();
	            jsonResponse.addProperty("status", rowsAffected > 0 ? "success" : "failure");
	            System.out.println(new Gson().toJson(jsonResponse));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//       try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
//            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
//            stmt.setString(1, username);
//            stmt.setString(2, email);
//            stmt.setString(3, password);
//
//            int rowsAffected = stmt.executeUpdate();
//            JsonObject jsonResponse = new JsonObject();
//            jsonResponse.addProperty("status", rowsAffected > 0 ? "success" : "failure");
//            out.println(new Gson().toJson(jsonResponse));
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JsonObject jsonResponse = new JsonObject();
//            jsonResponse.addProperty("status", "error");
//            out.println(new Gson().toJson(jsonResponse));
//        }
    }
}
