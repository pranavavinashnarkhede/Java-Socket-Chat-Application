import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

class MyChatClient
{
    public static void main(String[] args)
    {
        // Server address
        final String SERVER_ADDRESS = "localhost";

        // Server port number
        final int PORT = 2101;

        // Scanner reads messages from keyboard
        Scanner scanner = new Scanner(System.in);

        // Establish connection with server
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT))
        {
            // Reader receives messages from server
            BufferedReader reader = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            // Writer sends messages to server
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to Chat Server");
            System.out.println("Type 'end' to disconnect");

            // Create a separate thread for receiving messages
            Thread receiverThread = new Thread(() ->
            {
                try
                {
                    // Continuously receive messages
                    while (!socket.isClosed())
                    {
                        // Read message from server
                        String response = reader.readLine();

                        // Stop receiving if server disconnects
                        if (response == null)
                        {
                            System.out.println("Server Disconnected");

                            break;
                        }

                        // Display received message
                        System.out.println("\n" + response);
                    }
                }
                catch (IOException e)
                {
                    // Handle receiving errors
                    if (!socket.isClosed())
                    {
                        System.out.println("Receiving Error: "+ e.getMessage());
                    }
                }
            });

            // Start the receiving thread
            receiverThread.start();

            // Main thread handles keyboard input
            while (!socket.isClosed())
            {
                // Read message from user
                System.out.print("Enter Message: ");

                String message = scanner.nextLine();

                // Reject empty messages
                if (message.trim().isEmpty())
                {
                    System.out.println("Message cannot be empty");

                    continue;
                }

                // Send message to server
                writer.println(message);

                // Handle termination request
                if (message.equalsIgnoreCase("end"))
                {
                    System.out.println("Disconnecting...");

                    break;
                }
            }

            // Wait for receiving thread to finish
            receiverThread.join();
        }
        catch (IOException e)
        {
            // Handle connection errors
            System.out.println("Connection Error: "+ e.getMessage());
        }
        catch (InterruptedException e)
        {
            // Restore interrupted status
            Thread.currentThread().interrupt();

            System.out.println("Client interrupted");
        }
        finally
        {
            // Close keyboard input
            scanner.close();
        }

        System.out.println("Client Terminated");
    }
}