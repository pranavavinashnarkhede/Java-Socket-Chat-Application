import java.io.*;
import java.net.*;


class MyChatServer
{

    public static void main(String A[]) throws Exception
    {
        // Display the server application status.
        System.out.println("Welcome to the server side");

        // Create a server socket on port 2101.
        ServerSocket ssobj = new ServerSocket(2101);

        System.out.println("Server is waiting at port 2101");

        // Wait for a client to establish a connection.
        Socket sobj = ssobj.accept();

        System.out.println("Connection is accepted successfully");

        // Create a stream for sending messages to the client.
        PrintStream pobj = new PrintStream(sobj.getOutputStream());

        // Create a reader for receiving messages from the client.
        BufferedReader bobj1 = new BufferedReader( new InputStreamReader(sobj.getInputStream()));

        // Create a reader for taking input from the server user.
        BufferedReader bobj2 = new BufferedReader( new InputStreamReader(System.in));

        String str1 = null, str2 = null;

        // Display the chat server header.
        System.out.println("----------------------------------------");
        System.out.println("---------- Pranav Chat Server ----------");
        System.out.println("----------------------------------------");

        // Continue communication while the client sends messages.
        while ((str1 = bobj1.readLine()) != null)
        {
            // Display the message received from the client.
            System.out.println("The client says : " + str1);

            // Read a response from the server user.
            System.out.println("Enter message for client:");
            str2 = bobj2.readLine();

            // Send the response to the client.
            pobj.println(str2);
        }

        // Close the server and client sockets.
        ssobj.close();
        sobj.close();
    }
}

