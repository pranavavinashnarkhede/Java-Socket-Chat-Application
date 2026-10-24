import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

class ClientHandler implements Runnable
{
    // Socket used for communication with this client
    private Socket clientSocket;

    // Reader receives messages from the client
    private BufferedReader reader;

    // Writer sends messages to the client
    private PrintWriter writer;

    // Indicates whether the client is connected
    private volatile boolean connected = true;

    // Constructor receives the client's socket
    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    // Execute client communication inside a separate thread
    @Override
    public void run()
    {
        try
        {
            // Create input stream to receive client messages
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Create output stream to send messages
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Display connected client information
            System.out.println("Client Connected: "+ clientSocket.getInetAddress());

            // Notify other clients about the new connection
            MyChatServer.broadcast("A new client has joined the chat",this);

            // Continuously receive messages from this client
            while (connected)
            {
                // Read message from the client
                String message = reader.readLine();

                // Check whether the client disconnected
                if (message == null)
                {
                    break;
                }

                // Handle client termination request
                if (message.equalsIgnoreCase("end"))
                {
                    break;
                }

                // Reject empty or whitespace-only messages
                if (message.trim().isEmpty())
                {
                    sendMessage("Message cannot be empty");
                    continue;
                }

                // Display received message on server
                System.out.println("Client Message: " + message);

                // Add sender information to the message
                String formattedMessage ="Client "+ clientSocket.getPort()+ ": "+ message;

                // Broadcast message to all connected clients
                MyChatServer.broadcast(formattedMessage,this);
            }
        }
        catch (IOException e)
        {
            // Handle communication errors
            System.out.println("Client Communication Error: "+ e.getMessage());
        }
        finally
        {
            // Disconnect the client safely
            disconnect();
        }
    }

    // Send a message to this particular client
    public synchronized void sendMessage(String message)
    {
        // Ensure the client is still connected
        if (connected && writer != null)
        {
            // Send message through the client's output stream
            writer.println(message);

            // Check whether sending failed
            if (writer.checkError())
            {
                disconnect();
            }
        }
    }

    // Check whether the client is connected
    public boolean isConnected()
    {
        return connected && clientSocket != null && !clientSocket.isClosed();
    }

    // Disconnect the client and release resources
    public synchronized void disconnect()
    {
        // Prevent repeated disconnection
        if (!connected)
        {
            return;
        }

        // Update connection status
        connected = false;

        // Remove client from server collection
        MyChatServer.removeClient(this);

        // Close the client socket
        try
        {
            if (clientSocket != null && !clientSocket.isClosed())
            {
                clientSocket.close();
            }
        }
        catch (IOException e)
        {
            System.out.println("Error closing client socket");
        }

        // Notify remaining clients
        MyChatServer.broadcast("A client has left the chat",this);

        // Display disconnection information
        System.out.println("Client Disconnected");
    }
}