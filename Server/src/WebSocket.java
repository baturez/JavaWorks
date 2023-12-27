import java.io.IOException;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

public class WebSocket {
    static Session session ;

	@OnOpen
    public static void onOpen(Session session) {

    	System.out.println("WebSocket connection opened: " + session.getId());
        session = session;
        
        }

    @OnMessage
    public static void onMessage(String message, Session session) {
        System.out.println("Received WebSocket message: " + message);

        // Burada gerekli işlemleri yapabilirsiniz

        // Örnek: Gelen mesajı diğer tüm bağlantılara gönderme
        sendToAllConnectedSessions(message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket connection closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket error: " + throwable.getMessage());
    }

    private static void sendToAllConnectedSessions(String message) {
    	 Set<Session> openSessions = session.getOpenSessions();
         for (Session session : openSessions) {
             try {
                 session.getBasicRemote().sendText(message);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
    }
}
