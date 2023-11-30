package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/AddFriend")
public class AddFriendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddFriendServlet() {
        super(); 
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // Extract user IDs of the two friends
        int user1Id = Integer.parseInt(request.getParameter("user1Id"));
        int user2Id = Integer.parseInt(request.getParameter("user2Id"));

        try (Connection conn = JDBCConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO friends (user1_id, user2_id) VALUES (?, ?)");
            stmt.setInt(1, user1Id);
            stmt.setInt(2, user2Id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}