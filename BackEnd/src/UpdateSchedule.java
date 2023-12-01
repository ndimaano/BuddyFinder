import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

@WebServlet("/UpdateSchedule")
public class UpdateSchedule extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://your-database-url:3306/buddyFinder";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }


        EventData eventData = extractEventDataFromJson(jsonData.toString());

        if (eventData != null) {
        	
            boolean success = addEventToSchedule(eventData);

            // Send the response
            response.setContentType("text/plain");
            response.getWriter().println(success ? "success" : "error");
        } else {
            // Handle invalid JSON data
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid JSON data");
        }
    }

    private EventData extractEventDataFromJson(String jsonData) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonData, EventData.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        }
    }




    private boolean addEventToSchedule(EventData eventData) {
    	try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO scheduleTable (username, day_of_week, startTime, endTime, eventName) " +
                                "VALUES (?, ?, ?, ?, ?)")) {

               preparedStatement.setString(1, eventData.getUsername());
               preparedStatement.setString(2, eventData.getDay());
               preparedStatement.setString(3, eventData.getStartTime());
               preparedStatement.setString(4, eventData.getEndTime());
               preparedStatement.setString(5, eventData.getEventName());

               int rowsAffected = preparedStatement.executeUpdate();
               return rowsAffected > 0;
           } catch (SQLException e) {
               e.printStackTrace(); // Handle the exception properly in a real application
               return false;
           }
    }
    
    private class EventData {
    	
        private String username;
        private String day;
        private String startTime;
        private String eventID;
        private String endTime;
        private String eventName;
    	
        public EventData(String username, String day, String startTime, String endTime, String eventName, String eventID) {
            this.username = username;
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
            this.eventName = eventName;
            this.eventID = eventID;
        }
    	
        public String getUsername() {
            return username;
        }

        public String getDay() {
            return day;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getEventName() {
            return eventName;
        }
        public String getEventID() {
        	return eventID;
        }
    }
}