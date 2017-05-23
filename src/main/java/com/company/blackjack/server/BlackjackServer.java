package com.company.blackjack.server;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BlackjackServer {

    private static final String CONFIG_FILE = "config.xml";
    private static final int DEFAULT_PORT = 8888;

    private Integer port;

    public BlackjackServer() {
        initXML();
    }

    private void initXML() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(getClass().getResourceAsStream("/" + CONFIG_FILE));

            String portStr = doc.getDocumentElement().getAttribute("port");
            port = !portStr.isEmpty() ? Integer.parseInt(portStr) : DEFAULT_PORT;

            RoomDispatcher roomDispatcher = RoomDispatcher.getInstance();
            roomDispatcher.parseXML(doc);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = null;
            while((socket = serverSocket.accept()) != null) {
                new ClientHandler(socket).connect();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BlackjackServer server = new BlackjackServer();
        System.out.println("Blackjack server up and running...");
        server.start();
    }
}
