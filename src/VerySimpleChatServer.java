import java.io.*;
import java.net.*;
import java.util.*;


public class VerySimpleChatServer
{
	/*
	 * String[] adviceList = {"Take smaller bites", "Go for the tight jeans. No they do NOT make you look fat",
        "One word: inappropriate", "Just for today, be honest.  Tell your boss what you *really* think", 
        "You might want to rethink that haircut"};
	 */
	
	static Map<String, String> advices = new HashMap<>();
	
	static {
		advices.put("advice1", "Take smaller bites");
		advices.put("advice2", "Go for the tight jeans. No they do NOT make");
		advices.put("advice3", "Just for today, be honest");
	}
    ArrayList clientOutputStreams;
    
    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;
        
        public ClientHandler(Socket clientSOcket) {
            try {
                sock = clientSOcket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
                
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    tellEveryone(message);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
    
    public static void main(String[] args) {
        new VerySimpleChatServer().go();
    }
    
    public void go() {
        clientOutputStreams = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(5000);
            while(true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);
                
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got a connection");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    public void tellEveryone(String message) {
        Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}
