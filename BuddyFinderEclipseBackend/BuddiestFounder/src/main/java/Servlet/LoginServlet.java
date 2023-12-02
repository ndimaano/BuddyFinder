package Servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    //private static final String JDBC_URL = "jdbc:mysql://localhost/buddyFinder?user=root&password=root";
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        BufferedReader reader = request.getReader();
    	JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

    	String username = json.get("username").getAsString();
    	String password = json.get("password").getAsString();
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	JsonObject jsonResponse = new JsonObject();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/BuddyFinder?user=root&password=NeitherStock1@3");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM studentTable WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

       
            if (rs.next()) {
                jsonResponse.addProperty("status", "success");
                
            } else {
                jsonResponse.addProperty("status", "failure");
                
            }
            System.out.println(new Gson().toJson(jsonResponse));
        } catch (SQLException e) {
            e.printStackTrace();
            jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", "error");
            System.out.println(new Gson().toJson(jsonResponse));
        }
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}
