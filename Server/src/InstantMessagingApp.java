import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class InstantMessagingApp {

     static final String DATABASE_URL = "jdbc:sqlite:messaging.db";

    public static void main(String[] args) {
        // Create the database and tables if they don't exist
 
        // Example usage

        // Simulate a conversation

        // Display the conversation
    }

    static void createTables() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement()) {

            // Create a table to store users
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT UNIQUE)");

            // Create a table to store messages
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS messages ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "sender_id INTEGER, "
                    + "receiver_id INTEGER, "
                    + "content TEXT, "
                    + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (sender_id) REFERENCES users(id), "
                    + "FOREIGN KEY (receiver_id) REFERENCES users(id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     static void insertUser(String username) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT OR IGNORE INTO users (username) VALUES (?)")) {

            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     static String showUsers() {
    	 String total ="";
    	    try (Connection connection = DriverManager.getConnection(DATABASE_URL);
    	         Statement statement = connection.createStatement();
    	         ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
    	    	
    	        while (resultSet.next()) {
    	            int userId = resultSet.getInt("id");
    	            String username = resultSet.getString("username");

    	             total += String.valueOf(userId) + "," + username+" ";
    	        }
    	        

    	    } 
    	    catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	    return total;
    	}
     static String GetSenderMessages(String sender) {
    	 String total = "";
    	 try (Connection connection = DriverManager.getConnection(DATABASE_URL);
    	         Statement statement = connection.createStatement();
	    	         ResultSet resultSet = statement.executeQuery("SELECT * FROM messages WHERE sender_id = '" + sender + "'")) {
    	        while (resultSet.next()) {
    	            int messageId = resultSet.getInt("id");
    	            int senderId = resultSet.getInt("sender_id");
    	            int receiverId = resultSet.getInt("receiver_id");
    	            String content = resultSet.getString("content");
    	            Timestamp timestamp = resultSet.getTimestamp("timestamp");

    	            total += "("+messageId+")" + "\t" +  senderId + "\t" + receiverId + "\t" +content + "\t" +timestamp + "\n";
    	                               
    	        }

    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	 return total;
     
     }
     
     static String getMessages(String sender,String receiver) {
    	 String total = "";
    	 try (Connection connection = DriverManager.getConnection(DATABASE_URL);
    	         Statement statement = connection.createStatement();
	    	         ResultSet resultSet = statement.executeQuery("SELECT * FROM messages WHERE (sender_id = '" + sender + "' AND receiver_id = '" + receiver + "') OR (sender_id = '" + receiver + "' AND receiver_id = '" + sender + "')")) {
    	        while (resultSet.next()) {
    	            int messageId = resultSet.getInt("id");
    	            int senderId = resultSet.getInt("sender_id");
    	            int receiverId = resultSet.getInt("receiver_id");
    	            String content = resultSet.getString("content");
    	            Timestamp timestamp = resultSet.getTimestamp("timestamp");

    	            total += messageId+ "\t" +  senderId + "\t" + receiverId + "\t" +content + "\t" +timestamp + "\n";
    	                               
    	        }

    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	 return total;
     }
     
     static String showMessages() {
    	 String total ="";
    	    try (Connection connection = DriverManager.getConnection(DATABASE_URL);
    	         Statement statement = connection.createStatement();
    	         ResultSet resultSet = statement.executeQuery("SELECT * FROM messages")) {

    	        while (resultSet.next()) {
    	            int messageId = resultSet.getInt("id");
    	            int senderId = resultSet.getInt("sender_id");
    	            int receiverId = resultSet.getInt("receiver_id");
    	            String content = resultSet.getString("content");
    	            Timestamp timestamp = resultSet.getTimestamp("timestamp");

    	            total += "("+messageId+")" + "\t" +  senderId + "\t" + receiverId + "\t" +content + "\t" +timestamp + "\n";
    	                               
    	        }

    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	    return total;
    	}
     static String getSenderİd() {
    	 showMessages().split("\t");
    	 
    	 return null;
     }
   static void sendMessage(String sender, String receiver, String content) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO messages (sender_id, receiver_id, content) VALUES (" +
                     "(?), " +
                     "(?), ?)")) {
        	
            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, receiver);
            preparedStatement.setString(3, content);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     static void displayConversation(String user1, String user2) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT m.timestamp, u.username, m.content FROM messages m " +
                     "JOIN users u ON m.sender_id = u.id OR m.receiver_id = u.id " +
                     "WHERE (m.sender_id = (SELECT id FROM users WHERE username = ?) " +
                     "AND m.receiver_id = (SELECT id FROM users WHERE username = ?)) " +
                     "OR (m.sender_id = (SELECT id FROM users WHERE username = ?) " +
                     "AND m.receiver_id = (SELECT id FROM users WHERE username = ?)) " +
                     "ORDER BY m.timestamp")) {

            preparedStatement.setString(1, user1);
            preparedStatement.setString(2, user2);
            preparedStatement.setString(3, user2);
            preparedStatement.setString(4, user1);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String timestamp = resultSet.getString("timestamp");
                    String sender = resultSet.getString("username");
                    String content = resultSet.getString("content");

                    System.out.println(timestamp + " - " + sender + ": " + content);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}