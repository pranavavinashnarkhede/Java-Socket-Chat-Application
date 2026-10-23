import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

class MyChatServer
{
    public static void main(String[] args)
    {
        // Port number on which the server listens
        final int PORT = 2101;

        // ServerSocket is responsible for accepting client connections
        try (ServerSocket serverSocket = new ServerSocket(PORT))
        {
            System.out.println("Chat Server Started...");
            System.out.println("Waiting for clients on port " + PORT);

            // Server continuously waits for new client connections
            while (true)
            {
                // Accept the incoming client connection
                Socket clientSocket = serverSocket.accept();

                System.out.println("Client Connected: "+ clientSocket.getInetAddress());

                // Create a separate handler for this client
                ClientHandler clientHandler = new ClientHandler(clientSocket);

                // Create a new thread for the connected client
                Thread clientThread = new Thread(clientHandler);

                // Start the client thread
                clientThread.start();
            }
        }
        catch (IOException e)
        {
            // Handle server startup or connection errors
            System.out.println("Server Error: " + e.getMessage());
        }
    }
}