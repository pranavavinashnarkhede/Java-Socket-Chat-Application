import java.io.*;
import java.net.*;

class MyChatServer
{
    private static final int SERVER_PORT = 2101;
    private static final String TERMINATION_COMMAND = "end";

    public static void main(String A[]) throws IOException
    {
        System.out.println("Welcome to the server side");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT))
        {
            System.out.println("Server is waiting at port " + SERVER_PORT);

            try (Socket clientSocket = serverSocket.accept();
                 PrintStream clientOutput =new PrintStream(clientSocket.getOutputStream());
                 BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in)))
            {
                System.out.println("Connection accepted successfully");
                System.out.println("----------------------------------------");
                System.out.println("---------- Pranav Chat Server ----------");
                System.out.println("----------------------------------------");

                String message;
                while ((message = clientInput.readLine()) != null)
                {
                    if (message.equalsIgnoreCase(TERMINATION_COMMAND))
                    {
                        System.out.println("Client requested to end the chat.");
                        break;
                    }

                    // Validate the message received from the client.
                    if (message.trim().isEmpty())
                    {
                        clientOutput.println("Message cannot be empty. Please enter a message.");
                        continue;
                    }

                    System.out.println("The client says : " + message);
                    System.out.println("Enter message for client:");

                    String response = keyboard.readLine();
                    if (response == null)
                    {
                        break;
                    }

                    // Validate the server's response before sending it.
                    if (response.trim().isEmpty())
                    {
                        clientOutput.println("Server response cannot be empty. Please try again.");
                        continue;
                    }

                    clientOutput.println(response);
                }

                System.out.println("Client connection closed.");
            }
        }

        System.out.println("Chat server terminated.");
    }
}
