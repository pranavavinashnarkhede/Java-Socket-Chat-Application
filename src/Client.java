import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Scanner;

class MyChatClient
{
    public static void main(String[] args)
    {
        // Server address and port
        final String SERVER_ADDRESS = "localhost";
        final int PORT = 2101;

        // Scanner reads input from the keyboard
        Scanner scanner = new Scanner(System.in);

        // Establish connection with the server
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT))
        {
            // Read messages received from the server
            BufferedReader reader = new BufferedReader( new InputStreamReader( socket.getInputStream()));

            // Send messages to the server
            PrintStream writer = new PrintStream( socket.getOutputStream());

            System.out.println("Connected to Chat Server");
            System.out.println("Type 'end' to disconnect");

            // Continue communication until termination
            while (true)
            {
                // Read message from the user
                System.out.print("Enter Message: ");

                String message = scanner.nextLine();

                // Reject empty or whitespace-only messages
                if (message.trim().isEmpty())
                {
                    System.out.println("Message cannot be empty");
                    continue;
                }

                // Send message to the server
                writer.println(message);

                // Handle termination request
                if (message.equalsIgnoreCase("end"))
                {
                    System.out.println("Disconnecting...");
                    break;
                }

                // Receive server response
                String response = reader.readLine();

                // Check whether the server disconnected
                if (response == null)
                {
                    System.out.println("Server Disconnected");
                    break;
                }

                // Display server response
                System.out.println("Server: " + response);
            }
        }
        catch (IOException e)
        {
            // Handle connection or communication errors
            System.out.println("Connection Error: " + e.getMessage());
        }

        // Close keyboard input
        scanner.close();

        System.out.println("Client Terminated");
    }
}