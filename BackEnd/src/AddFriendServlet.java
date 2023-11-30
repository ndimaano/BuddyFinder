package Servlets;

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

@WebServlet("/AddFriend")
public class AddFriend extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials and URL
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your-database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        // Get the usernames of the two users who are to become friends
        String username = request.getParameter("username");
        String friend = request.getParameter("friend");

        // Add the friend relationship to the database
        boolean success = addFriendRelationship(username, friend);

        // Set response content type and get writer
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        if (success) {
            out.println("success");
            System.out.println("Friend relationship added successfully for user: " + username);
        } else {
            out.println("failure");
            System.out.println("Failed to add friend relationship for user: " + username);
        }
    }

    private boolean addFriendRelationship(String currentUserUsername, String friendToAddUsername) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO friends (user1_id, user2_id) " +
                             "VALUES ((SELECT studentID FROM studentTable WHERE username = ?), " +
                             "(SELECT studentID FROM studentTable WHERE username = ?))")) {

            preparedStatement.setString(1, currentUserUsername);
            preparedStatement.setString(2, friendToAddUsername);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
    }
}
