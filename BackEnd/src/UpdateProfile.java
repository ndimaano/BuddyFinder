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

@WebServlet("/UpdateProfile")
public class UpdateProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Extract student data
        String email = request.getParameter("email"); // Assuming email is unique for each student
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String newPassword = request.getParameter("password"); // Ensure proper password handling

        try (Connection conn = JDBCConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE studentTable SET name = ?, username = ?, password = ? WHERE email = ?");
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, BCrypt.hashpw(newPassword, BCrypt.gensalt())); // Hashing the password
            stmt.setString(4, email);
            int rowsAffected = stmt.executeUpdate();

            if(rowsAffected > 0) {
                response.getWriter().write("Profile updated successfully");
            } else {
                response.getWriter().write("Profile update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
    }
}

