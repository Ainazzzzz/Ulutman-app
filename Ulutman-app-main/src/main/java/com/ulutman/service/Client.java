package com.ulutman.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;
    private String name; // Имя текущего клиента

    public void connectToServer() {
        try {
            socket = new Socket("localhost", 8080);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            System.out.println("Подключен к чат-серверу.");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите ваше имя: ");
            name = scanner.nextLine();

            sendMessage(name);

            String confirmation = is.readUTF();
            if (confirmation.equals("#принято")) {
                System.out.println("Имя принято");
            }

            String partnerName = is.readUTF();
            System.out.println("Имя вашего партнера: " + partnerName);

            while (true) {
                System.out.print("Клиент " + name + ": ");
                String message = scanner.nextLine();

                sendMessage(message);

                if (message.equalsIgnoreCase("/exit")) {
                    break;
                }

                String response = is.readUTF();
                System.out.println(partnerName + ": " + response);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при подключении к серверу: " + e.getMessage());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }

    private void sendMessage(String message) throws IOException {
        os.writeUTF(message);
        os.flush();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connectToServer();
    }

    public class Client2 {
        private Socket socket;
        private DataInputStream is;
        private DataOutputStream os;
        private String name; // Имя текущего клиента

        public void connectToServer() {
            try {
                socket = new Socket("localhost", 8080);
                is = new DataInputStream(socket.getInputStream());
                os = new DataOutputStream(socket.getOutputStream());
                System.out.println("Подключен к чат-серверу.");

                Scanner scanner = new Scanner(System.in);
                System.out.print("Введите ваше имя: ");
                name = scanner.nextLine();

                sendMessage(name);

                String confirmation = is.readUTF();
                if (confirmation.equals("#принято")) {
                    System.out.println("Имя принято");
                }

                String partnerName = is.readUTF();
                System.out.println("Имя вашего партнера: " + partnerName);

                while (true) {
                    System.out.print("Клиент " + name + ": ");
                    String message = scanner.nextLine();

                    sendMessage(message);

                    if (message.equalsIgnoreCase("/exit")) {
                        break;
                    }

                    String response = is.readUTF();
                    System.out.println(partnerName + ": " + response);
                }

            } catch (IOException e) {
                System.err.println("Ошибка при подключении к серверу: " + e.getMessage());
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
                }
            }
        }

        private void sendMessage(String message) throws IOException {
            os.writeUTF(message);
            os.flush();
        }

        public static void main(String[] args) {
            Client client = new Client();
            client.connectToServer();
        }
    }
}




