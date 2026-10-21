import java.io.*;
import java.net.*;

class MyChatServer
{
    private static final int SERVER_PORT = 2101;
    private static final String TERMINATION_COMMAND = "end";

    public static void main(String A[]) throws IOException
    {
        System.out.println("Welcome to the server side");

        // Start the server and wait for one client to connect.
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT))
        {
            System.out.println("Server is waiting at port " + SERVER_PORT);

            try (Socket clientSocket = serverSocket.accept();
                 PrintStream clientOutput = new PrintStream(clientSocket.getOutputStream());
                 BufferedReader clientInput = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
                 BufferedReader keyboard = new BufferedReader( new InputStreamReader(System.in)))
            {
                System.out.println("Connection accepted successfully");

                System.out.println("----------------------------------------");
                System.out.println("---------- Pranav Chat Server ----------");
                System.out.println("----------------------------------------");

                String message;

                // Receive client messages until the client disconnects
                // or sends the termination command.
                while ((message = clientInput.readLine()) != null)
                {
                    if (message.equalsIgnoreCase(TERMINATION_COMMAND))
                    {
                        System.out.println("Client requested to end the chat.");
                        break;
                    }

                    System.out.println("The client says : " + message);
                    System.out.println("Enter message for client:");

                    String response = keyboard.readLine();

                    // End the session if server input is closed.
                    if (response == null)
                    {
                        break;
                    }

                    clientOutput.println(response);
                }

                // This message is also shown if the client disconnects unexpectedly.
                System.out.println("Client connection closed.");
            }
        }

        System.out.println("Chat server terminated.");
    }
}
