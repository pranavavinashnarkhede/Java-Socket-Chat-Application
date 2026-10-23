import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;

class ClientHandler implements Runnable
{
    // Socket used for communication with this client
    private Socket clientSocket;

    // Constructor receives the connected client's socket
    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    // This method executes when the client thread starts
    @Override
    public void run()
    {
        // Create communication streams for this client
        try
        {
            // Read messages sent by the client
            BufferedReader reader = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));

            // Send responses to the client
            PrintStream writer = new PrintStream( clientSocket.getOutputStream());

            // Display the client address
            System.out.println("Client Handler Started: " + clientSocket.getInetAddress());

            // Continuously receive messages from this client
            while (true)
            {
                // Read a message from the client
                String message = reader.readLine();

                // Check whether the client disconnected
                if (message == null)
                {
                    System.out.println("Client Disconnected: " + clientSocket.getInetAddress());

                    break;
                }

                // Handle client termination request
                if (message.equalsIgnoreCase("end"))
                {
                    System.out.println("Client requested termination");

                    // Send termination response
                    writer.println("Connection terminated");

                    break;
                }

                // Reject empty or whitespace-only messages
                if (message.trim().isEmpty())
                {
                    writer.println("Message cannot be empty");

                    continue;
                }

                // Display the received message
                System.out.println("Client Message: " + message);

                // Generate response for the client
                String response = "Server received: " + message;

                // Send response to this client
                writer.println(response);
            }
        }
        catch (IOException e)
        {
            // Handle communication errors for this client
            System.out.println("Client Communication Error: " + e.getMessage());
        }
        finally
        {
            // Close the client socket when communication ends
            try
            {
                if (clientSocket != null
                        && !clientSocket.isClosed())
                {
                    clientSocket.close();
                }
            }
            catch (IOException e)
            {
                System.out.println("Error closing client socket");
            }

            // Display thread termination
            System.out.println("Client Handler Terminated");
        }
    }
}