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

/**
 * Servlet implementation class ChatroomServlet
 */
@WebServlet("/ChatroomServlet")
public class ChatroomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChatroomServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String user1Messages = null;
		String user2Messages = null;
		// IMPORTANT: Front end needs to send this parameter (maybe call another servlet to get it)
		// Could change this, but I need to know who the user is somehow
		String userId = request.getParameter("userId");
		try {
			// CHANGE THIS LINE for both get and post
			conn = DriverManager.getConnection("jdbc:mysql://localhost/factory?user=root&password=root");
			ps = conn.prepareStatement("SELECT * FROM studentTable s, friends f, messages m WHERE f.messages_id = m.message_id AND s.studentId = " + userId + ";");
			rs = ps.executeQuery();
			// idk if this while is necessary
			while (rs.next())
			{
				user1Messages = rs.getString("user1_messages");
				user2Messages = rs.getString("user2_messages");
			}
			
			// Parse which user is user 1 vs user 2
			String thisUserMessages = null;
			String otherUserMessages = null;
			
			if (userId == rs.getString("user1_Id"))
			{
				thisUserMessages = user1Messages;
				otherUserMessages = user2Messages;
			}
			else
			{
				thisUserMessages = user2Messages;
				otherUserMessages = user1Messages;
			}
			PrintWriter out = response.getWriter();
			out.print(generateJSON(thisUserMessages, otherUserMessages));
			out.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet getResult = null;
		
		// IMPORTANT: Front end needs to set these parameters
		String newMessage = request.getParameter("message");
		String userId = request.getParameter("userId");
		
		String user1Message = null;
		String user2Message = null;
		String messageId = null;
		try {
			// CHANGE THIS LINE
			conn = DriverManager.getConnection("jdbc:mysql://localhost/factory?user=root&password=root");
			ps = conn.prepareStatement("SELECT * FROM studentTable s, friends f, messages m WHERE f.messages_id = m.message_id AND s.studentId = " + userId + ";");
			getResult = ps.executeQuery();
			user1Message = getResult.getString("user1_messages");
			user2Message = getResult.getString("user2_messages");
			messageId = getResult.getString("message_id");
			
			if (userId == getResult.getString("user1_Id"))
			{
				user1Message = newMessage;
			}
			else
			{
				user2Message = newMessage;
			}
			
			ps = conn.prepareStatement("UPDATE messages SET user1_messages = '" + user1Message + 
					"', user2_messages = '" + user2Message + "' WHERE message_id = " + messageId + ";");
			ps.executeQuery();
			
			doGet(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private String generateJSON(String thisUser, String otherUser)
	{
		String output = "{";
		output += "\"thisUserMessages\":";
		output += "\"" + thisUser + "\",";
		output += "\"otherUserMessages\":";
		output += "\"" + otherUser + "\"}";
		return output;
	}
}
