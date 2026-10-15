import java.io.*;
import java.net.*;
import java.time.*;


class MyChatClient
{

    public static void main(String A[]) throws Exception
    {
        // Display the client application status.
        System.out.println("Client application is running");

        // Establish a connection with the server.
        Socket sobj = new Socket("localhost", 2101);

        System.out.println("Connection established successfully");

        // Create streams for sending and receiving messages.
        PrintStream pobj = new PrintStream(sobj.getOutputStream());

        BufferedReader bobj1 = new BufferedReader( new InputStreamReader(sobj.getInputStream()));

        // Create a reader for taking input from the user.
        BufferedReader bobj2 = new BufferedReader( new InputStreamReader(System.in));

        String str1 = null, str2 = null;

        // Display the chat client header.
        System.out.println("----------------------------------------");
        System.out.println("---------- Pranav Chat Client ----------");
        System.out.println("----------------------------------------");

        // Check whether previous chat history exists.
        File fobj = new File("ChatHistory.txt");

        String sRet = null;

        if (fobj.exists())
        {
            System.out.println("Do you want to recover previous chat history? (Yes/No)");

            sRet = bobj2.readLine();

            if (sRet.equals("Yes"))
            {
                // Read and display the previous chat history.
                FileReader frobj = new FileReader(fobj);
                BufferedReader brobj = new BufferedReader(frobj);

                String line = null;

                while ((line = brobj.readLine()) != null)
                {
                    System.out.println(line);
                }

                brobj.close();
                frobj.close();
            }
            else
            {
                // Rename the existing history file as a backup.
                File oldfile = new File("ChatHistory.txt");
                File newfile = new File("ChatBackup.txt");

                oldfile.renameTo(newfile);
            }
        }

        // Create the chat history file if it does not exist.
        fobj.createNewFile();

        // Open the history file in append mode.
        FileWriter fwobj = new FileWriter("ChatHistory.txt", true);

        BufferedWriter bobj = new BufferedWriter(fwobj);

        // Record the current date and time for the chat session.
        LocalTime time = LocalTime.now().withNano(0);

        LocalDate today = LocalDate.now();

        // Write the chat session header to the history file.
        bobj.newLine();
        bobj.newLine();
        bobj.write("==========Chat History==========");
        bobj.newLine();
        bobj.newLine();

        bobj.write("Date : [" + today + "]");
        bobj.newLine();

        System.out.println("Enter message for server:");

        // Continue communication until the user enters "end".
        while (!(str1 = bobj2.readLine()).equals("end"))
        {
            // Store the client's message in the history file.
            bobj.write("[" + time + "] Client : " + str1);
            bobj.newLine();

            // Send the message to the server.
            pobj.println(str1);

            // Receive the server's response.
            str2 = bobj1.readLine();

            // Store the server's response in the history file.
            bobj.write("[" + time + "] Server : " + str2);
            bobj.newLine();

            // Display the server's response.
            System.out.println("The server says : " + str2);

            System.out.println("Enter message for server:");
        }

        // Write the closing separator to the history file.
        bobj.newLine();
        bobj.write("==============================");

        // Close the resources.
        bobj.close();
        fwobj.close();
        sobj.close();
    }
}

