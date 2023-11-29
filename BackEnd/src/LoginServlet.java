package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super(); 
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException { 
        PrintWriter out = response.getWriter(); 
        String username = request.getParameter("login-username"); 
        String password = request.getParameter("login-password"); 
        System.out.println(username + " " + password);

        try (Connection conn = JDBCConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HttpSession session = request.getSession();
                Cookie sessionCookie = StudentTracker.setUserSession(username, session);
                response.addCookie(sessionCookie);
                response.sendRedirect("index.html"); // Ensure this is the correct redirect
                out.println("Successfully logged in");
            } else {
                response.setContentType("text/html");
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Invalid credentials');");
                out.println("</script>");
                System.out.println("Invalid credentials: " + username + " " + password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider more comprehensive error handling
        } finally {
            out.flush();
            out.close();
        }
    }
}
