package Servlets;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

//class for creating session id's and cookies to track users
public class StudentTracker {
	
	private static final Map<String, String> studentMap = new HashMap<>();
	
	public static Cookie setUserSession(String email, HttpSession session) {
		// Generate a new session ID
        String studentId = UUID.randomUUID().toString();

        // Store the session ID and username in the session
        session.setAttribute("studentID", studentId);
        session.setAttribute("username", email);
        
        // Set a cookie with the session ID
        Cookie sessionCookie = new Cookie("session_id", sessionId);
        sessionCookie.setMaxAge(24 * 60 * 60); // Cookie expires in 24 hours
        studentMap.put(studentId, email);
        return sessionCookie;
	}
	
	public static void removeSession(String studentId) {
        studentMap.remove(studentId);
    }
	
	public static String getEmailFromSession(String studentId) {
        return studentMap.get(studentId);
    }

}