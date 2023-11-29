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

@WebServlet("/RemoveFriend")
public class RemoveFriend extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Run database server on mySQL Workbench 
    // Replace database credentials with your own in order to connect on local machine
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your-database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        // Get the username and friend username from the request parameter 
    	// (assuming it's passed as "username" for current logged-in user
    	// and "friend" for friend that is to be deleted)
        String username = request.getParameter("username");
        String friend = request.getParameter("friend");

        // Remove the friend relationship from the database
        boolean success = removeFriendRelationship(username, friend);

        // Response is a plain string, change to json if needed
        // make sure to handle on front end based on success or failure
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        if (success) {
        	out.println("success");
            System.out.println("Friend relationship removed successfully for user: " + username);
        } else {
        	out.println("failure");
            System.out.println("Failed to remove friend relationship for user: " + username);
        }
    }

    private boolean removeFriendRelationship(String currentUserUsername, String friendToRemoveUsername) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM friends " +
                             "WHERE (user1_id = (SELECT studentID FROM studentTable WHERE username = ?) " +
                             "AND user2_id = (SELECT studentID FROM studentTable WHERE username = ?)) " +
                             "OR (user1_id = (SELECT studentID FROM studentTable WHERE username = ?) " +
                             "AND user2_id = (SELECT studentID FROM studentTable WHERE username = ?))")) {

            preparedStatement.setString(1, currentUserUsername);
            preparedStatement.setString(2, friendToRemoveUsername);
            preparedStatement.setString(3, friendToRemoveUsername);
            preparedStatement.setString(4, currentUserUsername);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
    }
}

