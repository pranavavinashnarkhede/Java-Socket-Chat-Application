import java.io.*;
import java.net.*;
import java.time.*;


class MyChatClient
{
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 2101;
    private static final String TERMINATION_COMMAND = "end";

    public static void main(String A[]) throws IOException
    {
        System.out.println("Client application is running");

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        File historyFile = new File("ChatHistory.txt");

        // Recover or back up previous chat history.
        if (historyFile.exists())
        {
            System.out.println("Do you want to recover previous chat history? (Yes/No)");
            String choice = keyboard.readLine();

            if ("Yes".equalsIgnoreCase(choice))
            {
                try (BufferedReader historyReader =new BufferedReader(new FileReader(historyFile)))
                {
                    String line;
                    while ((line = historyReader.readLine()) != null)
                    {
                        System.out.println(line);
                    }
                }
            }
            else if ("No".equalsIgnoreCase(choice))
            {
                File backupFile = new File("ChatBackup.txt");
                if (!historyFile.renameTo(backupFile))
                {
                    System.out.println("Unable to back up previous chat history.");
                }
            }
        }

        if (!historyFile.exists())
        {
            historyFile.createNewFile();
        }

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintStream serverOutput =new PrintStream(socket.getOutputStream());
             BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter historyWriter =new BufferedWriter(new FileWriter(historyFile, true)))
        {
            System.out.println("Connection established successfully");
            System.out.println("----------------------------------------");
            System.out.println("---------- Pranav Chat Client ----------");
            System.out.println("----------------------------------------");

            LocalDate today = LocalDate.now();
            historyWriter.newLine();
            historyWriter.newLine();
            historyWriter.write("==========Chat History==========");
            historyWriter.newLine();
            historyWriter.write("Date : [" + today + "]");
            historyWriter.newLine();
            historyWriter.flush();

            System.out.println("Enter message for server (type 'end' to exit):");

            String message;
            while ((message = keyboard.readLine()) != null)
            {
                // Check the termination command before validating message text.
                if (message.equalsIgnoreCase(TERMINATION_COMMAND))
                {
                    serverOutput.println(TERMINATION_COMMAND);
                    break;
                }

                // Reject empty and whitespace-only messages.
                if (message.trim().isEmpty())
                {
                    System.out.println("Message cannot be empty. Please enter a message.");
                    System.out.println("Enter message for server (type 'end' to exit):");
                    continue;
                }

                LocalTime time = LocalTime.now().withNano(0);
                historyWriter.write("[" + time + "] Client : " + message);
                historyWriter.newLine();
                historyWriter.flush();

                serverOutput.println(message);

                String response = serverInput.readLine();
                if (response == null)
                {
                    System.out.println("The server disconnected.");
                    break;
                }

                time = LocalTime.now().withNano(0);
                historyWriter.write("[" + time + "] Server : " + response);
                historyWriter.newLine();
                historyWriter.flush();

                System.out.println("The server says : " + response);
                System.out.println("Enter message for server (type 'end' to exit):");
            }

            historyWriter.newLine();
            historyWriter.write("==============================");
            historyWriter.newLine();
            System.out.println("Chat client terminated.");
        }
        catch (ConnectException e)
        {
            System.out.println("Unable to connect. Make sure the server is running on port "+ SERVER_PORT + ".");
        }
        catch (SocketException e)
        {
            System.out.println("Connection to the server was lost. Chat session ended.");
        }
    }
}
