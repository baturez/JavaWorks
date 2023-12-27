import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import javax.websocket.Session;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Server {

	static ArrayList<String> messages = new ArrayList<String>();
	static Session session;
	static String total = null;
	static InstantMessagingApp sql = new InstantMessagingApp();
	static int port = 8989;

	public static WebSocket getsocket() {
		WebSocket socket = new WebSocket();
		return socket;
	}

	public static void runServer() throws IOException {

		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.create();

		server.createContext("/", new roothand());
		server.createContext("/users", new UserHandler());
		server.createContext("/conversation", new ConHandler());
		server.createContext("/message", new MessageHandler());
		server.createContext("/chat", new WebSocketHandler());

		server.start();
		System.out.println("Server started at " + port);

	}

	@ServerEndpoint("/chat")
	public static class WebSocketHandler implements HttpHandler {

		private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

		@OnOpen
		public void onOpen(Session session) {
			System.out.println("WebSocket connection opened: " + session.getId());
			sessions.add(session);

		}

		@OnMessage
		public void onMessage(String message, Session session) {
			System.out.println("Received WebSocket message: " + message);

			sendToAllConnectedSessions(message);
		}

		@OnClose
		public void onClose(Session session) {
			System.out.println("WebSocket connection closed: " + session.getId());
			sessions.remove(session);
		}

		@OnError
		public void onError(Session session, Throwable throwable) {
			System.out.println("WebSocket error: " + throwable.getMessage());
		}

		private void sendToAllConnectedSessions(String message) {
			for (Session session : sessions) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public static void sendMessageToAll(String message) {
			for (Session session : sessions) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void handle(HttpExchange he) throws IOException {
			he.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
			he.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
			he.getResponseHeaders().set("Access-Control-Allow-Methods", "POST");
			if ("POST".equals(he.getRequestMethod())) {
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8))) {
					String requestBody = reader.lines().collect(Collectors.joining("\n"));
					if (requestBody.split(",").length == 3) {
						sql.sendMessage(requestBody.split(",")[0], requestBody.split(",")[1],
								requestBody.split(",")[2]);
						total = sql.getMessages(requestBody.split(",")[0], requestBody.split(",")[1]);
						System.out.println(requestBody);
					}
				}
			}

			String filePath = "C:\\Users\\batuh\\eclipse_new\\Server_\\chat.html";
			java.nio.file.Path path = java.nio.file.Paths.get(filePath);
			byte[] fileBytes = java.nio.file.Files.readAllBytes(path);
			he.getResponseHeaders().set("Content-Type", "text/html");

			he.sendResponseHeaders(200, fileBytes.length);
			try (OutputStream os = he.getResponseBody()) {
				os.write(fileBytes);
			}
		}

	}

	public static void main(String[] args) throws IOException {
		runServer();
		sql.createTables();

	}

	public static void reciveMessage() {
		int a = sql.showMessages().split("\t").length;
		for (int i = 0; i < a; i++) {
			messages.add(sql.showMessages().split("\t")[i]);

		}
		if (messages.get(1) == sql.showUsers()) {

		}
	}

	public static class roothand implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
			if ("POST".equals(he.getRequestMethod())) {
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8))) {
					String requestBody = reader.lines().collect(Collectors.joining("\n"));
					sql.insertUser(requestBody);
					getsocket().onOpen(session);
				}
			}

			String filePath = "C:\\Users\\batuh\\eclipse_new\\Server_\\index.html";
			java.nio.file.Path path = java.nio.file.Paths.get(filePath);
			byte[] fileBytes = java.nio.file.Files.readAllBytes(path);
			he.getResponseHeaders().set("Content-Type", "text/html");

			he.sendResponseHeaders(200, fileBytes.length);
			try (OutputStream os = he.getResponseBody()) {
				os.write(fileBytes);
			}

		}

	}

	public static class ConHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange he) throws IOException {
			he.sendResponseHeaders(200, total.length());
			try (OutputStream os = he.getResponseBody()) {
				os.write(total.getBytes());
			}
		}
	}

	// public static class chattthand implements HttpHandler{

	// }

	public static class UserHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange t) throws IOException {

			t.sendResponseHeaders(200, sql.showUsers().length());
			try (OutputStream os = t.getResponseBody()) {
				os.write(sql.showUsers().getBytes());

			}
		}

	}

	public static class MessageHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange t) throws IOException {

			t.sendResponseHeaders(200, sql.showMessages().length());
			try (OutputStream os = t.getResponseBody()) {
				os.write(sql.showMessages().getBytes());

			}

		}

	}

}
