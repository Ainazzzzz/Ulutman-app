package com.ulutman.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private Map<String, ClientHandler> clients = new HashMap<>(); // Храним клиентов по имени

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Чат-сервер запущен на порту 8080");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключен новый клиент");
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }

    public void sendMessageTo(String recipientName, String message) {
        ClientHandler client = clients.get(recipientName);
        if (client != null) {
            try {
                client.sendMessage(message);
            } catch (IOException e) {
                System.err.println("Ошибка при отправке сообщения клиенту: " + e.getMessage());
                // Можно удалить клиента из списка, если он не доступен
            }
        }
    }

    public void addClient(ClientHandler clientHandler) {
        clients.put(clientHandler.getNamee(), clientHandler);
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler.getNamee());
    }
}


