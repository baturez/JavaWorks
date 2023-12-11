package net.codejava.networking.chat.client;

import java.net.*;
import java.io.*;
 





public class ChatClient {
    private String hostname;
    private int port;
    private String userName;
 
    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }
 
    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
 
            System.out.println("Connected to the chat server");
 
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
 
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
 
    }
 
    void setUserName(String userName) {
        this.userName = userName;
    }
 
    String getUserName() {
        return this.userName;
    }
 
 
    public static void main(String[] args) {
        if (args.length < 2) return;
  
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
 
        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
    public class ReadThread extends Thread {
        private BufferedReader reader;
        private Socket socket;
        private ChatClient client;
     
        public ReadThread(Socket socket, ChatClient client) {
            this.socket = socket;
            this.client = client;
     
            try {
                InputStream input = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
            } catch (IOException ex) {
                System.out.println("Error getting input stream: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        public void run() {
            while (true) {
                try {
                    String response = reader.readLine();
                    System.out.println("\n" + response);
     
                    // prints the username after displaying the server's message
                    if (client.getUserName() != null) {
                        System.out.print("[" + client.getUserName() + "]: ");
                    }
                } catch (IOException ex) {
                    System.out.println("Error reading from server: " + ex.getMessage());
                    ex.printStackTrace();
                    break;
                }
            }
        }
        
    }
    
    public class WriteThread extends Thread {
        private PrintWriter writer;
        private Socket socket;
        private ChatClient client;
     
        public WriteThread(Socket socket, ChatClient client) {
            this.socket = socket;
            this.client = client;
     
            try {
                OutputStream output = socket.getOutputStream();
                writer = new PrintWriter(output, true);
            } catch (IOException ex) {
                System.out.println("Error getting output stream: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
     
        public void run() {
     
            Console console = System.console();
     
            String userName = console.readLine("\nEnter your name: ");
            client.setUserName(userName);
            writer.println(userName);
     
            String text;
     
            do {
                text = console.readLine("[" + userName + "]: ");
                writer.println(text);
     
            } while (!text.equals("bye"));
     
            try {
                socket.close();
            } catch (IOException ex) {
     
                System.out.println("Error writing to server: " + ex.getMessage());
            }
        }
    }


}