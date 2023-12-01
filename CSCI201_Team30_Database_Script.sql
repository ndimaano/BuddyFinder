CREATE database BuddyFinder;

-- Student Table
CREATE TABLE BuddyFinder.studentTable (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE BuddyFinder.scheduleTable (
    eventID INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    day_of_week VARCHAR(10),
    startTime TIME,
    endTime TIME,
    eventName VARCHAR(255),
    PRIMARY KEY (eventID),
    FOREIGN KEY (username) REFERENCES studentTable(username)
);

-- Friends Table
CREATE TABLE BuddyFinder.friends (
    friendship_id INT PRIMARY KEY,
    username VARCHAR(50),
    username2 VARCHAR(50),
    messages_id INT,
    FOREIGN KEY (username) REFERENCES studentTable(username),
    FOREIGN KEY (username2) REFERENCES studentTable(username),
    FOREIGN KEY (messages_id) REFERENCES messages(messages_id)
);

-- Chat messages table
-- For now, just a varchar of the most recent message from each side
-- Call ChatroomServlet GET and PUT to update

CREATE TABLE BuddyFinder.messages (
    messages_id INT PRIMARY KEY,
    user1_messages VARCHAR(50),
    user2_messages VARCHAR(50)
);
