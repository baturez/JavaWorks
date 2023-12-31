


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
public class Server {
	static String total = null;
	static InstantMessagingApp sql = new InstantMessagingApp();
	static int port = 8989;
	public static void main(String[] args) throws IOException {
		runServer();
		sql.createTables();
}
	public static void runServer() throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/", new roothand());
		server.createContext("/users", new UserHandler());
		server.createContext("/conversation", new ConHandler());
		server.createContext("/message", new MessageHandler());
		server.createContext("/chat", new WebSocketHandler());
		server.start();
		System.out.println("Server started at " + port);
}
	public static class WebSocketHandler implements HttpHandler {
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
						sql.sendMessage(requestBody.split(",")[0], requestBody.split(",")[1],requestBody.split(",")[2]);
						total = sql.getMessages(requestBody.split(",")[0], requestBody.split(",")[1]);
}}}
			String filePath = "C:\\Users\\batuh\\eclipse_server\\ServerExemple\\chat.html";
			java.nio.file.Path path = java.nio.file.Paths.get(filePath);
			byte[] fileBytes = java.nio.file.Files.readAllBytes(path);
			he.getResponseHeaders().set("Content-Type", "text/html");
			he.sendResponseHeaders(200, fileBytes.length);
			try (OutputStream os = he.getResponseBody()) {
				os.write(fileBytes);
}}}
	public static class roothand implements HttpHandler {
		@Override
		public void handle(HttpExchange he) throws IOException {
			if ("POST".equals(he.getRequestMethod())) {
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8))) {
					String requestBody = reader.lines().collect(Collectors.joining("\n"));
					sql.insertUser(requestBody);
}}
			String filePath = "C:\\Users\\batuh\\eclipse_server\\ServerExemple\\index.html";
			java.nio.file.Path path = java.nio.file.Paths.get(filePath);
			byte[] fileBytes = java.nio.file.Files.readAllBytes(path);
			he.getResponseHeaders().set("Content-Type", "text/html");
			he.sendResponseHeaders(200, fileBytes.length);
			try (OutputStream os = he.getResponseBody()) {
				os.write(fileBytes);
}}}
	public static class ConHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange he) throws IOException {
			he.sendResponseHeaders(200, total.length());
			try (OutputStream os = he.getResponseBody()) {
				os.write(total.getBytes());
				}}}
	public static class UserHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			t.sendResponseHeaders(200, sql.showUsers().length());
			try (OutputStream os = t.getResponseBody()) {
				os.write(sql.showUsers().getBytes());
}}}
	public static class MessageHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			t.sendResponseHeaders(200, sql.showMessages().length());
			try (OutputStream os = t.getResponseBody()) {
				os.write(sql.showMessages().getBytes());
			}}}}