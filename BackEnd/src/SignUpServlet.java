import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/SignUp")
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SignUpServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String username = request.getParameter("signup-username");
        String email = request.getParameter("signup-email");
        String password = request.getParameter("signup-password");

        if (!ifStudentExists(email, username)) {
            try {
                JDBCConnector.insertUser(email, username, password);
                HttpSession session = request.getSession();
                Cookie sessionCookie = StudentTracker.setUserSession(username, session);
                response.addCookie(sessionCookie);
                response.sendRedirect("index.html"); // Redirect to a meaningful page
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("Error during sign up: " + e.getMessage());
            }
        } else {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            out.println("User already exists");
        }

        out.flush();
        out.close();
    }

    private boolean ifStudentExists(String email, String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCConnector.getConnection();
            //  check duplicate email
            stmt = conn.prepareStatement("SELECT email FROM users WHERE email = ?");
            stmt.setString(1, email);
            if (stmt.executeQuery().next()) {
                return true;
            }
            stmt.close();
            // check duplicate username
            stmt = conn.prepareStatement("SELECT username FROM users WHERE username = ?");
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
