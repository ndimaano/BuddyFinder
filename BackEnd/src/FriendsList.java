import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/FriendList")
public class FriendsList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Run database server on mySQL Workbench 
    // Replace database credentials with your own in order to connect on local machine
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your-database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    
    	// Change the parameter to whatever is being passed from the frontend
        String username  = request.getParameter("username");

        // Call to database
        // Make sure to change the query if using something other than username (e.g. userID)
        List<String> friendList = getFriendUsernames(username);

        // Uses gson to convert to json so that Xcode can receive a json of the data
        String jsonFriendList = convertToJson(friendList);

        // Send the JSON response to Xcode frontend
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(jsonFriendList);
        out.flush();
        out.close();
        
        // Log the friend list for verification
        System.out.println(jsonFriendList);
    }

    private List<String> getFriendUsernames(String username) {
        List<String> friendUsernames = new ArrayList<>();
        
        // Query the friend table to find every friend of this user
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
            		 "SELECT u.username " +
                     "FROM friends f " +
                     "JOIN studentTable u ON (f.user2_id = u.studentID OR f.user1_id = u.studentID) " +
                     "WHERE f.user1_id = (SELECT studentID FROM studentTable WHERE username = ?) " +
                     "OR f.user2_id = (SELECT studentID FROM studentTable WHERE username = ?)")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String friendUsername = resultSet.getString("username");
                    friendUsernames.add(friendUsername);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return friendUsernames;
    }
    
    private String convertToJson(List<String> friendUsernames) {
		Gson gson = new Gson();
		return gson.toJson(friendUsernames);
    }
}
