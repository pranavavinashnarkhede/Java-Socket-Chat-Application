import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class Server
{
    // Port number on which the server listens
    private static final int PORT = 2101;

    // Stores all currently connected clients
    // ConcurrentHashMap provides thread-safe storage
    private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    public static void main(String[] args)
    {
        // Start the server and listen for connections
        try (ServerSocket serverSocket = new ServerSocket(PORT))
        {
            System.out.println("Chat Server Started...");
            System.out.println("Waiting for clients on port " + PORT);

            // Continuously accept new client connections
            while (true)
            {
                // Accept an incoming client connection
                Socket clientSocket = serverSocket.accept();

                // Create a handler for the connected client
                ClientHandler clientHandler = new ClientHandler(clientSocket);

                // Add the client to the connected clients collection
                clients.add(clientHandler);

                // Create a separate thread for the client
                Thread clientThread = new Thread(clientHandler);

                // Start communication with the client
                clientThread.start();
            }
        }
        catch (IOException e)
        {
            // Handle server errors
            System.out.println("Server Error: " + e.getMessage());
        }
    }

    // Broadcast a message to all connected clients
    public static void broadcast( String message, ClientHandler sender)
    {
        // Send the message to every connected client
        for (ClientHandler client : clients)
        {
            // Avoid sending messages to disconnected clients
            if (client.isConnected())
            {
                client.sendMessage(message);
            }
        }
    }

    // Remove a client when it disconnects
    public static void removeClient(ClientHandler client)
    {
        // Remove the client from the collection
        clients.remove(client);

        System.out.println("Client Removed");
    }
}