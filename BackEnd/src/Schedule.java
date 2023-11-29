import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/Schedule")
public class Schedule extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Run database server on mySQL Workbench 
    // Replace database credentials with your own in order to connect on local machine
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your-database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	// Assumes request parameter is username (as it should be)
    	String username = request.getParameter("username");
    	
    	// Querying database for schedule of user with given username
    	Map<String, String> userSchedule = getUserSchedule(username);
    	
    	String jsonSchedule = convertToJson(userSchedule);
    	
    	response.setContentType("application/json");
    	PrintWriter out = response.getWriter();
    	out.println(jsonSchedule);
    	// Log schedule for verification
    	System.out.println(jsonSchedule);
	}
    
    private Map<String, String> getUserSchedule(String username) {
        Map<String, String> userSchedule = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT day_of_week, GROUP_CONCAT(time_slot ORDER BY time_slot) AS schedule " +
                             "FROM scheduleTable " +
                             "WHERE studentID = (SELECT studentID FROM studentTable WHERE username = ?) " +
                             "GROUP BY day_of_week")) {

            preparedStatement.setString(1, username);

            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String dayOfWeek = resultSet.getString("day_of_week");
                    
                    // Schedule is in concatenated string format where each
                    // 30 min timeslot is signified by the start time of the slot
                    
                    // e.g. if a user has timeslots 700, 1200, 1600, and 1630
                    // then the string will be "700,1200,1600,1630"
                    // which can then be parsed 
                    
                    // These strings are put in the map by their corresponding days of the week
                    String schedule = resultSet.getString("schedule");
                    userSchedule.put(dayOfWeek, schedule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return userSchedule;
    }

    private String convertToJson(Map<String, String> userSchedule) {
        // Assuming you have Gson in your project
        Gson gson = new Gson();
        return gson.toJson(userSchedule);
    }
}
