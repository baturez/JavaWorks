import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import netscape.javascript.JSObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Server {
 
    static ArrayList<String> Users = new ArrayList<>();
    static ArrayList<String> rawUsers = new ArrayList<>();

    static ArrayList<String> Message = new ArrayList<>();
    
    static int port = 8989; 
    public Server(int port) {
	this.port = port;
}

    public static void runServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new roothand());
        server.createContext("/users",new UserHandler());
        server.createContext("/message",new MessageHandler());
        server.createContext("/chat",new ChatHandler());
        server.createContext("/chatt",new ChattHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        
        server.start();
        System.out.println("Server started at " + port);
        
    }

    public void execute() {
    	
    }
    
    public static void main(String[] args) throws IOException {
        runServer();
    }
    private static String getJson() {
        // Your logic to generate JSON data
        String usersString = "[" + String.join(", ", Users) + "]";
        return "{" + "\"users\":" + usersString + "}";
    }
    private static String getJson_m() {
        // Your logic to generate JSON data
        String usersString = String.join(",", Message);
        return  usersString ;
    }
    

    public static class roothand implements HttpHandler{

		@Override
		public void handle(HttpExchange he) throws IOException {
			if ("POST".equals(he.getRequestMethod())) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8))) {
                    String requestBody = reader.lines().collect(Collectors.joining("\n"));
                    rawUsers.add(requestBody);
                    Users.add('"'+requestBody+'"');
                    System.out.println(requestBody);
                }
            }

            String filePath = "C:\\Users\\batuh\\eclipse_new\\Server\\index.html"; 
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            byte[] fileBytes = java.nio.file.Files.readAllBytes(path);
            he.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(fileBytes);
            }
			
		}
    	
    }
    
    
    
    
   

    
    public static class ChatHandler implements HttpHandler{

		@Override
		public void handle(HttpExchange he) throws IOException {
			String filePath = "C:\\Users\\batuh\\eclipse_new\\Server\\chat.html"; 
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            byte[] fileBytes = java.nio.file.Files.readAllBytes(path);
            he.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(fileBytes);
            }
			
		}
    	
    }
    public static void writeAll() throws FileNotFoundException {
    	PrintWriter pr = new PrintWriter("Users.txt");
    	pr.print("{\"users\":[]}");
    	PrintWriter pr1 = new PrintWriter("Messages.txt");
    	pr1.print("{\"messages\":[]}");
    	pr1.close();
    	pr.close();
    }
    
    public static class UserHandler implements HttpHandler{

		@Override
		public void handle(HttpExchange t) throws IOException {
			String jsonflie = getJson();
			 String path = "Users.txt";

		        // Write JSON data to Users.txt
		        Files.write(Paths.get(path), jsonflie.getBytes());
		        String fileContent = new String(Files.readAllBytes(Paths.get(path)));

		        // Respond to the client
		        t.sendResponseHeaders(200, fileContent.length());
		        try (OutputStream os = t.getResponseBody()) {
		            os.write(fileContent.getBytes());
		        }
		}
    	
    }
    
    public static class MessageHandler implements HttpHandler{
    	 private String generateMessageJSON() {
             return "{"+ '"' +"messages" + '"' +":" +"["+Message.stream().map(message ->  message ).collect(Collectors.joining("")) +  "]"+"}";
         }

		@Override
		public void handle(HttpExchange t) throws IOException {
			String jsonflie = generateMessageJSON();
			String path= "Messages.txt";
			Files.write(Paths.get(path), jsonflie.getBytes());
			String fileContent = new String(Files.readAllBytes(Paths.get(path)));
//			 String jsonFile = generateMessageJSON();
//	            t.getResponseHeaders().set("Content-Type", "application/json");
	//            t.sendResponseHeaders(200, jsonFile.length());
	  //          try (OutputStream os = t.getResponseBody()) {
	    //            os.write(jsonFile.getBytes());
	      //      }
			t.sendResponseHeaders(200, fileContent.length());
			try (OutputStream os = t.getResponseBody())
			{
				os.write(fileContent.getBytes());
				
			} 
		
		}
    	
    }

    public static class ChattHandler implements HttpHandler {
    	 private String generateMessageJSON() {
             return "[" + Message.stream().map(message -> "\"" + message + "\"").collect(Collectors.joining(",")) + "]" ;
         }



    	  private byte[] combineByteArrays(byte[] array1, byte[] array2) {
              byte[] combined = new byte[array1.length + array2.length];
              System.arraycopy(array1, 0, combined, 0, array1.length);
              System.arraycopy(array2, 0, combined, array1.length, array2.length);
              return combined;
          }

        @Override
        public void handle(HttpExchange he) throws IOException {
        	
        	if ("POST".equals(he.getRequestMethod())) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8))) {
                    String requestBody = reader.lines().collect(Collectors.joining("\n"));
                        if (Message.size()>=rawUsers.size()+1) {
							Message.add(","+requestBody);
                        	
                        }
                        else
                        {
                        	Message.add(requestBody);
                        }
                        
                        
				}
            }

            String messageListHTML = generateMessageJSON();
            String filePath = "C:\\Users\\batuh\\eclipse_new\\Server\\cchat.html"; 
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            byte[] fileBytes = java.nio.file.Files.readAllBytes(path);
            he.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(fileBytes);
            }
        }
    }



}


