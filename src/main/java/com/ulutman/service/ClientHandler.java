package com.ulutman.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private DataInputStream is;
    private DataOutputStream os;
    private String name;
    private String partnerName;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            is = new DataInputStream(clientSocket.getInputStream());
            os = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Ошибка при получении потоков ввода/вывода: " + e.getMessage());
        }
    }

    public void sendMessage(String message) throws IOException {
        os.writeUTF(message);
    }

    public String getNamee() {
        return name;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    @Override
    public void run() {
        try {
            name = is.readUTF();
            sendMessage("#принято");
            System.out.println(name + " присоединился к чату");

            partnerName = is.readUTF();

            sendMessage(partnerName);

            while (true) {
                String message = is.readUTF();
                System.out.println(name + ": " + message);
                sendMessage(message);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обработке клиента: " + e.getMessage());
            server.removeClient(this);
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии сокета: " + e.getMessage());
            }
        }
    }
}



